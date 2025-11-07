package com.example.IOT__distributed_sensor_system_whit_MQTT.service;

import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

/*
* El MqttSubscriber es el servicio que implementa MqttCallback para manejar callbacks de MQTT
* Gestiona suscripciones a temas y publica los mensajes recibidos
* usando Reactive Streams con Flow API
 */
@Component
public class MqttSubscriber implements MqttCallback {

    //Loger para registrar los eventos
    private static final Logger logger = LoggerFactory.getLogger(MqttSubscriber.class);

    //Inyeccion del cliente MQTT
    private final MqttClient mqttClient;

    /*
    * Bueno, lo que permite el Publisher reactivo que emite mensajes a los suscriptores
    * y SubmissionPublisher implementa la interfaz Flow.Publisher
     */
    private final SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

    public MqttSubscriber(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    /*
            IMPORTANTE:
            Qos En Mqtt define el nivel de garantia para que un mensaje sea entregado por un editor a un suscriptor,
            es mas que todo un conjunto de reglas qie se basan en la conexion TCP.
     */

    //se suscribe  a un tema MQTT  especifico
    public void subscribe(String topic, int qos) throws MqttException {
        // Se suscribe al tema Qos especificado
        mqttClient.subscribe(topic, qos);
        // Log de suscripcion
        logger.info(" Suscrito al tema : {} con Qos: {} ", topic, qos);
        // Establece este objeto como callback para eventos MQTT
        mqttClient.setCallback(this);
    }


/*
* Callback invocado cuando se pierde la conexion con el broker MQTT
*
 */
    @Override
    public void connectionLost(Throwable cause) {
        logger.warn(" conexion perdida: {}", cause.getMessage());
    }

    /*
    * Callback invocado cuando llega un mensaje a un tema suscrito
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        // Convierte el payload del mensaje a String
        String receivedMessage = new String(message.getPayload());
        logger.info(" Mensaje reibido por el tema {} : {}", topic, message);
        // publica el mensaje a todos los suscriptores del flujo reactivo
        publisher.submit(receivedMessage);
    }

    /*
    * Callback invocado cuando se completa la entrega de un mensaje
    *
    * y el parametro token de entrega del mensaje
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }


    // permite que otros componentes se suscriban al flujo de mensaje MQTT
    public void subscribeToMessages(Flow.Subscriber<String> subscriber) {
        publisher.subscribe(subscriber);
    }

}
