package com.example.IOT__distributed_sensor_system_whit_MQTT.model;

/*
* Represrenta un nodo sensor con su identificador y configuracion
 */

public class SensorNode {

    private String nodeId; //id del nodo. Ejemplo de uso: "SENSOR_01"
    private String nodeName;    // Nombre descriptivo del nodo. Ejemplo: "Temperatura_salon"
    private String location; // ubicacion del sensor. Ejemplo: " piso 3, salon 302"
    private String mqttTopic; // tema o topico asignado. Ejemplo "sensors/salon/temp-01"
    private Boolean active; // esta activo o no?
}



