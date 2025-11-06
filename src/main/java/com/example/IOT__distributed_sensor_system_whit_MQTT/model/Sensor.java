package com.example.IOT__distributed_sensor_system_whit_MQTT.model;

/*
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
 */

import com.example.IOT__distributed_sensor_system_whit_MQTT.audit.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "sensor")
public class Sensor extends Auditable<Sensor> {

    // Id del sensor
    @Id
    private UUID uuid;

    // Marca de timpo cuando se captura el dato
    @Column(name = "marca_de_tiempo")
    private String timestamp;

    //valor de temperatura capturando
    @Column (name = "valor")
    private BigDecimal value;

    //Humendad
    @Column (name = "valor")
    private BigDecimal humidity;

    public Sensor() {

    }

    public Sensor(UUID uuid, String timestamp, BigDecimal humidity, BigDecimal value) {
        this.uuid = uuid;
        this.timestamp = timestamp;
        this.humidity = humidity;
        this.value = value;
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

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
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
                ", value=" + value +
                ", humidity=" + humidity +
                '}';
    }
}
