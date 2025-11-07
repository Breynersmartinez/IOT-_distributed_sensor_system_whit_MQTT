package com.example.IOT__distributed_sensor_system_whit_MQTT.service;

import com.example.IOT__distributed_sensor_system_whit_MQTT.model.Sensor;
import com.example.IOT__distributed_sensor_system_whit_MQTT.model.SensorNode;
import com.example.IOT__distributed_sensor_system_whit_MQTT.repository.SensorRepository;
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
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Map para almacenar los nodos sensores y sus estados de streaming
    private final Map<String, SensorNode> sensorNodes = new ConcurrentHashMap<>();
    private final Map<String, AtomicBoolean> streamingStates = new ConcurrentHashMap<>();
    private final Map<String, Thread> sensorThreads = new ConcurrentHashMap<>();

    public MultiSensorService(MqttPublisher mqttPublisher, SensorRepository sensorRepository) {
        this.mqttPublisher = mqttPublisher;
        this.sensorRepository = sensorRepository;
    }

    /**
     * Registra un nuevo nodo sensor
     */
    public void registerSensorNode(SensorNode node) {
        if (node.getNodeId() == null || node.getNodeId().isEmpty()) {
            logger.warn("No se puede registrar un nodo sin ID");
            return;
        }
        sensorNodes.put(node.getNodeId(), node);
        streamingStates.put(node.getNodeId(), new AtomicBoolean(false));
        logger.info("Nodo sensor registrado: {}", node);
    }

    /**
     * Registra múltiples nodos sensores de una sola vez
     */
    public void registerMultipleSensorNodes(List<SensorNode> nodes) {
        nodes.forEach(this::registerSensorNode);
        logger.info("Se registraron {} nodos sensores", nodes.size());
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
