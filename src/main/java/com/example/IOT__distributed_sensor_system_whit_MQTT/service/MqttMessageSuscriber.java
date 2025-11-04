package com.example.IOT__distributed_sensor_system_whit_MQTT.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Flow;
import java.util.function.Consumer;

public class MqttMessageSuscriber implements Flow.Subscriber<String>{

    private static final Logger logger = LoggerFactory.getLogger(MqttMessageSuscriber.class);

    private Flow.Subscription subscription;

    private final java.util.function.Consumer<String> messageConsumer;

    public MqttMessageSuscriber(Consumer<String> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    @Override
    public void  onSubscribe(Flow.Subscription subscription)
    {
        this.subscription = subscription;
        subscription.request(1);
        logger.info(" Suscrito al flujo de mensajes. ");
    }

    @Override
    public void onNext(String item){
        logger.info(" Mensaje recibido: {}", item);
        messageConsumer.accept(item);
        subscription.request(1);
    }

    @Override
    public void    onError(Throwable throwable)
    {
        logger.error(" Error ocurrido: {}", throwable.getMessage(), throwable);
    }

    @Override
    public void onComplete()
    {
        logger.info(" Flujo de mensages completo. ");
    }

}
