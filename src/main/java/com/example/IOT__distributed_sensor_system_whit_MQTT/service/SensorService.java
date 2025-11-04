package com.example.IOT__distributed_sensor_system_whit_MQTT.service;

import com.example.IOT__distributed_sensor_system_whit_MQTT.config.MqttConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class SensorService {

    private static final Logger logger = LoggerFactory.getLogger(SensorService.class);

    // Inyeccion de dependencias
    private final MqttPublisher mqttPublisher;

    private final MqttConfig mqttConfig;


    private final final Automatic

}
