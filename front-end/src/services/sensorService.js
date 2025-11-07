import { apiCall } from './api';

export const sensorService = {
  // Nodos
  async registerNode(nodeData) {
    return apiCall('/multi-sensor/register', {
      method: 'POST',
      body: JSON.stringify(nodeData),
    });
  },

  async registerMultipleNodes(nodesData) {
    return apiCall('/multi-sensor/register-multiple', {
      method: 'POST',
      body: JSON.stringify(nodesData),
    });
  },

  async getAllNodes() {
    return apiCall('/multi-sensor/nodes');
  },

  async getNode(nodeId) {
    return apiCall(`/multi-sensor/nodes/${nodeId}`);
  },

  async updateNode(nodeId, nodeData) {
    return apiCall(`/multi-sensor/nodes/${nodeId}`, {
      method: 'PUT',
      body: JSON.stringify(nodeData),
    });
  },

  async deleteNode(nodeId) {
    return apiCall(`/multi-sensor/nodes/${nodeId}`, {
      method: 'DELETE',
    });
  },

  // Control de transmisión
  async startNode(nodeId) {
    return apiCall(`/multi-sensor/start/${nodeId}`, {
      method: 'POST',
    });
  },

  async stopNode(nodeId) {
    return apiCall(`/multi-sensor/stop/${nodeId}`, {
      method: 'POST',
    });
  },

  async startAllNodes() {
    return apiCall('/multi-sensor/start-all', {
      method: 'POST',
    });
  },

  async stopAllNodes() {
    return apiCall('/multi-sensor/stop-all', {
      method: 'POST',
    });
  },

  // Estado
  async getActiveNodes() {
    return apiCall('/multi-sensor/active');
  },

  async isNodeActive(nodeId) {
    return apiCall(`/multi-sensor/active/${nodeId}`);
  },

  // Datos
  async getAllSensorData() {
    return apiCall('/sensor-data/all');
  },

  async getLatestSensorData(limit = 10) {
    return apiCall(`/sensor-data/latest?limit=${limit}`);
  },
};