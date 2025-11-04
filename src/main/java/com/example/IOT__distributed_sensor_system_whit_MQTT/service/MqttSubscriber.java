package com.example.IOT__distributed_sensor_system_whit_MQTT.service;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.SubmissionPublisher;

//Implenta de la interfaz MqttCallback para llamar de vuelta
public class MqttSubscriber implements MqttCallback {

    private static final Logger logger = LoggerFactory.getLogger(MqttSubscriber.class);

    //Inyeccion de  depencias
    private final MqttClient mqttClient;

    private final SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

    public MqttSubscriber(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    //Metodo para hacer subcripciones a los  topicos
    public void subscribe(String topic, int qos) throws MqttException {
        mqttClient.subscribe(topic, qos);
        logger.info(" Subcrito a topico: {} con Qos: {} ", topic, qos);
        mqttClient.setCallback(this);
    }


    //metodo que devuelve las conecciones perdidas

    @Override
    public void connectionLost(Throwable cause) {

    }


}
