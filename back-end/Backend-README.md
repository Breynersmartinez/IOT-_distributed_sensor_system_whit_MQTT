# Backend - IoT Distributed Sensor System with MQTT

[![Java](https://img.shields.io/badge/Java-17+-orange?style=flat-square)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green?style=flat-square)](https://spring.io/projects/spring-boot)
[![MQTT](https://img.shields.io/badge/MQTT-3.1.1-red?style=flat-square)](https://mqtt.org/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue?style=flat-square)](https://maven.apache.org/)

Servidor backend robusto y escalable desarrollado con Spring Boot que implementa un sistema completo de sensores IoT distribuidos con comunicación MQTT, persistencia de datos y API REST.

---

## Tabla de Contenidos

- [Características](#características)
- [Requisitos Previos](#requisitos-previos)
- [Instalación Rápida](#instalación-rápida)
- [Configuración](#configuración)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [API Endpoints](#api-endpoints)
- [Arquitectura](#arquitectura)
- [Conceptos Clave](#conceptos-clave)
- [Ejemplos de Uso](#ejemplos-de-uso)
- [Troubleshooting](#troubleshooting)

---

## Características

### Sensores Distribuidos
- Registro y gestión de múltiples nodos sensores
- Transmisión independiente de cada nodo
- Control individual y colectivo
- Estados persistentes en BD

### Datos Ambientales
- Generación automática de temperatura (-20°C a 50°C)
- Generación automática de humedad (30% a 95%)
- Timestamps precisos (formato: yyyy-MM-dd HH:mm:ss)
- Transmisión configurable (cada 1 segundo)

### MQTT Integration
- Publicación de datos a broker MQTT
- Suscripción a mensajes en tiempo real
- Soporte para QoS 0, 1, 2
- Reconexión automática

### Persistencia
- Base de datos SQL (MySQL/PostgreSQL)
- ORM con JPA/Hibernate
- Auditoría automática
- Consultas optimizadas

### API REST
- 20+ endpoints RESTful
- CORS configurado
- Validación de entrada
- Manejo robusto de errores

### Concurrencia
- Múltiples sensores transmitiendo simultáneamente
- Thread-safe con AtomicBoolean
- ConcurrentHashMap para estado
- Sin race conditions

---

## Requisitos Previos

### Software Requerido
- Java Development Kit (JDK) 17 o superior
```bash
java -version
```

- Apache Maven 3.8 o superior
```bash
mvn -version
```

- Base de Datos (una de las siguientes):
  - MySQL 8.0+
  - PostgreSQL 12+
  - MariaDB 10.5+

- MQTT Broker (uno de los siguientes):
  - test.mosquitto.org (público)
  - Mosquitto local
  - HiveMQ
  - EMQX

### Hardware Mínimo
- RAM: 2 GB
- Disco: 500 MB
- CPU: Procesador moderno

---

## Instalación Rápida

### 1. Clonar Repositorio
```bash
git clone https://github.com/Breynersmartinez/IOT-_distributed_sensor_system_whit_MQTT.git

```

### 2. Crear Base de Datos
```bash
# MySQL
mysql -u root -p
CREATE DATABASE iot_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
EXIT;

# PostgreSQL
psql -U postgres
CREATE DATABASE iot_db;
\q


CREATE TABLE sensor_data(
id UUID NOT NULL PRIMARY KEY,
marca_de_tiempo VARCHAR(150) NOT NULL,
temperatura DECIMAL NOT NULL,
humedad DECIMAL NOT NULL
);

CREATE TABLE multi_sensor_data(
id VARCHAR(100) NOT NULL PRIMARY KEY,
nombre_nodo VARCHAR(100) NOT NULL,
ubicacion VARCHAR(150) NOT NULL,
topico VARCHAR(150) NOT NULL,
activo BOOLEAN NOT NULL
);
```

### 3. Compilar
```bash
mvn clean install
```

### 4. Ejecutar
```bash
mvn spring-boot:run
```

El servidor estará disponible en: http://localhost:8080

O tambein el servidor estará disponible en: https://iot-distributed-sensor-system-whit-mqtt.onrender.com

---

## Configuración

### Archivo: application.properties

```properties
# ========================================
# SERVER CONFIGURATION
# ========================================
spring.application.name=IOT-_distributed_sensor_system_whit_MQTT
server.port=8080
server.servlet.context-path=/

# ========================================
# DATABASE CONFIGURATION
# ========================================


# PostgreSQL (alternativa)
spring.datasource.driver-class-name = org.postgresql.Driver
spring.datasource.url=${URL_DB}
spring.datasource.username=${USER_NAME}
spring.datasource.password=${PASSWORD_DB}
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true

# ========================================
# MQTT CONFIGURATION
# ========================================
mqtt.broker.url=tcp://test.mosquitto.org:1883
mqtt.topic=test5555868/topic
mqtt.client.id=mqttSpringClient
mqtt.qos=2



# Brokers alternativos:
# mqtt.broker.url=tcp://broker.emqx.io:1883
# mqtt.broker.url=tcp://mqtt.eclipseprojects.io:1883
# mqtt.broker.url=tcp://localhost:1883 (local)

# ========================================
# LOGGING CONFIGURATION
# ========================================
logging.level.root=info
logging.file.name=logs/app.log

```

### Variables de Entorno (alternativa)

Crear archivo .env:
```bash
DATABASE_URL=jdbc:mysql://localhost:3306/iot_db
DATABASE_USER=root
DATABASE_PASSWORD=password
MQTT_BROKER_URL=tcp://test.mosquitto.org:1883
MQTT_TOPIC=sensores/principal
MQTT_CLIENT_ID=iotSpringClient
MQTT_QOS=2
```

---

## Estructura del Proyecto

```


---

## API Endpoints

### Sensores Individuales

POST /sensor/start
Inicia transmisión de sensor único

POST /sensor/stop
Detiene transmisión de sensor único

GET /sensor-data/all
Obtiene todos los registros de sensores

Respuesta:
```json
[
  {
    "uuid": "550e8400-e29b-41d4-a716-446655440000",
    "timestamp": "2025-11-06 14:30:45",
    "temperature": 24.56,
    "humidity": 65.23
  }
]
```

GET /sensor-data/latest?limit=10
Obtiene los últimos N registros

---

### Múltiples Sensores

POST /multi-sensor/register
Registra un nuevo nodo sensor

```json
{
  "nodeId": "SENSOR-01",
  "nodeName": "Temperatura Oficina",
  "location": "Piso 3, Oficina 302",
  "mqttTopic": "sensors/office/temp-01"
}
```

POST /multi-sensor/register-multiple
Registra múltiples nodos

GET /multi-sensor/nodes
Obtiene todos los nodos

GET /multi-sensor/nodes/{nodeId}
Obtiene un nodo específico

PUT /multi-sensor/nodes/{nodeId}
Actualiza un nodo

DELETE /multi-sensor/nodes/{nodeId}
Elimina un nodo

POST /multi-sensor/start/{nodeId}
Inicia transmisión de nodo específico

POST /multi-sensor/stop/{nodeId}
Detiene transmisión de nodo específico

POST /multi-sensor/start-all
Inicia transmisión de todos los nodos

POST /multi-sensor/stop-all
Detiene transmisión de todos los nodos

GET /multi-sensor/active
Obtiene nodos activos

GET /multi-sensor/active/{nodeId}
Verifica si un nodo está activo

---

### MQTT

POST /mqtt/message
Publica un mensaje en MQTT

Parámetros:
- message: Contenido del mensaje
- topic: Tópico de destino
- qos: Nivel QoS (0, 1, 2)

GET /mqtt/subscribe?topic=sensores/test&qos=1
Se suscribe a un tópico (SSE)

POST /mqtt/disconnect
Desconecta del broker MQTT

POST /mqtt/reconnect
Reconecta al broker MQTT

---

## Arquitectura

### Capas de la Aplicación

```
Capa de Presentación (Controllers)
      |
      v
Capa de Lógica de Negocio (Services)
      |
      v
Capa de Acceso a Datos (Repositories)
      |
      v
Capa de Persistencia (Base de Datos)
```

### Patrones de Diseño

- MVC - Model View Controller
- Repository Pattern - Acceso a datos
- Service Pattern - Lógica de negocio
- Dependency Injection - Spring IoC
- Reactive Streams - Manejo de datos en tiempo real

---

## Conceptos Clave

### QoS (Quality of Service)

Define el nivel de garantía de entrega en MQTT:

| Nivel | Nombre | Descripción |
|-------|--------|-------------|
| 0 | At most once | Sin garantía de entrega |
| 1 | At least once | Garantía mínima |
| 2 | Exactly once | Máxima garantía |

Configuración en proyecto: mqtt.qos=2

---

### AtomicBoolean

Clase thread-safe para manejar flags booleanos:

```java
AtomicBoolean isStreaming = new AtomicBoolean(false);
isStreaming.set(true);
if (isStreaming.get()) {
    // Hacer algo
}
```

---

### ConcurrentHashMap

Map thread-safe para almacenar estado:

```java
Map<String, AtomicBoolean> streamingStates = new ConcurrentHashMap<>();
streamingStates.put("SENSOR-01", new AtomicBoolean(false));
```

---

### Server-Sent Events (SSE)

Permite que el servidor envíe datos de forma continua:

```java
@GetMapping(value = "/mqtt/subscribe", 
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public Flux<Object> streamMessages(@RequestParam String topic) {
    // Stream de datos
}
```

---

### Reactive Streams

Patrón para manejar flujos de datos asincronos:

```java
SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
publisher.subscribe(subscriber);
publisher.submit(message);
```

---

## Ejemplos de Uso

### Ejemplo 1: Registrar y Transmitir

```bash
# 1. Registrar nodo
curl -X POST http://localhost:8080/multi-sensor/register \
  -H "Content-Type: application/json" \
  -d '{
    "nodeId": "SENSOR-01",
    "nodeName": "Temperatura Oficina",
    "location": "Piso 3, Oficina 302",
    "mqttTopic": "sensors/office/temp-01"
  }'

# 2. Iniciar transmisión
curl -X POST http://localhost:8080/multi-sensor/start/SENSOR-01

# 3. Esperar 5 segundos

# 4. Obtener datos
curl http://localhost:8080/sensor-data/latest?limit=5
```

---

### Ejemplo 2: Múltiples Sensores

```bash
# Registrar 3 sensores
curl -X POST http://localhost:8080/multi-sensor/register-multiple \
  -H "Content-Type: application/json" \
  -d '[
    {"nodeId":"S-01","nodeName":"Oficina","location":"Piso 3","mqttTopic":"sensors/office/temp"},
    {"nodeId":"S-02","nodeName":"Bodega","location":"Sótano","mqttTopic":"sensors/warehouse/temp"},
    {"nodeId":"S-03","nodeName":"Almacén","location":"Almacén","mqttTopic":"sensors/storage/temp"}
  ]'

# Iniciar todos
curl -X POST http://localhost:8080/multi-sensor/start-all

# Ver nodos activos
curl http://localhost:8080/multi-sensor/active
```

---

## Troubleshooting

### Error: "Failed to connect to MQTT broker"

Causa: Broker MQTT no disponible

Solución:
```bash
# Verificar conectividad
telnet test.mosquitto.org 1883

# Cambiar broker en application.properties
mqtt.broker.url=tcp://broker.emqx.io:1883
```

---

### Error: "Cannot resolve table"

Causa: Tablas de BD no existen

Solución:
```bash
# Cambiar ddl-auto a create
spring.jpa.hibernate.ddl-auto=create

# O crear manualmente
mysql -u root -p iot_db < schema.sql
```

---

### Error: "Port 8080 already in use"

Causa: Otro proceso usando puerto 8080

Solución:
```bash
# Cambiar puerto
server.port=8081

# O matar proceso
lsof -i :8080
kill -9 <PID>
```

---

### Error: "Process is not defined"

Causa: Conflicto de variables de entorno (Vite/CRA)

Solución:
- Verificar que frontend use VITE_API_URL (Vite) o REACT_APP_API_URL (CRA)
- Revisar archivo api.js en frontend

---

### Error: "Cannot find module 'lucide-react'"

Causa: Dependencia no instalada

Solución:
```bash
cd front-end
npm install lucide-react
```

---

## Referencias

- Spring Boot Documentation: https://spring.io/projects/spring-boot
- Eclipse Paho MQTT: https://www.eclipse.org/paho/
- MQTT Protocol: https://mqtt.org/
- Hibernate ORM: https://hibernate.org/

---

## Próximas Mejoras

- Agregar autenticación JWT
- Agregar WebSocket para streaming
- Agregar métricas con Actuator
- Agregar tests unitarios
- Agregar documentación Swagger
- Agregar caché con Redis
- Agregar compresión de datos

---

Última actualización: Noviembre 2025
Versión: 1.0.0
Autor: Breiner Saul Martinez Muñoz