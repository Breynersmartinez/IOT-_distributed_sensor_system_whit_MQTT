package com.example.IOT__distributed_sensor_system_whit_MQTT.model;

/*
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
 */
import org.springframework.data.annotation.Id;
import java.math.BigDecimal;
import java.util.UUID;

//@Table(name = "sensor")
public class Sensor {

    @Id
    private UUID uuid;

   // @Column(name = "marca_de_tiempo")
    private String timestamp;

    //@Column (name = "valor")
    private BigDecimal value;

}
