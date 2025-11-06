package com.example.IOT__distributed_sensor_system_whit_MQTT.controller;

import com.example.IOT__distributed_sensor_system_whit_MQTT.service.SensorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * Controlador REST  para gestionar el streaming de datos del sensor
 * Permite iniciar y detener la transmision de datos
 */
@RestController
@RequestMapping("/sensor")
public class SensorController {

    // Inyeccion de dependencias del servicio de sensor
    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    // endPoint palanca para iniciar o detener la transmision de datos del sensor
    // Como parametros esta start para iniciar y stop para detener
    @PostMapping("/{toggle}")
    public ResponseEntity<String> strartStreaming(@PathVariable String toggle) // ResponseEntity con mensaje de extio o error
    {

        if (toggle.equals("start"))   // si el para metro es start, se inicia la transmision
        {
            sensorService.startStreaming();
            return ResponseEntity.ok(" Se inicio la transmision de datos del sensor ");

        } else if (toggle.equals("stop"))    // si el para metro es stop, se detien  la transmision
        {
            sensorService.stopStreaming();
            return ResponseEntity.ok(" Se paro la transmisioon de datos del sensor ");

            // si el parametro no es valido, arroja un error
        } else {
            return ResponseEntity.badRequest().body(" Palanca de parametros invalida. Use 'start o 'stop ");
        }
    }

}
