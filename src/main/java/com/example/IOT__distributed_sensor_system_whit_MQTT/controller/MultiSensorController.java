package com.example.IOT__distributed_sensor_system_whit_MQTT.controller;

import com.example.IOT__distributed_sensor_system_whit_MQTT.model.SensorNode;
import com.example.IOT__distributed_sensor_system_whit_MQTT.service.MultiSensorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/multi-sensor")
public class MultiSensorController {

    private static final Logger logger = LoggerFactory.getLogger(MultiSensorController.class);
    private final MultiSensorService multiSensorService;

    public MultiSensorController(MultiSensorService multiSensorService) {
        this.multiSensorService = multiSensorService;
    }

    /**
     * Registra un nuevo nodo sensor

     */
    @PostMapping("/register")
    public ResponseEntity<String> registerSensorNode(@RequestBody SensorNode node) {
        try {
            multiSensorService.registerSensorNode(node);
            logger.info("Nodo sensor registrado: {}", node.getNodeId());
            return ResponseEntity.ok("Nodo sensor registrado exitosamente: " + node.getNodeId());
        } catch (Exception e) {
            logger.error("Error al registrar nodo sensor: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error al registrar nodo: " + e.getMessage());
        }
    }

    /**
     * Registra múltiples nodos sensores
     */
    @PostMapping("/register-multiple")
    public ResponseEntity<String> registerMultipleSensorNodes(@RequestBody List<SensorNode> nodes) {
        try {
            multiSensorService.registerMultipleSensorNodes(nodes);
            logger.info("Se registraron {} nodos sensores", nodes.size());
            return ResponseEntity.ok("Se registraron " + nodes.size() + " nodos sensores exitosamente");
        } catch (Exception e) {
            logger.error("Error al registrar múltiples nodos: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error al registrar nodos: " + e.getMessage());
        }
    }

    /**
     * Obtiene todos los nodos registrados
     */
    @GetMapping("/nodes")
    public ResponseEntity<List<SensorNode>> getAllSensorNodes() {
        try {
            List<SensorNode> nodes = multiSensorService.getAllSensorNodes();
            logger.info("Se recuperaron {} nodos sensores", nodes.size());
            return ResponseEntity.ok(nodes);
        } catch (Exception e) {
            logger.error("Error al obtener nodos sensores: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Obtiene un nodo específico

     */
    @GetMapping("/nodes/{nodeId}")
    public ResponseEntity<SensorNode> getSensorNode(@PathVariable String nodeId) {
        try {
            SensorNode node = multiSensorService.getSensorNode(nodeId);
            if (node == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(node);
        } catch (Exception e) {
            logger.error("Error al obtener nodo {}: {}", nodeId, e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Inicia el streaming de un nodo específico
     */
    @PostMapping("/start/{nodeId}")
    public ResponseEntity<String> startSensorNode(@PathVariable String nodeId) {
        try {
            multiSensorService.startSensorNode(nodeId);
            return ResponseEntity.ok("Streaming iniciado para nodo: " + nodeId);
        } catch (Exception e) {
            logger.error("Error al iniciar nodo {}: {}", nodeId, e.getMessage(), e);
            return ResponseEntity.status(500).body("Error al iniciar nodo: " + e.getMessage());
        }
    }

    /**
     * Detiene el streaming de un nodo específico

     */
    @PostMapping("/stop/{nodeId}")
    public ResponseEntity<String> stopSensorNode(@PathVariable String nodeId) {
        try {
            multiSensorService.stopSensorNode(nodeId);
            return ResponseEntity.ok("Streaming detenido para nodo: " + nodeId);
        } catch (Exception e) {
            logger.error("Error al detener nodo {}: {}", nodeId, e.getMessage(), e);
            return ResponseEntity.status(500).body("Error al detener nodo: " + e.getMessage());
        }
    }

    /**
     * Inicia el streaming de TODOS los nodos
     */
    @PostMapping("/start-all")
    public ResponseEntity<String> startAllSensors() {
        try {
            multiSensorService.startAllSensors();
            return ResponseEntity.ok("Streaming iniciado para todos los nodos");
        } catch (Exception e) {
            logger.error("Error al iniciar todos los nodos: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error al iniciar nodos: " + e.getMessage());
        }
    }

    /**
     * Detiene el streaming de TODOS los nodos
     */
    @PostMapping("/stop-all")
    public ResponseEntity<String> stopAllSensors() {
        try {
            multiSensorService.stopAllSensors();
            return ResponseEntity.ok("Streaming detenido para todos los nodos");
        } catch (Exception e) {
            logger.error("Error al detener todos los nodos: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error al detener nodos: " + e.getMessage());
        }
    }

    /**
     * Obtiene los nodos activos
     */
    @GetMapping("/active")
    public ResponseEntity<List<SensorNode>> getActiveSensorNodes() {
        try {
            List<SensorNode> activeNodes = multiSensorService.getActiveSensorNodes();
            return ResponseEntity.ok(activeNodes);
        } catch (Exception e) {
            logger.error("Error al obtener nodos activos: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Verifica si un nodo está activo

     */
    @GetMapping("/active/{nodeId}")
    public ResponseEntity<Boolean> isNodeActive(@PathVariable String nodeId) {
        try {
            boolean active = multiSensorService.isNodeActive(nodeId);
            return ResponseEntity.ok(active);
        } catch (Exception e) {
            logger.error("Error al verificar estado del nodo {}: {}", nodeId, e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }
}