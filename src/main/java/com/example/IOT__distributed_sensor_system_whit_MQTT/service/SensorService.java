package com.example.IOT__distributed_sensor_system_whit_MQTT.service;

import com.example.IOT__distributed_sensor_system_whit_MQTT.config.MqttConfig;
import com.example.IOT__distributed_sensor_system_whit_MQTT.model.Sensor;
import com.example.IOT__distributed_sensor_system_whit_MQTT.repository.SensorRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.UUID.randomUUID;


@Service
public class SensorService {

    private static final Logger logger = LoggerFactory.getLogger(SensorService.class);
    // Inyeccion de dependencias
    private final MqttPublisher mqttPublisher;
    private final MqttConfig mqttConfig;
    private final SensorRepository sensorRepository;

    /*
     *  hice uso del AtomicBoolean porque con la variable thread safe porque indica si el streaming esta activo
     * deja controlar el flujo desde multiples hilos de una forma segura
     */
    private final AtomicBoolean isStreaming = new AtomicBoolean(false);

    // formateador de fechas para el timestamp
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // inyeccion de dependencias por constructor, la forma mas adecuada para aplicar la inyeccion
    public SensorService(MqttPublisher mqttPublisher, MqttConfig mqttConfig, SensorRepository sensorRepository) {
        this.mqttPublisher = mqttPublisher;
        this.mqttConfig = mqttConfig;
        this.sensorRepository = sensorRepository;
    }


    /*
     * Genera un  valor de temperatura aleatorio
     * Retorna un valor  BigDecimal seleccionado aleatoriamente  del rango -20.0 a 50.0.
     */

    private BigDecimal getRandomTemperature() {
        return BigDecimal.valueOf(-20 + (Math.random() * 70));
    }

    /*
     * Generar un valor de humendad aleatorio
     * Y nos retorna por ende un valor decimal elegido de formal aleatoria  del rango de 30 a 95 %
     */

    private BigDecimal getRandomHumidity() {
        return BigDecimal.valueOf(30 + (Math.random() * 65));
    }

    //Establece la fecha y hora atual y un valor de temperatura y humedad aleatorio a una instancia del sensor
    private Sensor createSensorData() {
        Sensor sensor = new Sensor();
        LocalDateTime timeStamp = LocalDateTime.now();
        sensor.setTimestamp(timeStamp.format(formatter)); //Formatea y establece la fecha y hora
        sensor.setTemperature(getRandomTemperature()); // usa un valor de temperatura aleatorio
        sensor.setHumidity(getRandomHumidity()); // usa un valor de humedad aleatorio
        return sensor;
    }

    /**
     * Publica el sensor en MQTT y lo guarda en la base de datos
     */
    private void publishAndSavedSensorData(Sensor sensor) {

        // Se crea un ObjectMapper para serializar el sensor  a un json
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            //Serializa el sensor a formato json
            String sensorData = objectMapper.writeValueAsString(sensor);
            // publica los datos al tema MQTT configurado con el Qos configurado
            mqttPublisher.publish(sensorData, mqttConfig.getTopic(), mqttConfig.getQos());
            // log de los datos publicados
            logger.info(" Datos de sensores publicados: {}", sensorData);

            // Guardar en la base de datos
            Sensor savedSensor = sensorRepository.save(sensor);
            logger.info(" Datos de sensores guardados en la base de datos: {}", savedSensor);

        } catch (JsonProcessingException e) {
            // Error al serializar a json
            logger.error("No se pudo serializar los datos del sensor: {}", e.getMessage(), e);

        } catch (MqttException e) {
            //Error al publicae en MQTT
            logger.error("No se pudo publicar los datos del sensor: {}", e.getMessage(), e);

        } catch (Exception e) {
            logger.error(" Error, No se pudieron guardar los datos del sensor: {}", e.getMessage(), e);

        }
    }

    /**
     * Flujo de transmision continua de datos del sensor
     */
    private void streamSensorValues(AtomicBoolean isStreaming) {

        //creacion de instancia del sensor
        //Sensor sensor = new Sensor();
        while (isStreaming.get()) {
            Sensor sensor = createSensorData();
            publishAndSavedSensorData(sensor);

            //USO DE HILOS
            try {
                Thread.sleep(1000); // que duerma durante 1000 milisegundo
            } catch (InterruptedException e) {
                // aqui se restaura el estado de interrupcion del hilo
                Thread.currentThread().interrupt();
                logger.warn(" Flujo interrumpido {}", e.getMessage());
            }
        }

    }


    /*
     * Inicia el streaming de datos del sensor en un hilo separado
     * y si el streaming ya esta activo, no se hace nada
     */
    public void startStreaming() {
        // Verifica si el streamig no esta activo
        if (!isStreaming.get()) {
            //marca que el streamig esta activo
            isStreaming.set(true);
            // Crea un nuevo hilo para ejecutar el streaming
            Thread streamingThread = new Thread(() -> streamSensorValues(isStreaming));
            // Inicia el hilo
            streamingThread.start();
            logger.info(" Se inicio la transmision de datos del sensor. ");
        } else {
            logger.warn(" La transmision de datos del sensor ya esta en funcionamiento ");
        }
    }

    /*
     * Para el streaming de datos del sensor
     * pone  la bandera isStreaming en false para que pare el bucle
     */
    public void stopStreaming() {
        // Verifica si el streaming esta actvio
        if (isStreaming.get()) {
            // Marca qu el streaming debe detenerse
            isStreaming.set(false);
            logger.info(" Detencion de la transmision del sensor ");
        } else {
            logger.warn(" LA transimision de datos de los sensores no esta funcionando  ");
        }
    }


    /*
    * Obtien todos los registros de sensores
     */

    public List<Sensor> getAllSensorData(){
        return sensorRepository.findAllByOrderByTimestampDesc();
    }

    /*
    * Obtiene los ultimos n registros de sensores
     */
    public List<Sensor> getLatestSensorData(int limit) {
        return sensorRepository.findAllByOrderByTimestampDesc(
                PageRequest.of(0, limit)
        );
    }
}
