package com.example.IOT__distributed_sensor_system_whit_MQTT.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.Flow;
import java.util.function.Consumer;

/*
 * MqttMessageSuscriber impelementa la interfaz Flow.Subscriber para  procesar
 * mensaje en un flujo reactivo. Forma parte del patron Reactive Streams
 */
public class MqttMessageSuscriber implements Flow.Subscriber<String> {

    // Logger para registrar eventos
    private static final Logger logger = LoggerFactory.getLogger(MqttMessageSuscriber.class);
    //Suscripcion al flujo y permite controlar las solicitudes
    private Flow.Subscription subscription;

    //Consumidor que procsa cada mensaje recibido
    private final java.util.function.Consumer<String> messageConsumer;
    public MqttMessageSuscriber(Consumer<String> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    //Se ivoca ciando se establece la suscripcion al flujo
    // Inicialisa la suscripcion y solicita el primer elemento
    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        //Solicita 1 elemento del flujo
        subscription.request(1);
        logger.info(" Suscrito al flujo de mensajes. ");
    }

    // Se invoca cuando llega un nuevo elemento que es el mensaje del flujo
    @Override
    public void onNext(String item) {
        logger.info(" Mensaje recibido: {}", item);
        // Procesa el mensaje con el consumidor
        messageConsumer.accept(item);
        //Solicita el siguiente elemento
        subscription.request(1);
    }

    // Se invoca cuando ocurre un error  en el flujo
    @Override
    public void onError(Throwable throwable) {
        logger.error(" Error ocurrido: {}", throwable.getMessage(), throwable);
    }

    // Se invoca cuando el flujo se completa correctamente
    @Override
    public void onComplete() {
        logger.info(" Flujo de mensages completo. ");
    }

}
