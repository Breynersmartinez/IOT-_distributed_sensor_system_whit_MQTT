package com.example.IOT__distributed_sensor_system_whit_MQTT.model;



import com.example.IOT__distributed_sensor_system_whit_MQTT.audit.Auditable;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "sensor_data")
public class Sensor extends Auditable<Sensor> {

    // Id del sensor
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private UUID uuid;

    // Marca de timpo cuando se captura el dato
    @Column(name = "marca_de_tiempo")
    private String timestamp;

    //valor de temperatura capturando
    @Column (name = "temperatura")
    private BigDecimal temperature;

    //Humendad
    @Column (name = "humedad")
    private BigDecimal humidity;

    public Sensor() {

    }

    public Sensor(UUID uuid, String timestamp, BigDecimal temperature, BigDecimal humidity) {
        this.uuid = uuid;
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    // getters and setters
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public BigDecimal getHumidity() {
        return humidity;
    }

    public void setHumidity(BigDecimal humidity) {
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "uuid=" + uuid +
                ", timestamp='" + timestamp + '\'' +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                '}';
    }
}
