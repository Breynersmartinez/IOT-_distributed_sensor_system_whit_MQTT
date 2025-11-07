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

    public SensorNode() {
    }

    public SensorNode(String nodeId, String nodeName, String location, String mqttTopic) {
        this.nodeId = nodeId;
        this.nodeName = nodeName;
        this.location = location;
        this.mqttTopic = mqttTopic;
        this.active = true;
    }

    // Getters y Setters
    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMqttTopic() {
        return mqttTopic;
    }

    public void setMqttTopic(String mqttTopic) {
        this.mqttTopic = mqttTopic;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "SensorNode{" +
                "nodeId='" + nodeId + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", location='" + location + '\'' +
                ", mqttTopic='" + mqttTopic + '\'' +
                ", active=" + active +
                '}';
    }
}

