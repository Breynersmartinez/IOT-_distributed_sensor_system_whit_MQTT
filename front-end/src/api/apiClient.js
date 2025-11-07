const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

export const apiClient = {
  // Sensor Data Endpoints
  async getLatestSensorData(limit = 20) {
    const response = await fetch(`${API_BASE_URL}/sensor-data/latest?limit=${limit}`);
    if (!response.ok) throw new Error('Error fetching sensor data');
    return response.json();
  },

  async getAllSensorData() {
    const response = await fetch(`${API_BASE_URL}/sensor-data/all`);
    if (!response.ok) throw new Error('Error fetching all sensor data');
    return response.json();
  },

  // Sensor Control Endpoints
  async startStreaming() {
    const response = await fetch(`${API_BASE_URL}/sensor/start`, { method: 'POST' });
    if (!response.ok) throw new Error('Error starting streaming');
    return response.json();
  },

  async stopStreaming() {
    const response = await fetch(`${API_BASE_URL}/sensor/stop`, { method: 'POST' });
    if (!response.ok) throw new Error('Error stopping streaming');
    return response.json();
  },

  // Multi-Sensor Endpoints
  async getAllSensorNodes() {
    const response = await fetch(`${API_BASE_URL}/multi-sensor/nodes`);
    if (!response.ok) throw new Error('Error fetching sensor nodes');
    return response.json();
  },

  async getSensorNode(nodeId) {
    const response = await fetch(`${API_BASE_URL}/multi-sensor/nodes/${nodeId}`);
    if (!response.ok) throw new Error('Error fetching sensor node');
    return response.json();
  },

  async registerSensorNode(node) {
    const response = await fetch(`${API_BASE_URL}/multi-sensor/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(node),
    });
    if (!response.ok) throw new Error('Error registering sensor node');
    return response.json();
  },

  async updateSensorNode(nodeId, node) {
    const response = await fetch(`${API_BASE_URL}/multi-sensor/nodes/${nodeId}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(node),
    });
    if (!response.ok) throw new Error('Error updating sensor node');
    return response.json();
  },

  async deleteSensorNode(nodeId) {
    const response = await fetch(`${API_BASE_URL}/multi-sensor/nodes/${nodeId}`, {
      method: 'DELETE',
    });
    if (!response.ok) throw new Error('Error deleting sensor node');
    return response.json();
  },

  async startSensorNode(nodeId) {
    const response = await fetch(`${API_BASE_URL}/multi-sensor/start/${nodeId}`, {
      method: 'POST',
    });
    if (!response.ok) throw new Error('Error starting sensor node');
    return response.json();
  },

  async stopSensorNode(nodeId) {
    const response = await fetch(`${API_BASE_URL}/multi-sensor/stop/${nodeId}`, {
      method: 'POST',
    });
    if (!response.ok) throw new Error('Error stopping sensor node');
    return response.json();
  },

  async getActiveSensorNodes() {
    const response = await fetch(`${API_BASE_URL}/multi-sensor/active`);
    if (!response.ok) throw new Error('Error fetching active sensors');
    return response.json();
  },

  async isNodeActive(nodeId) {
    const response = await fetch(`${API_BASE_URL}/multi-sensor/active/${nodeId}`);
    if (!response.ok) throw new Error('Error checking node status');
    return response.json();
  },

  async startAllSensors() {
    const response = await fetch(`${API_BASE_URL}/multi-sensor/start-all`, {
      method: 'POST',
    });
    if (!response.ok) throw new Error('Error starting all sensors');
    return response.json();
  },

  async stopAllSensors() {
    const response = await fetch(`${API_BASE_URL}/multi-sensor/stop-all`, {
      method: 'POST',
    });
    if (!response.ok) throw new Error('Error stopping all sensors');
    return response.json();
  },

  // MQTT Endpoints
  async publishMessage(message, topic, qos = 1) {
    const response = await fetch(
      `${API_BASE_URL}/mqtt/message?message=${encodeURIComponent(message)}&topic=${encodeURIComponent(topic)}&qos=${qos}`,
      { method: 'POST' }
    );
    if (!response.ok) throw new Error('Error publishing message');
    return response.json();
  },

  async disconnectMqtt() {
    const response = await fetch(`${API_BASE_URL}/mqtt/disconnect`, { method: 'POST' });
    if (!response.ok) throw new Error('Error disconnecting MQTT');
    return response.json();
  },

  async reconnectMqtt() {
    const response = await fetch(`${API_BASE_URL}/mqtt/reconnect`, { method: 'POST' });
    if (!response.ok) throw new Error('Error reconnecting MQTT');
    return response.json();
  },
};