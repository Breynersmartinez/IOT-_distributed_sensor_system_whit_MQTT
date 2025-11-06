package com.example.IOT__distributed_sensor_system_whit_MQTT.controller;


import com.example.IOT__distributed_sensor_system_whit_MQTT.service.MqttMessageSuscriber;
import com.example.IOT__distributed_sensor_system_whit_MQTT.service.MqttPublisher;
import com.example.IOT__distributed_sensor_system_whit_MQTT.service.MqttSubscriber;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
@RequestMapping("/mqtt") // EndPoint par hacer peticiones  protocolo  Mqtt
public class MqttController {

    private static final Logger logger = LoggerFactory.getLogger(MqttController.class);


    // Inyeccion de dependencias

    private final MqttSubscriber mqttSubscriber;

    private final MqttPublisher mqttPublisher;

    private final MqttClient mqttClient;

    public MqttController(MqttSubscriber mqttSubscriber, MqttPublisher mqttPublisher, MqttClient mqttClient) {
        this.mqttSubscriber = mqttSubscriber;
        this.mqttPublisher = mqttPublisher;
        this.mqttClient = mqttClient;
    }


    /*
     * Enpoint Rest para publicar mensajes en un broker MQTT.
     *
     * Este metodo recibe los parametros necesarios para publicar un mensaje
     * en un tema MQTT especifico con un nivel de calidad de servicio Qos
     * configurable. El mensaje se envia de forma asicronica al broker
     */
    @PostMapping("/message")
    public ResponseEntity<String> publishMessage(
            //Parametros de entrada
            @RequestParam String message, // Contenido del mensaje a publicar
            @RequestParam String topic,  // Tema MQTT destino donde se publicara el mensaje
            @RequestParam(defaultValue = "1") int qos)   // Nivel Qos 0, 1 o 2 por defecto 1
    {
        try {
            //Publicar el mensaje en el breoker MQTT
            mqttPublisher.publish(message, topic, qos);
            // Retrona respuesta exitosa
            return ResponseEntity.ok("Mensaje de publicacion a tema '" + topic + "' : " + message);
        } catch (Exception e) {
            // Registrar el error y retornar respuesta de error
            logger.error(" Error en la publicacion del mensaje: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(" Error en la publicacion del mensaje: " + e.getMessage());

        }

    }

    /**
     *
     * Endpoint para suscribirse a un tema MQTT y recibir mensajes en tiempo real
     * Retorna un flujo de Server-Sent Events o el SSE
     *
     * Parametros:
     *  topic: Tema MQTT a suscribirse
     *  qos: Nivel de calidad de servicio
     *
     *  retorna Flux con los mensajes recibidos en tiempo real
     */
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Object> streamMessages(@RequestParam String topic, @RequestParam int qos) {
        try {
            // Se suscribe al tema MQTT  con el Qos especificado
            mqttSubscriber.subscribe(topic, qos);
        } catch (Exception e) {
            //Log de error si falla la suscripcion
            logger.error(" Suscripcion fallida al tema {} with Qos {} : {}", topic, qos, e.getMessage());
            // Retorna un flujo con error
            return Flux.error(new RuntimeException(" Suscripcion fallida: " + e.getMessage()));
        }
        // Crea un flujo reactivo que emite los mensajes recibidos
        return Flux.create(sink -> {
            try {
                // Se suscribe al servicio de mensajes MQTT
                mqttSubscriber.subscribeToMessages(new MqttMessageSuscriber(sink::next));
            } catch (Exception e) {
                logger.error(" Error al suscribirse a los mensajes del tema {} : {} ", topic, e.getMessage());
                // Completa el flujo con error
                sink.error(new RuntimeException(" Error de suscripcion a mensajes " + e.getMessage()));
            }
        // Se agrega un daley de 100 milisegundos entre las emisiones para evitar sobre carga
        }).delayElements(Duration.ofMillis(100));

    }

    //     Endpoint para desconectar el cliente MQTT del broker
    @PostMapping("/disconnect")
    public ResponseEntity<String> disconnect() {
        try {
            // Desconecta el cliente del broker MQTT
            mqttClient.disconnect();
            // Log de desconexion exitosa
            logger.info(" El cliente MQTT se desconecto correctamente ");
            return ResponseEntity.ok(" Cliente MQTT desconectado ");
        } catch (MqttException e) {
            // Error de error de desconexion
            logger.error("Error al desconectar el cliente MQTT: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("No se pudo desconectar el cliente MQTT");
        }
    }

    /*
     * Endpoint para reconectar el cliente MQTT al broker.
     * Verifica si el cliente está inicializado y conectado antes de reconectar.
     */
    @PostMapping("/reconnect")
    public ResponseEntity<String> reconnect() {
        try {
            // Verifica si el cliente MQTT  esta inicializado
            if (mqttClient == null) {
                logger.error(" El cliente MQTT no está inicializado ");
                return ResponseEntity.status(500).body(" El cliente MQTT no está inicializado ");
            }
            // si no esta conectado, intenta conectarse
            if (!mqttClient.isConnected()) {
                mqttClient.connect();
            }
            //Loger del estado de la conexion
            logger.info(" El cliente MQTT esta conectado: {}", mqttClient.isConnected());
            // Verifica si la conexion fue exitosa
            if (mqttClient.isConnected()) {
                logger.info(" El cliente MQTT se volvió a conectar correctamente ");
                return ResponseEntity.ok(" El cliente MQTT esta reconectado");
            } else {
                logger.error(" No se pudo volver a conectar el cliente MQTT ");
                return ResponseEntity.status(500).body("  No se pudo volver a conectar el cliente MQTT ");
            }

        } catch (MqttException e) {
            logger.error("Error al volver a conectar con el broker MQTT: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error al volver a conectar el cliente MQTT: " + e.getMessage());
        }


    }


}
