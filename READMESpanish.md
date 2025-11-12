# IoT Distributed Sensor System with MQTT

![Version](https://img.shields.io/badge/version-1.0.0-blue?style=flat-square)
![Status](https://img.shields.io/badge/status-Active-success?style=flat-square)
![License](https://img.shields.io/badge/license-MIT-green?style=flat-square)
![Java](https://img.shields.io/badge/Java-17+-orange?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green?style=flat-square)
![React](https://img.shields.io/badge/React-18+-blue?style=flat-square)

Sistema de Sensores Distribuidos IoT con MQTT - Implementación completa de una red de sensores distribuidos que transmite datos ambientales (temperatura y humedad) a través del protocolo MQTT, con persistencia en base de datos PostgreSQL en la nube y dashboard de visualización en tiempo real.

![alt text](img/img_2.png)


---

## Tabla de Contenidos

- [Descripción General](#descripción-general)
- [Características Principales](#características-principales)
- [Requisitos del Sistema](#requisitos-del-sistema)
- [Instalación Rápida](#instalación-rápida)
- [Configuración Detallada](#configuración-detallada)
- [Despliegue en Producción](#despliegue-en-producción)
- [Arquitectura](#arquitectura)
- [API REST](#api-rest)
- [Uso](#uso)
- [Troubleshooting](#troubleshooting)
- [Tecnologías](#tecnologías)
- [Autor](#autor)

---

## Descripción General

Este proyecto implementa un **sistema completo de sensores distribuidos IoT** con las siguientes capacidades:

### Funcionalidades Principales

1. **Registro y Gestión de Nodos Sensores** - Crear, actualizar, eliminar y controlar múltiples nodos sensores
![alt text](image-3.png)
2. **Transmisión de Datos en Tiempo Real** - Publicación automática de temperatura y humedad cada segundo
![alt text](image-2.png)
3. **Comunicación MQTT** - Integración con broker MQTT para pub/sub con QoS configurable



4. **Persistencia de Datos** - Almacenamiento en PostgreSQL en la nube (Neon DB)

5. **Visualización Interactiva** - Dashboard React moderno, responsivo y en tiempo real
![alt text](image-4.png)


6. **API REST Completa** - 20+ endpoints para todas las operaciones del sistema

### Enlaces de Acceso

- **Demo en Vivo**: https://iot-distributed-sensor-system-whit.vercel.app
- **API Backend**: https://iot-distributed-sensor-system-whit-mqtt.onrender.com
- **Documentación API**: https://iot-distributed-sensor-system-whit-mqtt.onrender.com/swagger-ui/index.html
- **Repositorio**: https://github.com/Breynersmartinez/IOT-_distributed_sensor_system_whit_MQTT

---

## Características Principales

### Backend (Spring Boot 3.x)

 Sistema de sensores distribuidos y escalable  
 Publicación/Suscripción MQTT con Eclipse Paho  
 Generación de datos (temperatura -20°C a 50°C, humedad 30% a 95%)  
 Control individual y masivo de sensores  
 Persistencia con JPA/Hibernate en PostgreSQL  
 Streaming reactivo en tiempo real  
 Manejo seguro de concurrencia (AtomicBoolean, ScheduledExecutorService)  
 API REST RESTful completa con Swagger  
 CORS configurado para múltiples orígenes  
 Logging completo con SLF4J  

### Frontend (React 18+)

 Dashboard moderno con Tailwind CSS  
 Interfaz responsiva (mobile, tablet, desktop)  
 Registro y gestión de nodos sensores  
 Control de transmisión (start/stop global e individual)  
 Tabla de datos en tiempo real con polling cada 5 segundos  
 Gráficos interactivos con Recharts  
 Estadísticas e indicadores en vivo  
 Validación de formularios  
 Manejo robusto de errores  
 Dark mode profesional  

---

## Requisitos del Sistema

### Backend

- Java 17 o superior
- Maven 3.8+
- PostgreSQL 12+ (o MySQL 8.0+)
- MQTT Broker (test.mosquitto.org o local)

### Frontend

- Node.js 16+
- npm 8+

### Infraestructura (Producción)

- Render (Backend hosting)
- Vercel (Frontend hosting)
- Neon DB (Base de datos PostgreSQL en la nube)

---

## Instalación Rápida

### 1. Clonar el Repositorio

```bash
git clone https://github.com/Breynersmartinez/IOT-_distributed_sensor_system_whit_MQTT.git
cd IOT-_distributed_sensor_system_whit_MQTT
```

### 2. Backend

```bash
# Compilar
mvn clean install

# Configurar application.properties con tus credenciales de BD
nano src/main/resources/application.properties

# Ejecutar
mvn spring-boot:run
```



El backend estará disponible en: **http://localhost:8080**


![alt text](img/execution.png)

### 3. Frontend

```bash
cd front-end

# Instalar dependencias
npm install

# Crear archivo .env
echo "VITE_API_URL=http://localhost:8080" > .env

# Ejecutar
npm run dev
```

El frontend estará disponible en: **http://localhost:5173**

![alt text](image.png)


---


## Configuración Detallada

### Backend - application.properties

```properties
# SERVER
spring.application.name=IOT-_distributed_sensor_system_whit_MQTT
server.port=8080
server.address=0.0.0.0

# DATABASE - PostgreSQL (Neon)
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=${URL_DB}
spring.datasource.username=${USER_NAME}
spring.datasource.password=${PASSWORD_DB}

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=false

# MQTT
mqtt.broker.url=tcp://test.mosquitto.org:1883
mqtt.topic=test5555868/topic
mqtt.client.id=mqttSpringClient
mqtt.qos=2

# CORS
spring.web.cors.allowed-origins=http://localhost:5173,http://192.168.110.41:5173,https://iot-distributed-sensor-system-whit.vercel.app
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*
spring.web.cors.allow-credentials=true

# LOGGING
logging.level.root=info
logging.file.name=logs/app.log
```





### Frontend - .env

```env
VITE_API_URL=http://192.168.110.139:8080
VITE_APP_NAME=IoT Dashboard
VITE_APP_VERSION=1.0.0
```

![alt text](image-1.png)

### Configurar Base de Datos (Neon)

```sql
CREATE TABLE sensor_data (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    marca_de_tiempo VARCHAR(150) NOT NULL,
    temperatura DECIMAL(10,2) NOT NULL,
    humedad DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE multi_sensor_data (
    id VARCHAR(100) PRIMARY KEY,
    nombre_nodo VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(150) NOT NULL,
    topico VARCHAR(150) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_sensor_timestamp ON sensor_data(marca_de_tiempo);
CREATE INDEX idx_sensor_created ON sensor_data(created_at);
```


![alt text](img/db.png)

---

## Despliegue en Producción

### Backend en Render

1. Crear cuenta en [Render.com](https://render.com)
2. Nuevo Web Service desde GitHub
3. Configurar variables de entorno:
   ```
   URL_DB=jdbc:postgresql://...
   USER_NAME=usuario
   PASSWORD_DB=contraseña
   ```
4. Deploy automático
5. **URL**: https://iot-distributed-sensor-system-whit-mqtt.onrender.com

### Frontend en Vercel

1. Crear cuenta en [Vercel.com](https://vercel.com)
2. Importar repositorio desde GitHub
3. Configurar variable de entorno:
   ```
   VITE_API_URL=https://iot-distributed-sensor-system-whit-mqtt.onrender.com
   ```
4. Deploy automático
5. **URL**: https://iot-distributed-sensor-system-whit.vercel.app

---

## Arquitectura

### Diagrama General

![alt text](img/architecture.png)

### Diagrma de clases 

![alt text](img/class-diagram.png)

### Componentes Clave

**Backend:**
- Controllers: SensorController, SensorDataController, MultiSensorController, MqttController
- Services: SensorService, MultiSensorService, MqttPublisher, MqttSubscriber
- Models: Sensor, SensorNode
- Config: MqttConfig, CorsConfig

**Frontend:**
- SensorDashboard: Componente principal
- Estado: latestData, sensorNodes, activeSensors
- Efectos: Polling cada 5 segundos
- Llamadas API: Fetch con manejo de errores

---

## API REST

### Endpoints Principales

**Sensores Individuales:**
```
POST   /sensor/start              Inicia transmisión
POST   /sensor/stop               Detiene transmisión
GET    /sensor-data/all           Obtiene todos los registros
GET    /sensor-data/latest?limit=10  Obtiene últimos N registros
```

**Nodos Sensores Múltiples:**
```
POST   /multi-sensor/register           Registra nuevo nodo
GET    /multi-sensor/nodes              Obtiene todos los nodos
GET    /multi-sensor/nodes/{nodeId}     Obtiene nodo específico
PUT    /multi-sensor/nodes/{nodeId}     Actualiza nodo
DELETE /multi-sensor/nodes/{nodeId}     Elimina nodo
POST   /multi-sensor/start/{nodeId}     Inicia nodo específico
POST   /multi-sensor/stop/{nodeId}      Detiene nodo específico
POST   /multi-sensor/start-all          Inicia todos los nodos
POST   /multi-sensor/stop-all           Detiene todos los nodos
GET    /multi-sensor/active             Obtiene nodos activos
GET    /multi-sensor/active/{nodeId}    Verifica si nodo está activo
```

**MQTT:**
```
POST   /mqtt/message              Publica mensaje en MQTT
GET    /mqtt/subscribe            Se suscribe a tópico (SSE)
POST   /mqtt/disconnect           Desconecta del broker
POST   /mqtt/reconnect            Reconecta al broker
```

### Ejemplo de Uso

```bash
# Registrar nodo sensor
curl -X POST http://localhost:8080/multi-sensor/register \
  -H "Content-Type: application/json" \
  -d '{
    "nodeId": "SENSOR-01",
    "nodeName": "Temperatura Oficina",
    "location": "Piso 3",
    "mqttTopic": "sensors/office/temp"
  }'

# Iniciar transmisión
curl -X POST http://localhost:8080/sensor/start

# Obtener últimos datos
curl http://localhost:8080/sensor-data/latest?limit=10
```

---

## Uso

### 1. Registrar un Nodo Sensor

Desde el dashboard:
1. Hacer clic en "Nuevo Nodo"
2. Completar el formulario
3. Hacer clic en "Registrar Nodo"

### 2. Iniciar Transmisión

Desde el dashboard:
1. Hacer clic en botón "Iniciar" (verde)
2. Observar el estado cambiar a "Activo"
3. Los datos comenzarán a generarse y visualizarse en tiempo real

### 3. Monitorear Datos

- **Tabla**: Muestra los últimos 10 registros
- **Gráfico**: Visualiza temperatura y humedad en tiempo real
- **Indicadores**: Muestran promedio de temperatura, humedad y nodos activos

### 4. Controlar Nodos Individuales

Para cada nodo en la tabla:
- Botón "Iniciar": Comienza transmisión del nodo
- Botón "Detener": Detiene transmisión del nodo
- Botón "Eliminar": Borra el nodo del sistema

---

## Troubleshooting

### Error: "Failed to connect to backend"

**Verificar:**
```bash
# Verificar que backend está corriendo
curl http://localhost:8080/sensor-data/all

# Verificar CORS en backend
grep "allowed-origins" src/main/resources/application.properties
```

### Error: "MQTT Connection refused"

**Solución:**
```bash
# Probar conectividad a broker
telnet test.mosquitto.org 1883

# O cambiar a otro broker en application.properties
mqtt.broker.url=tcp://broker.emqx.io:1883
```

### Error: "Database connection error"

**Verificar:**
```bash
# Verificar credenciales en .bashrc o .env
cat ~/.bashrc | grep URL_DB

# Probar conexión a PostgreSQL
psql postgresql://user:pass@host/database
```

### El frontend no carga datos

**Solución:**
```bash
# Verificar que .env tiene URL correcta
cat .env | grep VITE_API_URL

# Reiniciar frontend
npm run dev
```

### Lag o lentitud en actualización

**Nota Importante (Producción):**
Al usar Render gratuito, la primera petición puede tardar segundos debido a la política de hibernación. Una vez reactivada, el sistema funciona óptimamente.

---

## Tecnologías

### Backend
- **Java 17+** - Lenguaje de programación
- **Spring Boot 3.x** - Framework web
- **Spring Data JPA** - ORM para base de datos
- **Eclipse Paho** - Cliente MQTT
- **PostgreSQL** - Base de datos
- **Maven 3.8+** - Gestor de dependencias
- **Swagger** - Documentación de API

### Frontend
- **React 18+** - Librería UI
- **Vite** - Bundler moderno
- **Tailwind CSS** - Framework de estilos
- **Recharts** - Librería de gráficos
- **Lucide React** - Iconografía

### Infraestructura
- **MQTT 3.1.1** - Protocolo de mensajería
- **PostgreSQL** - Base de datos relacional
- **Render** - Hosting backend
- **Vercel** - Hosting frontend
- **Neon DB** - PostgreSQL en la nube
- **Git** - Control de versiones

---

## Seguridad

 Variables de entorno para credenciales sensibles  
 CORS configurado para validar origen  
Validación de entrada en formularios y API  
 Manejo seguro de excepciones  
 SQL Injection prevention con JPA  
 Thread-safe con AtomicBoolean y ConcurrentHashMap  
 No exposición de información sensible en logs  

---

## Rendimiento

 Polling configurado cada 5 segundos (evita sobrecarga)  
 Conexión persistente a MQTT (reutiliza conexión)  
 Caché en frontend (reduce llamadas API)  
 Índices en base de datos para consultas rápidas  
 Generación de datos cada 1 segundo (configurable)  
 ScheduledExecutorService para tareas periódicas  

---

## Próximas Mejoras

- [ ] Agregar autenticación JWT
- [ ] Implementar WebSocket para streaming real
- [ ] Agregar caché con Redis
- [ ] Compresión de datos
- [ ] Notificaciones en tiempo real
- [ ] Soporte para múltiples brokers MQTT
- [ ] Dashboard con más métricas
- [ ] Exportación de datos (CSV, Excel)

---

## Contribuciones

Las contribuciones son bienvenidas. Para cambios mayores:

1. Fork el proyecto
2. Crea una rama: `git checkout -b feature/AmazingFeature`
3. Commit: `git commit -m 'Add AmazingFeature'`
4. Push: `git push origin feature/AmazingFeature`
5. Abre un Pull Request

---

## Licencia

Este proyecto está bajo licencia MIT. Ver [LICENSE](LICENSE) para más detalles.

---

## Autor

**Breiner Saul Martinez Muñoz**

- GitHub: [@Breynersmartinez](https://github.com/Breynersmartinez)
- Repositorio: [IOT-_distributed_sensor_system_whit_MQTT](https://github.com/Breynersmartinez/IOT-_distributed_sensor_system_whit_MQTT)

---

## Soporte

Para reportar bugs o solicitar features:
- Crear un [Issue](https://github.com/Breynersmartinez/IOT-_distributed_sensor_system_whit_MQTT/issues)
- Contacto directo por GitHub

---

## Agradecimientos

- [Eclipse Paho](https://www.eclipse.org/paho/) - Cliente MQTT
- [Spring Boot Team](https://spring.io/projects/spring-boot) - Framework
- [React Team](https://react.dev/) - Librería UI
- [Tailwind CSS](https://tailwindcss.com/) - Framework CSS
- [Recharts](https://recharts.org/) - Visualización de datos

---

**Última actualización:** Noviembre 2025  
**Versión:** 1.0.0  
**Estado:** Producción 