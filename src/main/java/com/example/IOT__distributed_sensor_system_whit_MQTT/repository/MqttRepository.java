package com.example.IOT__distributed_sensor_system_whit_MQTT.repository;

import com.example.IOT__distributed_sensor_system_whit_MQTT.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MqttRepository extends JpaRepository<Sensor, UUID> {


}
