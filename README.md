# IoT Distributed Sensor System with MQTT

![Version](https://img.shields.io/badge/version-1.0.0-blue?style=flat-square)
![Status](https://img.shields.io/badge/status-Active-success?style=flat-square)
![License](https://img.shields.io/badge/license-MIT-green?style=flat-square)
![Java](https://img.shields.io/badge/Java-17+-orange?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green?style=flat-square)
![React](https://img.shields.io/badge/React-18+-blue?style=flat-square)

IoT Distributed Sensor System with MQTT - A complete implementation of a distributed sensor network that transmits environmental data (temperature and humidity) through the MQTT protocol, with persistence in PostgreSQL cloud database and real-time visualization dashboard.

![alt text](img/img_2.png)

---

## Table of Contents

- [General Description](#general-description)
- [Main Features](#main-features)
- [System Requirements](#system-requirements)
- [Quick Installation](#quick-installation)
- [Detailed Configuration](#detailed-configuration)
- [Production Deployment](#production-deployment)
- [Architecture](#architecture)
- [REST API](#rest-api)
- [Usage](#usage)
- [Troubleshooting](#troubleshooting)
- [Technologies](#technologies)
- [Author](#author)

---

## General Description

This project implements a **complete IoT distributed sensor system** with the following capabilities:

### Main Features

1. **Sensor Node Registration and Management** - Create, update, delete and control multiple sensor nodes
![alt text](img/image-3.png)

2. **Real-Time Data Transmission** - Automatic publication of temperature and humidity every second
![alt text](img/image-2.png)

3. **MQTT Communication** - MQTT broker integration for pub/sub with configurable QoS

4. **Data Persistence** - Storage in PostgreSQL cloud database (Neon DB)

5. **Interactive Visualization** - Modern, responsive and real-time React dashboard
![alt text](img/image-4.png)

6. **Complete REST API** - 20+ endpoints for all system operations

### Access Links

- **Live Demo**: https://iot-distributed-sensor-system-whit.vercel.app
- **Backend API**: https://iot-distributed-sensor-system-whit-mqtt.onrender.com
- **API Documentation**: https://iot-distributed-sensor-system-whit-mqtt.onrender.com/swagger-ui/index.html
- **Repository**: https://github.com/Breynersmartinez/IOT-_distributed_sensor_system_whit_MQTT

---

## Main Features

### Backend (Spring Boot 3.x)

 Distributed and scalable sensor system  
 MQTT Publish/Subscribe with Eclipse Paho  
 Data generation (temperature -20°C to 50°C, humidity 30% to 95%)  
 Individual and bulk sensor control  
 Persistence with JPA/Hibernate in PostgreSQL  
 Reactive real-time streaming  
 Safe concurrency handling (AtomicBoolean, ScheduledExecutorService)  
 Complete REST API with Swagger  
 CORS configured for multiple origins  
 Complete logging with SLF4J  

### Frontend (React 18+)

 Modern dashboard with Tailwind CSS  
 Responsive interface (mobile, tablet, desktop)  
 Sensor node registration and management  
 Transmission control (start/stop global and individual)  
 Real-time data table with 5-second polling  
 Interactive charts with Recharts  
 Live statistics and indicators  
 Form validation  
 Robust error handling  
 Professional dark mode  

---

## System Requirements

### Backend

- Java 17 or higher
- Maven 3.8+
- PostgreSQL 12+ (or MySQL 8.0+)
- MQTT Broker (test.mosquitto.org or local)

### Frontend

- Node.js 16+
- npm 8+

### Infrastructure (Production)

- Render (Backend hosting)
- Vercel (Frontend hosting)
- Neon DB (PostgreSQL cloud database)

---

## Quick Installation

### 1. Clone the Repository

```bash
git clone https://github.com/Breynersmartinez/IOT-_distributed_sensor_system_whit_MQTT.git
cd IOT-_distributed_sensor_system_whit_MQTT
```

### 2. Backend

```bash
# Compile
mvn clean install

# Configure application.properties with your DB credentials
nano src/main/resources/application.properties

# Run
mvn spring-boot:run
```

Backend will be available at: **http://localhost:8080**

![alt text](img/execution.png)

### 3. Frontend

```bash
cd front-end

# Install dependencies
npm install

# Create .env file
echo "VITE_API_URL=http://localhost:8080" > .env

# Run
npm run dev
```

Frontend will be available at: **http://localhost:5173**

![alt text](img/image.png)

---

## Detailed Configuration

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

![alt text](img/image-1.png)

### Configure Database (Neon)

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

## Production Deployment

### Backend on Render

1. Create account at [Render.com](https://render.com)
2. New Web Service from GitHub
3. Configure environment variables:
   ```
   URL_DB=jdbc:postgresql://...
   USER_NAME=user
   PASSWORD_DB=password
   ```
4. Automatic deployment
5. **URL**: https://iot-distributed-sensor-system-whit-mqtt.onrender.com

### Frontend on Vercel

1. Create account at [Vercel.com](https://vercel.com)
2. Import repository from GitHub
3. Configure environment variable:
   ```
   VITE_API_URL=https://iot-distributed-sensor-system-whit-mqtt.onrender.com
   ```
4. Automatic deployment
5. **URL**: https://iot-distributed-sensor-system-whit.vercel.app

---

## Architecture

### General Diagram

![alt text](img/architecture.png)

### Class Diagram

![alt text](img/class-diagram.png)

### Key Components

**Backend:**
- Controllers: SensorController, SensorDataController, MultiSensorController, MqttController
- Services: SensorService, MultiSensorService, MqttPublisher, MqttSubscriber
- Models: Sensor, SensorNode
- Config: MqttConfig, CorsConfig

**Frontend:**
- SensorDashboard: Main component
- State: latestData, sensorNodes, activeSensors
- Effects: 5-second polling
- API Calls: Fetch with error handling

---

## REST API

### Main Endpoints

**Individual Sensors:**
```
POST   /sensor/start              Start transmission
POST   /sensor/stop               Stop transmission
GET    /sensor-data/all           Get all records
GET    /sensor-data/latest?limit=10  Get last N records
```

**Multiple Sensor Nodes:**
```
POST   /multi-sensor/register           Register new node
GET    /multi-sensor/nodes              Get all nodes
GET    /multi-sensor/nodes/{nodeId}     Get specific node
PUT    /multi-sensor/nodes/{nodeId}     Update node
DELETE /multi-sensor/nodes/{nodeId}     Delete node
POST   /multi-sensor/start/{nodeId}     Start specific node
POST   /multi-sensor/stop/{nodeId}      Stop specific node
POST   /multi-sensor/start-all          Start all nodes
POST   /multi-sensor/stop-all           Stop all nodes
GET    /multi-sensor/active             Get active nodes
GET    /multi-sensor/active/{nodeId}    Check if node is active
```

**MQTT:**
```
POST   /mqtt/message              Publish message to MQTT
GET    /mqtt/subscribe            Subscribe to topic (SSE)
POST   /mqtt/disconnect           Disconnect from broker
POST   /mqtt/reconnect            Reconnect to broker
```

### Usage Example

```bash
# Register sensor node
curl -X POST http://localhost:8080/multi-sensor/register \
  -H "Content-Type: application/json" \
  -d '{
    "nodeId": "SENSOR-01",
    "nodeName": "Office Temperature",
    "location": "Floor 3",
    "mqttTopic": "sensors/office/temp"
  }'

# Start transmission
curl -X POST http://localhost:8080/sensor/start

# Get latest data
curl http://localhost:8080/sensor-data/latest?limit=10
```

---

## Usage

### 1. Register a Sensor Node

From the dashboard:
1. Click on "New Node"
2. Fill out the form
3. Click on "Register Node"

### 2. Start Transmission

From the dashboard:
1. Click on "Start" button (green)
2. Watch the status change to "Active"
3. Data will begin to generate and display in real-time

### 3. Monitor Data

- **Table**: Shows the last 10 records
- **Chart**: Displays temperature and humidity in real-time
- **Indicators**: Show average temperature, humidity and active nodes

### 4. Control Individual Nodes

For each node in the table:
- "Start" button: Begin node transmission
- "Stop" button: Stop node transmission
- "Delete" button: Remove node from system

---

## Troubleshooting

### Error: "Failed to connect to backend"

**Verify:**
```bash
# Check that backend is running
curl http://localhost:8080/sensor-data/all

# Verify CORS in backend
grep "allowed-origins" src/main/resources/application.properties
```

### Error: "MQTT Connection refused"

**Solution:**
```bash
# Test connectivity to broker
telnet test.mosquitto.org 1883

# Or change to another broker in application.properties
mqtt.broker.url=tcp://broker.emqx.io:1883
```

### Error: "Database connection error"

**Verify:**
```bash
# Check credentials in .bashrc or .env
cat ~/.bashrc | grep URL_DB

# Test PostgreSQL connection
psql postgresql://user:pass@host/database
```

### Frontend not loading data

**Solution:**
```bash
# Verify .env has correct URL
cat .env | grep VITE_API_URL

# Restart frontend
npm run dev
```

### Lag or slow updates

**Important Note (Production):**
When using free Render tier, the first request may take seconds due to hibernation policy. Once reactivated, the system performs optimally.

---

## Technologies

### Backend
- **Java 17+** - Programming language
- **Spring Boot 3.x** - Web framework
- **Spring Data JPA** - ORM for database
- **Eclipse Paho** - MQTT client
- **PostgreSQL** - Database
- **Maven 3.8+** - Dependency manager
- **Swagger** - API documentation

### Frontend
- **React 18+** - UI library
- **Vite** - Modern bundler
- **Tailwind CSS** - Styling framework
- **Recharts** - Charting library
- **Lucide React** - Icon library

### Infrastructure
- **MQTT 3.1.1** - Messaging protocol
- **PostgreSQL** - Relational database
- **Render** - Backend hosting
- **Vercel** - Frontend hosting
- **Neon DB** - PostgreSQL cloud
- **Git** - Version control

---

## Security

 Environment variables for sensitive credentials  
 CORS configured to validate origin  
 Input validation in forms and API  
 Safe exception handling  
 SQL Injection prevention with JPA  
 Thread-safe with AtomicBoolean and ConcurrentHashMap  
 No exposure of sensitive information in logs  

---

## Performance

 Polling configured every 5 seconds (avoids overload)  
 Persistent MQTT connection (reuses connection)  
 Frontend caching (reduces API calls)  
 Database indexes for fast queries  
 Data generation every 1 second (configurable)  
 ScheduledExecutorService for periodic tasks  

---

## Future Improvements

- [ ] Add JWT authentication
- [ ] Implement WebSocket for real streaming
- [ ] Add caching with Redis
- [ ] Data compression
- [ ] Real-time notifications
- [ ] Support for multiple MQTT brokers
- [ ] Dashboard with more metrics
- [ ] Data export (CSV, Excel)

---

## Contributions

Contributions are welcome. For major changes:

1. Fork the project
2. Create a branch: `git checkout -b feature/AmazingFeature`
3. Commit: `git commit -m 'Add AmazingFeature'`
4. Push: `git push origin feature/AmazingFeature`
5. Open a Pull Request

---

## License

This project is licensed under MIT. See [LICENSE](LICENSE) for details.

---

## Author

**Breiner Saul Martinez Muñoz**

- GitHub: [@Breynersmartinez](https://github.com/Breynersmartinez)
- Repository: [IOT-_distributed_sensor_system_whit_MQTT](https://github.com/Breynersmartinez/IOT-_distributed_sensor_system_whit_MQTT)

---

## Support

To report bugs or request features:
- Create an [Issue](https://github.com/Breynersmartinez/IOT-_distributed_sensor_system_whit_MQTT/issues)
- Direct contact via GitHub

---

## Acknowledgments

- [Eclipse Paho](https://www.eclipse.org/paho/) - MQTT client
- [Spring Boot Team](https://spring.io/projects/spring-boot) - Framework
- [React Team](https://react.dev/) - UI library
- [Tailwind CSS](https://tailwindcss.com/) - Styling framework
- [Recharts](https://recharts.org/) - Data visualization

---

**Last Updated:** November 2025  
**Version:** 1.0.0  
**Status:** Production 