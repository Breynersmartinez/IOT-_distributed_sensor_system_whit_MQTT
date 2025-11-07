package com.example.IOT__distributed_sensor_system_whit_MQTT.controller;

import com.example.IOT__distributed_sensor_system_whit_MQTT.model.Sensor;
import com.example.IOT__distributed_sensor_system_whit_MQTT.service.SensorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sensor-data")
public class SensorDataController {


    private static final Logger logger = LoggerFactory.getLogger(SensorDataController.class);
    private final SensorService sensorService;

    public SensorDataController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    /**
     * Obtiene todos los registros de sensores
     * GET /sensor-data/all
     */
    @GetMapping("/all")
    public ResponseEntity<List<Sensor>> getAllSensorData() {
        try {
            List<Sensor> sensorData = sensorService.getAllSensorData();
            logger.info("Se recuperaron {} registros de sensores", sensorData.size());
            return ResponseEntity.ok(sensorData);
        } catch (Exception e) {
            logger.error("no se recuperaron {} registros de sensores", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }

    }

/**
 * Obtiene los últimos N registros de sensores
 * GET /sensor-data/latest?limit=10
 */
    @GetMapping("/latest")
    public ResponseEntity<List<Sensor>> getLastestSensorData(
            @RequestParam(defaultValue = "10") int limit){
        try
        {
            if(limit <= 0){
                return ResponseEntity.badRequest().build();
            }
            List<Sensor>  sensorData = sensorService.getLatestSensorData(limit);
            logger.info(" Se recuperaron los  10 ultimos {} registros de sensores ", sensorData.size());
            return ResponseEntity.ok(sensorData);
        }catch (Exception e){
            logger.error(" Error, No se pudieron  recuperaron los  10 ultimos {} registros de sensores ", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }




}


