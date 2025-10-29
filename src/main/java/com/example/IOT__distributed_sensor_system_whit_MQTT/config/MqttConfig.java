package com.example.IOT__distributed_sensor_system_whit_MQTT.config;

import lombok.Getter;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    // URL DEL BROKER
    @Value("${mqtt.broker.url}")
    private String brokerUrl;

    //
    @Getter
    @Value("${mqtt.topic}")
    private String topic;

    // ID DE CLIENTE
    @Value("${mqtt.client.id}")
    private String clientId;


    @Getter
    @Value("${mqtt.qos}")
    private int qos;

    @Bean
    public MqttClient mqttClient() throws Exception {
        MqttClient mqttClient = new MqttClient(brokerUrl, clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        mqttClient.connect(options);
        return mqttClient;
    }

}
