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

    private static final Logger logger = LoggerFactory.getLogger(MqttPublisher.class);

    private final MqttClient mqttClient;

    public MqttPublisher(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    // metodo para publicar
    public void publish(String messageContent, String topic, int qos) throws MqttException {
        publishMessage(messageContent, topic, qos);
        logger.info(" Mensaje publicado a tema '{}' : {} ", topic, messageContent);
    }

    //Metodo para publicar mensaje
    private void publishMessage(String messageContent, String topic, int qos) throws MqttException {
        MqttMessage message = new MqttMessage(messageContent.getBytes());
        message.setQos(qos);
        mqttClient.publish(topic, message);

    }

}
