package com.example.IOT__distributed_sensor_system_whit_MQTT.service;

import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Flow;
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

    /*
            IMPORTANTE:
            Qos En Mqtt define el nivel de garantia para que un mensaje sea entregado por un editor a un suscriptor,
            es mas que todo un conjunto de reglas qie se basan en la conexion TCP.
     */

    //Metodo para hacer subcripciones a los  topicos
    public void subscribe(String topic, int qos) throws MqttException {
        mqttClient.subscribe(topic, qos);
        logger.info(" Suscrito al tema : {} con Qos: {} ", topic, qos);
        mqttClient.setCallback(this);
    }


    //metodo que devuelve las conecciones perdidas

    @Override
    public void connectionLost(Throwable cause) {
        logger.warn(" conexion perdida: {}", cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String receivedMessage = new String(message.getPayload());
        logger.info(" Mensaje reibido por el tema {} : {}", topic, message);
        publisher.submit(receivedMessage);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }


    public void subscribeToMessages(Flow.Subscriber<String> subscriber) {
        publisher.subscribe(subscriber);
    }

}
