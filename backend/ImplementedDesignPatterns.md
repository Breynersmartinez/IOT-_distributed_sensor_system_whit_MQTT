# Análisis de Patrones de Diseño - Proyecto IoT MQTT

##  Patrones Correctamente Implementados

### 1. **Factory Method** 
**Ubicación**: `MqttConfig.mqttClient()` y `SensorService.create()`

**Justificación correcta**:
- Encapsula la creación compleja del `MqttClient` con sus configuraciones
- Evita acoplamiento directo a la clase `MqttClient`
- Facilita cambios futuros en la configuración

```java
@Bean
public MqttClient mqttClient() throws Exception {
    MqttClient mqttClient = new MqttClient(brokerUrl, clientId);
    MqttConnectOptions options = new MqttConnectOptions();
    options.setCleanSession(true);
    mqttClient.connect(options);
    return mqttClient;
}
```

---

### 2. **Observer Pattern** 
**Ubicación**: `MqttSubscriber` implementa `MqttCallback` y usa `SubmissionPublisher<String>`

**Justificación correcta**:
- Desacopla productores (sensores) de consumidores (clientes REST)
- Notifica automáticamente cambios cuando llegan nuevos mensajes
- Permite múltiples suscriptores reaccionando al mismo evento

```java
@Override
public void messageArrived(String topic, MqttMessage message) {
    String receivedMessage = new String(message.getPayload());
    publisher.submit(receivedMessage);  // Notifica observadores
}
```

---

### 3. **Dependency Injection** 
**Ubicación**: Todo el proyecto usa inyección de dependencias por constructor

**Justificación correcta**:
- Reduce acoplamiento entre clases
- Facilita testing y reemplazo de implementaciones
- Spring gestiona automáticamente el ciclo de vida

```java
public MqttController(MqttSubscriber mqttSubscriber, 
                     MqttPublisher mqttPublisher, 
                     MqttClient mqttClient) {
    this.mqttSubscriber = mqttSubscriber;
    this.mqttPublisher = mqttPublisher;
    this.mqttClient = mqttClient;
}
```

