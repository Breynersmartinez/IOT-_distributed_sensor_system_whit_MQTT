# IoT Distributed Sensor System with MQTT

![Version](https://img.shields.io/badge/version-1.0.0-blue?style=flat-square)
![Status](https://img.shields.io/badge/status-Active-success?style=flat-square)
![License](https://img.shields.io/badge/license-MIT-green?style=flat-square)

Sistema de Sensores Distribuidos IoT con MQTT

Implementación completa de una red de sensores distribuidos que transmite datos ambientales (temperatura y humedad) a través del protocolo MQTT, con persistencia en base de datos y dashboard de visualización en tiempo real.

---

## Tabla de Contenidos

- [Descripción General](#descripción-general)
- [Características Principales](#características-principales)
- [Requisitos del Sistema](#requisitos-del-sistema)
- [Instalación](#instalación)
- [Arquitectura](#arquitectura)
- [Uso](#uso)
- [Documentación](#documentación)
- [Tecnologías](#tecnologías)
- [Autor](#autor)

---

## Descripción General

Este proyecto implementa un sistema completo de sensores distribuidos IoT que permite:

1. Registro y gestión de nodos sensores - Crear, actualizar, eliminar nodos sensores
2. Transmisión de datos en tiempo real - Publicación automática de temperatura y humedad
3. Comunicación MQTT - Integración con broker MQTT para pubsub
4. Persistencia de datos - Almacenamiento en base de datos SQL
5. Visualización interactiva - Dashboard React moderno y responsivo
6. API REST completa - Endpoints para todas las operaciones

---

## Características Principales

### Backend (Spring Boot)
- Sistema de sensores distribuidos y escalable
- Publicación/Suscripción MQTT con Paho
- Transmisión configurable de datos (temperatura y humedad)
- Control individual y masivo de sensores
- Persistencia con JPA/Hibernate
- Streaming reactivo en tiempo real
- Manejo seguro de concurrencia (threads, AtomicBoolean)
- API REST RESTful completa
- CORS configurado
- Logging completo con SLF4J

### Frontend (React)
- Dashboard moderno con Tailwind CSS
- Interfaz responsiva (mobile, tablet, desktop)
- Registro de nodos sensores
- Control de transmisión (start/stop)
- Tabla de datos en tiempo real
- Estadísticas e indicadores
- Validación de formularios
- Manejo robusto de errores
- Dark mode profesional
- Actualización automática cada 5 segundos

---

## Requisitos del Sistema

### Backend
- Java 17 o superior
- Maven 3.8+
- Base de Datos MySQL 8.0+ o PostgreSQL 12+
- MQTT Broker (test.mosquitto.org o local)

### Frontend
- Node.js 16+
- npm 8+

### Común
- Git para control de versiones
- Terminal/CMD

---

## Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/iot-distributed-sensor-system.git
cd iot-distributed-sensor-system
```

### 2. Configurar Backend

```bash
cd backend

# Compilar
mvn clean install

# Crear base de datos
mysql -u root -p
CREATE DATABASE iot_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE iot_db;
```

Configurar `application.properties`:

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

### 3. Configurar Frontend

```bash
cd ../front-end

# Instalar dependencias
npm install

# Crear archivo .env
echo "VITE_API_URL=http://localhost:8080" > .env
```

### 4. Ejecutar Aplicación

Terminal 1 - Backend:
```bash
cd backend
mvn spring-boot:run
```

Terminal 2 - Frontend:
```bash
cd front-end
npm run dev
```

Acceder en: http://localhost:5173

---

## Arquitectura

### Arquitectura General

```
Capa de Presentacion (Frontend)
      |
      | HTTP/REST
      |
      v
Capa de Aplicacion (Backend - Spring Boot)
      |
      | MQTT Protocol
      |
      v
MQTT Broker (test.mosquitto.org:1883)
      |
      v
Base de Datos SQL
(MySQL/PostgreSQL)
```

### Componentes Principales

**Backend:**
- Controllers: Endpoints REST
- Services: Lógica de negocio
- Repositories: Acceso a datos
- Models: Entidades de dominio
- Config: Configuraciones (MQTT, CORS)

**Frontend:**
- Components: Componentes React
- Hooks: Lógica reutilizable
- Services: Llamadas a API
- Utils: Funciones auxiliares

---

## Uso

### 1. Registrar un Nodo Sensor

Frontend:
1. Click en "Nuevo Nodo"
2. Completar formulario
3. Click en "Registrar Nodo"

cURL:
```bash
curl -X POST http://localhost:8080/multi-sensor/register \
  -H "Content-Type: application/json" \
  -d '{
    "nodeId": "SENSOR-01",
    "nodeName": "Temperatura Oficina",
    "location": "Piso 3, Oficina 302",
    "mqttTopic": "sensors/office/temp-01"
  }'
```

### 2. Iniciar Transmisión

Frontend:
- En tabla de nodos, click en botón de inicio

cURL:
```bash
curl -X POST http://localhost:8080/multi-sensor/start/SENSOR-01
```

### 3. Ver Datos en Tiempo Real

Frontend:
- Datos aparecen automáticamente en tabla

cURL:
```bash
curl http://localhost:8080/sensor-data/latest?limit=10
```

---

## Documentación

| Documento | Descripción |
|-----------|-----------|
| Backend README | Documentación técnica del backend |
| Frontend README | Documentación técnica del frontend |
| API Reference | Referencia completa de endpoints |
| Architecture | Arquitectura del sistema |

---

## Tecnologías

### Backend
- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- Eclipse Paho (MQTT)
- MySQL 8.0
- Maven 3.8+
- SLF4J (Logging)

### Frontend
- React 18+
- Tailwind CSS
- Vite
- Lucide React

### Infraestructura
- MQTT 3.1.1
- REST API
- JPA/Hibernate

---

## Seguridad

- Variables de entorno para configuración sensible
- CORS configurado para validar origen
- Validación de entrada en formularios
- Manejo seguro de excepciones
- No exposición de información sensible
- SQL Injection prevention con JPA
- Thread-safe con AtomicBoolean y ConcurrentHashMap

---

## Rendimiento

- Polling configurado cada 5 segundos (evita sobrecarga)
- Conexión persistente a MQTT (reutiliza conexión)
- Caché en frontend (reduce llamadas API)
- Índices en BD para consultas rápidas
- Compresión de respuestas JSON

---

## Troubleshooting

### Error: "Failed to resolve import 'lucide-react'"
```bash
cd front-end
npm install lucide-react
```

### Error: "CORS policy: No 'Access-Control-Allow-Origin'"
Verificar que CorsConfig.java esté en backend

### Error: "Connection refused" en MQTT
Verificar que broker esté disponible: telnet test.mosquitto.org 1883

### Error: "Database connection refused"
Verificar que MySQL esté corriendo: sudo systemctl start mysql

---

## Contribuciones

Las contribuciones son bienvenidas. Para cambios mayores:

1. Fork el proyecto
2. Crea una rama (git checkout -b feature/AmazingFeature)
3. Commit cambios (git commit -m 'Add AmazingFeature')
4. Push a la rama (git push origin feature/AmazingFeature)
5. Abre un Pull Request

---

## Licencia

Este proyecto está bajo licencia MIT. Ver LICENSE para más detalles.

---

## Autor

Breiner Saul Martinez Muñoz

- GitHub: https://github.com/tu-usuario
- Email: tu.email@example.com

---

## Soporte

Para reportar bugs o solicitar features:
- Issues: https://github.com/tu-usuario/iot-distributed-sensor-system/issues
- Email: soporte@example.com

---

## Agradecimientos

- Eclipse Paho - Cliente MQTT
- Spring Boot Team - Framework
- React Team - Librería UI
- Tailwind CSS - Framework CSS

---

Última actualización: Noviembre 2025
Versión: 1.0.0
Estado: Producción