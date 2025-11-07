package com.example.IOT__distributed_sensor_system_whit_MQTT.repository;

import com.example.IOT__distributed_sensor_system_whit_MQTT.model.SensorNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorNodeRepository extends JpaRepository<SensorNode, String> {

    /**
     * Obtiene todos los nodos que están activos
     */
    List<SensorNode> findByActive(Boolean active);
}
