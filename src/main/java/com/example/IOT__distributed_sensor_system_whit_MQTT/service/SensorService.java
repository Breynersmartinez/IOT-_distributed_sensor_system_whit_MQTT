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

    /*
     *  hice uso del AtomicBoolean porque con la variable thread safe porque indica si el streaming esta activo
     * deja controlar el flujo desde multiples hilos de una forma segura
     */
    private final AtomicBoolean isStreaming = new AtomicBoolean(false);

    // formateador de fechas para el timestamp
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // inyeccion de dependencias por constructor, la forma mas adecuada para aplicar la inyeccion
    public SensorService(MqttPublisher mqttPublisher, MqttConfig mqttConfig) {
        this.mqttPublisher = mqttPublisher;
        this.mqttConfig = mqttConfig;
    }

    // Crea una nueva instancia de sennsor con un id
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

    /*
     * Generar un valor de humendad aleatorio
     * Y nos retorna por ende un valor decimal elegido de formal aleatoria  del rango de 30 a 95 %
     */

    private BigDecimal getRamdomHumidity() {
        return BigDecimal.valueOf(30 + (Math.random() * 65));
    }

    //Establece la fecha y hora atual y un valor de temperatura y humedad aleatorio a una instancia del sensor
    private Sensor setSensorTemp(Sensor sensor) {
        LocalDateTime timeStamp = LocalDateTime.now();
        sensor.setTimestamp(timeStamp.format(formatter)); //Formatea y establece la fecha y hora
        sensor.setValue(getRamdomTemperature()); // usa un valor de temperatura aleatorio
        sensor.setValue(getRamdomHumidity());
        return sensor;
    }

    /*
     * genera y publica los datos del sensor de manera constante a traves de MQTT
     * y se ejecuta mientras que isStreaming sea true
     */
    private void streamSensorValues(AtomicBoolean isStreaming) {


        //creacion de instancia del sensor
        Sensor sensor = new Sensor();

        // Se crea un ObjectMapper para serializar el sensor  a un json
        ObjectMapper objectMapper = new ObjectMapper();
        while (isStreaming.get()) {

            // Actualiza la fecha y hora y la temperatura del sensor
            setSensorTemp(sensor);

            try {
                //Serializa el sensor a formato json
                String sensorData = objectMapper.writeValueAsString(sensor);
                // publica los datos al tema MQTT configurado con el Qos configurado
                mqttPublisher.publish(sensorData, mqttConfig.getTopic(), mqttConfig.getQos());
                // log de los datos publicados
                logger.info(" Datos de sensores publicados: {}", sensorData);
            } catch (JsonProcessingException e) {
                // Error al serializar a json
                logger.error("No se pudo serializar los datos del sensor: {}", e.getMessage(), e);
                break;
            } catch (MqttException e) {
                //Error al publicae en MQTT
                logger.error("No se pudo publicar los datos del sensor: {}", e.getMessage(), e);
                break;
            }
        }

        //USO DE HILOS
        try {
            Thread.sleep(1000); // que duerma durante 1000 milisegundo
        } catch (InterruptedException e) {
            // aqui se restaura el estado de interrupcion del hilo
            Thread.currentThread().interrupt();
            logger.warn(" Flujo interrumpido {}", e.getMessage());
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


}
