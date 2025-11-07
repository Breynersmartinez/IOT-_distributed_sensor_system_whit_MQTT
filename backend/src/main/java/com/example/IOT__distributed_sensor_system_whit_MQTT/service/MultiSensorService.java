package com.example.IOT__distributed_sensor_system_whit_MQTT.service;

import com.example.IOT__distributed_sensor_system_whit_MQTT.model.Sensor;
import com.example.IOT__distributed_sensor_system_whit_MQTT.model.SensorNode;
import com.example.IOT__distributed_sensor_system_whit_MQTT.repository.SensorRepository;
import com.example.IOT__distributed_sensor_system_whit_MQTT.repository.SensorNodeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class MultiSensorService {

    private static final Logger logger = LoggerFactory.getLogger(MultiSensorService.class);

    private final MqttPublisher mqttPublisher;
    private final SensorRepository sensorRepository;
    private final SensorNodeRepository sensorNodeRepository; // NUEVO: Inyectar repositorio de nodos
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Map para almacenar los nodos sensores y sus estados de streaming EN MEMORIA
    private final Map<String, SensorNode> sensorNodes = new ConcurrentHashMap<>();
    private final Map<String, AtomicBoolean> streamingStates = new ConcurrentHashMap<>();
    private final Map<String, Thread> sensorThreads = new ConcurrentHashMap<>();

    public MultiSensorService(MqttPublisher mqttPublisher,
                              SensorRepository sensorRepository,
                              SensorNodeRepository sensorNodeRepository) { // NUEVO: Agregar en constructor
        this.mqttPublisher = mqttPublisher;
        this.sensorRepository = sensorRepository;
        this.sensorNodeRepository = sensorNodeRepository;

        // NUEVO: Cargar los nodos que ya existen en la BD
        loadSensorNodesFromDatabase();
    }

    /**
     * NUEVO: Carga todos los nodos sensores desde la base de datos
     */
    private void loadSensorNodesFromDatabase() {
        try {
            List<SensorNode> persistedNodes = sensorNodeRepository.findAll();
            persistedNodes.forEach(node -> {
                sensorNodes.put(node.getNodeId(), node);
                streamingStates.put(node.getNodeId(), new AtomicBoolean(false));
            });
            logger.info("Se cargaron {} nodos desde la base de datos", persistedNodes.size());
        } catch (Exception e) {
            logger.error("Error al cargar nodos desde la BD: {}", e.getMessage(), e);
        }
    }

    /**
     * Registra un nuevo nodo sensor Y LO PERSISTE EN BD
     */
    public void registerSensorNode(SensorNode node) {
        if (node.getNodeId() == null || node.getNodeId().isEmpty()) {
            logger.warn("No se puede registrar un nodo sin ID");
            return;
        }

        try {
            // NUEVO: Guardar en la base de datos
            SensorNode savedNode = sensorNodeRepository.save(node);

            // Guardar en memoria
            sensorNodes.put(savedNode.getNodeId(), savedNode);
            streamingStates.put(savedNode.getNodeId(), new AtomicBoolean(false));

            logger.info("Nodo sensor registrado y persistido: {}", savedNode);
        } catch (Exception e) {
            logger.error("Error al registrar nodo sensor: {}", e.getMessage(), e);
        }
    }

    /**
     * Registra múltiples nodos sensores de una sola vez
     */
    public void registerMultipleSensorNodes(List<SensorNode> nodes) {
        try {
            // NUEVO: Guardar todos en la base de datos
            List<SensorNode> savedNodes = sensorNodeRepository.saveAll(nodes);

            // Guardar en memoria
            savedNodes.forEach(node -> {
                sensorNodes.put(node.getNodeId(), node);
                streamingStates.put(node.getNodeId(), new AtomicBoolean(false));
            });

            logger.info("Se registraron {} nodos sensores", savedNodes.size());
        } catch (Exception e) {
            logger.error("Error al registrar múltiples nodos: {}", e.getMessage(), e);
        }
    }

    /**
     * Obtiene todos los nodos registrados
     */
    public List<SensorNode> getAllSensorNodes() {
        return new ArrayList<>(sensorNodes.values());
    }

    /**
     * Obtiene un nodo específico por ID
     */
    public SensorNode getSensorNode(String nodeId) {
        return sensorNodes.get(nodeId);
    }

    /**
     * NUEVO: Elimina un nodo sensor
     */
    public void deleteSensorNode(String nodeId) {
        try {
            // Detener si está en streaming
            if (streamingStates.get(nodeId) != null && streamingStates.get(nodeId).get()) {
                stopSensorNode(nodeId);
            }

            // Eliminar de BD
            sensorNodeRepository.deleteById(nodeId);

            // Eliminar de memoria
            sensorNodes.remove(nodeId);
            streamingStates.remove(nodeId);
            sensorThreads.remove(nodeId);

            logger.info("Nodo sensor eliminado: {}", nodeId);
        } catch (Exception e) {
            logger.error("Error al eliminar nodo {}: {}", nodeId, e.getMessage(), e);
        }
    }

    /**
     * NUEVO: Actualiza un nodo sensor
     */
    public SensorNode updateSensorNode(String nodeId, SensorNode updatedNode) {
        try {
            SensorNode existing = sensorNodes.get(nodeId);
            if (existing == null) {
                logger.warn("Nodo no encontrado: {}", nodeId);
                return null;
            }

            // Actualizar campos
            existing.setNodeName(updatedNode.getNodeName());
            existing.setLocation(updatedNode.getLocation());
            existing.setMqttTopic(updatedNode.getMqttTopic());

            // Guardar en BD
            SensorNode saved = sensorNodeRepository.save(existing);

            // Actualizar en memoria
            sensorNodes.put(nodeId, saved);

            logger.info("Nodo actualizado: {}", nodeId);
            return saved;
        } catch (Exception e) {
            logger.error("Error al actualizar nodo {}: {}", nodeId, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Inicia el streaming de un nodo sensor específico
     */
    public void startSensorNode(String nodeId) {
        SensorNode node = sensorNodes.get(nodeId);
        AtomicBoolean streamingState = streamingStates.get(nodeId);

        if (node == null) {
            logger.warn("Nodo sensor no encontrado: {}", nodeId);
            return;
        }

        if (streamingState.get()) {
            logger.warn("El nodo {} ya está transmitiendo", nodeId);
            return;
        }

        streamingState.set(true);
        node.setActive(true);

        // NUEVO: Actualizar estado en BD
        try {
            sensorNodeRepository.save(node);
        } catch (Exception e) {
            logger.error("Error al actualizar estado del nodo en BD: {}", e.getMessage());
        }

        Thread sensorThread = new Thread(() -> streamSensorValues(nodeId));
        sensorThread.setName("SensorNode-" + nodeId);
        sensorThread.start();

        sensorThreads.put(nodeId, sensorThread);
        logger.info("Iniciado streaming del nodo: {}", nodeId);
    }

    /**
     * Detiene el streaming de un nodo sensor específico
     */
    public void stopSensorNode(String nodeId) {
        AtomicBoolean streamingState = streamingStates.get(nodeId);
        SensorNode node = sensorNodes.get(nodeId);

        if (streamingState == null || !streamingState.get()) {
            logger.warn("El nodo {} no está transmitiendo", nodeId);
            return;
        }

        streamingState.set(false);
        if (node != null) {
            node.setActive(false);

            // NUEVO: Actualizar estado en BD
            try {
                sensorNodeRepository.save(node);
            } catch (Exception e) {
                logger.error("Error al actualizar estado del nodo en BD: {}", e.getMessage());
            }
        }
        logger.info("Detenido streaming del nodo: {}", nodeId);
    }

    /**
     * Inicia el streaming de todos los nodos
     */
    public void startAllSensors() {
        sensorNodes.keySet().forEach(this::startSensorNode);
        logger.info("Iniciados todos los sensores");
    }

    /**
     * Detiene el streaming de todos los nodos
     */
    public void stopAllSensors() {
        sensorNodes.keySet().forEach(this::stopSensorNode);
        logger.info("Detenidos todos los sensores");
    }

    /**
     * Genera un valor de temperatura aleatorio
     */
    private BigDecimal getRandomTemperature() {
        return BigDecimal.valueOf(-20 + (Math.random() * 70))
                .setScale(2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Genera un valor de humedad aleatorio
     */
    private BigDecimal getRandomHumidity() {
        return BigDecimal.valueOf(30 + (Math.random() * 65))
                .setScale(2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Crea un nuevo sensor con datos aleatorios para un nodo específico
     */
    private Sensor createSensorData() {
        Sensor sensor = new Sensor();
        LocalDateTime timeStamp = LocalDateTime.now();
        sensor.setTimestamp(timeStamp.format(formatter));
        sensor.setTemperature(getRandomTemperature());
        sensor.setHumidity(getRandomHumidity());
        return sensor;
    }

    /**
     * Publica el sensor en MQTT y lo guarda en la base de datos
     */
    private void publishAndSaveSensorData(Sensor sensor, SensorNode node) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String sensorData = objectMapper.writeValueAsString(sensor);

            // Publicar en el tópico MQTT del nodo
            mqttPublisher.publish(sensorData, node.getMqttTopic(), 1);
            logger.info("Nodo {} publicó en MQTT [{}]: {}",
                    node.getNodeId(), node.getMqttTopic(), sensorData);

            // Guardar en la base de datos
            Sensor savedSensor = sensorRepository.save(sensor);
            logger.info("Nodo {} guardó en BD: {}", node.getNodeId(), savedSensor);

        } catch (JsonProcessingException e) {
            logger.error("Error al serializar datos del nodo {}: {}",
                    node.getNodeId(), e.getMessage(), e);
        } catch (MqttException e) {
            logger.error("Error al publicar MQTT del nodo {}: {}",
                    node.getNodeId(), e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error general del nodo {}: {}",
                    node.getNodeId(), e.getMessage(), e);
        }
    }

    /**
     * Flujo de transmisión continua para un nodo específico
     */
    private void streamSensorValues(String nodeId) {
        SensorNode node = sensorNodes.get(nodeId);
        AtomicBoolean streamingState = streamingStates.get(nodeId);

        logger.info("Iniciando flujo de datos para nodo: {}", nodeId);

        while (streamingState.get()) {
            Sensor sensor = createSensorData();
            publishAndSaveSensorData(sensor, node);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Flujo del nodo {} interrumpido: {}", nodeId, e.getMessage());
                break;
            }
        }

        logger.info("Flujo de datos finalizado para nodo: {}", nodeId);
    }

    /**
     * Obtiene el estado de un nodo (activo/inactivo)
     */
    public boolean isNodeActive(String nodeId) {
        AtomicBoolean state = streamingStates.get(nodeId);
        return state != null && state.get();
    }

    /**
     * Obtiene todos los nodos activos
     */
    public List<SensorNode> getActiveSensorNodes() {
        return sensorNodes.values().stream()
                .filter(SensorNode::isActive)
                .toList();
    }
}