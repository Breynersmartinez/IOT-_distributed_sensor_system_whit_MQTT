package com.example.IOT__distributed_sensor_system_whit_MQTT.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de CORS para permitir que el dashboard React
 * acceda a los endpoints de la API
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // En desarrollo: permitir localhost:3000
                // En producción: cambiar a tu dominio
                .allowedOrigins("http://localhost:3000", "http://localhost:3001", "http://localhost:5173/", "https://iot-distributed-sensor-system-whit.vercel.app")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
