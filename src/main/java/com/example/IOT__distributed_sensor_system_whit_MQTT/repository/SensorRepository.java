package com.example.IOT__distributed_sensor_system_whit_MQTT.repository;

import com.example.IOT__distributed_sensor_system_whit_MQTT.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, UUID> {

    /*
     * Obtien todos los registros de sensores
     */

    public List<Sensor> findAllByOrderByTimestampDesc();


    /*
     * Obtiene los ultimos n registros de sensores
     */

    List<Sensor> findAllByOrderByTimestampDesc(Pageable pageable);

}
