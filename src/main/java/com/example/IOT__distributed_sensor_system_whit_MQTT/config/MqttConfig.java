package com.example.IOT__distributed_sensor_system_whit_MQTT.config;

import lombok.Getter;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    // URL del broker MQTT (obtenida desde application.properties)
    @Value("${mqtt.broker.url}")
    private String brokerUrl;

    // Tema MQTT por defecto para publicar/suscribirse
    @Getter
    @Value("${mqtt.topic}")
    private String topic;

    // ID DE CLIENTE MQTT
    @Value("${mqtt.client.id}")
    private String clientId;


    /*
        IMPORTANTE:
        Qos En Mqtt define el nivel de garantia para que un mensaje sea entregado por un editor a un suscriptor,
        es mas que todo un conjunto de reglas qie se basan en la conexion TCP.
 */
    @Getter
    @Value("${mqtt.qos}")
    private int qos;

    @Bean
    public MqttClient mqttClient() throws Exception // Por si hay errores de conexion
    {
        // Intancia un nuevo cliente MQTT con la url del broker y el di del cliete
        MqttClient mqttClient = new MqttClient(brokerUrl, clientId);

        // Crea nuevas opciones de conexion
        MqttConnectOptions options = new MqttConnectOptions();

        /*
        *  setCleanSession si es true: Borra sesiones previas al conectar
        *  Permite empezar con una sesión limpia sin mensajes pendientes
         */
        options.setCleanSession(true);
        mqttClient.connect(options); // Conecta el cliente al broker
        return mqttClient; //Retorna al cliente ya xonfigurado y conectado al broker
    }

}
