package com.example.IOT__distributed_sensor_system_whit_MQTT.controller;

import com.example.IOT__distributed_sensor_system_whit_MQTT.service.SensorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sensor")
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    // endPoint palanca
    @PostMapping("/{toggle}")
    public ResponseEntity<String> strartStreaming(@PathVariable String toggle)
    {
        if(toggle.equals("start")){
            sensorService.startStreaming();
            return ResponseEntity.ok(" Se inicio la transmision de datos del sensor ");
        }else if (toggle.equals("stop") ){
           sensorService.stopStreaming();
           return ResponseEntity.ok(" Se paro la transmisioon de datos del sensor ");
        } else {
            return ResponseEntity.badRequest().body(" Palanca de parametros invalida. Use 'start o 'stop ");
        }
    }

}
