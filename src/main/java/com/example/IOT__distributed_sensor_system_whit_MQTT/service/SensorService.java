package com.example.IOT__distributed_sensor_system_whit_MQTT.service;

import com.example.IOT__distributed_sensor_system_whit_MQTT.config.MqttConfig;
import com.example.IOT__distributed_sensor_system_whit_MQTT.model.Sensor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.UUID.randomUUID;


@Service
public class SensorService {

    private static final Logger logger = LoggerFactory.getLogger(SensorService.class);

    // Inyeccion de dependencias
    private final MqttPublisher mqttPublisher;

    private final MqttConfig mqttConfig;


    private final AtomicBoolean isStreaming = new AtomicBoolean(false);

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public SensorService(MqttPublisher mqttPublisher, MqttConfig mqttConfig) {
        this.mqttPublisher = mqttPublisher;
        this.mqttConfig = mqttConfig;
    }

    private Sensor create() {
        Sensor sensor = new Sensor();
        sensor.setUuid(randomUUID());
        return sensor;
    }


    /*
     * Genera un  valor de temperatura aleatorio
     * Retorna un valor  BigDecimal seleccionado aleatoriamente  del rango -20.0 a 50.0.
     */

    private BigDecimal getRamdomTemperature() {
        return BigDecimal.valueOf(-20 + (Math.random() * 70));
    }

    //Metodo para modificar la temperatura del sensor
    private Sensor setSensorTemp(Sensor sensor) {
        LocalDateTime timeStamp = LocalDateTime.now();
        sensor.setTimestamp(timeStamp.format(formatter));
        sensor.setValue(getRamdomTemperature());
        return sensor;
    }

    private void streamSensorValues(AtomicBoolean isStreaming) {
        Sensor sensor = new Sensor();
        ObjectMapper objectMapper = new ObjectMapper();
        while (isStreaming.get()) {
            setSensorTemp(sensor);

            try {
                String sensorData = objectMapper.writeValueAsString(sensor);
                mqttPublisher.publish(sensorData, mqttConfig.getTopic(), mqttConfig.getQos());
                logger.info(" Datos de sensores publicados: {}", sensorData);
            } catch (JsonProcessingException e) {
                logger.error("No se pudo serializar los datos del sensor: {}", e.getMessage(), e);
                break;
            } catch (MqttException e) {
            logger.error("No se pudo publicar los datos del sensor: {}", e.getMessage(), e);
                break;
            }
        }

        //USO DE HILOS
        try
        {
            Thread.sleep(1000); // que duerma durante 1000 milisegundo
        } catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            logger.warn( " Flujo interrumpido {}", e.getMessage());
        }

    }


    public void startStreaming(){
        if(!isStreaming.get()){
            isStreaming.set(true);
            Thread streamingThread = new Thread(() -> streamSensorValues(isStreaming));
            streamingThread.start();
            logger.info(" Se inicio la transmision de datos del sensor. ");
        } else {
            logger.warn( " La transmision de datos del sensor ya esta en funcionamiento ");
        }
    }

    public void stopStreaming(){
        if(isStreaming.get()){
            isStreaming.set(false);
            logger.info(" Detencion de la transmision del sensor ");
        }else{
            logger.warn(" LA transimision de datos de los sensores no esta funcionando  ");
        }
    }


}
