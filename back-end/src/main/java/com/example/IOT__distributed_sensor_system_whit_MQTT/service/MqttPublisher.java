package com.example.IOT__distributed_sensor_system_whit_MQTT.service;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class MqttPublisher {

    // Logger para registrar eventos de publicacion
    private static final Logger logger = LoggerFactory.getLogger(MqttPublisher.class);

    // Inyeccion del cliente MQTT
    private final MqttClient mqttClient;

    public MqttPublisher(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    // metodo para publicar un mensaje
    public void publish(String messageContent, String topic, int qos) throws MqttException {
       // publica el mensaje con los parametros especificados
        publishMessage(messageContent, topic, qos);
        // log de publicacion exitosa
        logger.info(" Mensaje publicado a tema '{}' : {} ", topic, messageContent);
    }


    //Metodo privado que realiza la publicacion real del mensaje
    private void publishMessage(String messageContent, String topic, int qos) throws MqttException {
        // Crea un mensaje MQTT a partir del contenido de string
        MqttMessage message = new MqttMessage(messageContent.getBytes());
        // Establece el nivel de los Qos para el mensaje
        message.setQos(qos);
        // publcia el mensaje al broker MQTT
        mqttClient.publish(topic, message);

    }


}
