import React, { useState, useEffect } from 'react';
import { Activity, Droplets, Thermometer, Power, Plus, Trash2, Play, Square, RefreshCw } from 'lucide-react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

// API Configuration
const API_BASE_URL = import.meta.env.VITE_API_URL;

export default function SensorDashboard() {
  const [latestData, setLatestData] = useState([]);
  const [sensorNodes, setSensorNodes] = useState([]);
  const [activeSensors, setActiveSensors] = useState([]);
  const [loading, setLoading] = useState(false);
  const [streaming, setStreaming] = useState(false);
  const [showNewNodeForm, setShowNewNodeForm] = useState(false);
  const [newNode, setNewNode] = useState({ nodeId: '', nodeName: '', location: '', mqttTopic: '' });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  // Fetch latest sensor data
  const fetchLatestData = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/sensor-data/latest?limit=20`);
      if (response.ok) {
        const data = await response.json();
        setLatestData(data);
      }
    } catch (err) {
      console.error('Error fetching data:', err);
    }
  };

  // Fetch all sensor nodes
  const fetchSensorNodes = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/multi-sensor/nodes`);
      if (response.ok) {
        const data = await response.json();
        setSensorNodes(data);
      }
    } catch (err) {
      console.error('Error fetching nodes:', err);
    }
  };

  // Fetch active sensors
  const fetchActiveSensors = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/multi-sensor/active`);
      if (response.ok) {
        const data = await response.json();
        setActiveSensors(data);
      }
    } catch (err) {
      console.error('Error fetching active sensors:', err);
    }
  };

  // Initial load and polling
  useEffect(() => {
    fetchLatestData();
    fetchSensorNodes();
    fetchActiveSensors();

    const interval = setInterval(() => {
      fetchLatestData();
      fetchActiveSensors();
    }, 5000);

    return () => clearInterval(interval);
  }, []);

  // Start streaming
  const handleStartStreaming = async () => {
    try {
      setLoading(true);
      const response = await fetch(`${API_BASE_URL}/sensor/start`, { method: 'POST' });
      if (response.ok) {
        setStreaming(true);
        setSuccess('Transmisión iniciada');
        setTimeout(() => setSuccess(''), 3000);
      }
    } catch (err) {
      setError('Error al iniciar transmisión: ' + err.message);
      setTimeout(() => setError(''), 3000);
    } finally {
      setLoading(false);
    }
  };

  // Stop streaming
  const handleStopStreaming = async () => {
    try {
      setLoading(true);
      const response = await fetch(`${API_BASE_URL}/sensor/stop`, { method: 'POST' });
      if (response.ok) {
        setStreaming(false);
        setSuccess('Transmisión detenida');
        setTimeout(() => setSuccess(''), 3000);
      }
    } catch (err) {
      setError('Error al detener transmisión: ' + err.message);
      setTimeout(() => setError(''), 3000);
    } finally {
      setLoading(false);
    }
  };

  // Register new sensor node
  const handleRegisterNode = async () => {
    if (!newNode.nodeId || !newNode.nodeName || !newNode.location || !newNode.mqttTopic) {
      setError('Todos los campos son requeridos');
      setTimeout(() => setError(''), 3000);
      return;
    }

    try {
      setLoading(true);
      const response = await fetch(`${API_BASE_URL}/multi-sensor/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newNode),
      });

      if (response.ok) {
        setSuccess('Nodo registrado exitosamente');
        setNewNode({ nodeId: '', nodeName: '', location: '', mqttTopic: '' });
        setShowNewNodeForm(false);
        fetchSensorNodes();
        setTimeout(() => setSuccess(''), 3000);
      } else {
        setError('Error al registrar el nodo');
        setTimeout(() => setError(''), 3000);
      }
    } catch (err) {
      setError('Error al registrar nodo: ' + err.message);
      setTimeout(() => setError(''), 3000);
    } finally {
      setLoading(false);
    }
  };

  // Start specific node
  const handleStartNode = async (nodeId) => {
    try {
      const response = await fetch(`${API_BASE_URL}/multi-sensor/start/${nodeId}`, { method: 'POST' });
      if (response.ok) {
        setSuccess(`Nodo ${nodeId} iniciado`);
        fetchActiveSensors();
        setTimeout(() => setSuccess(''), 3000);
      }
    } catch (err) {
      setError('Error: ' + err.message);
      setTimeout(() => setError(''), 3000);
    }
  };

  // Stop specific node
  const handleStopNode = async (nodeId) => {
    try {
      const response = await fetch(`${API_BASE_URL}/multi-sensor/stop/${nodeId}`, { method: 'POST' });
      if (response.ok) {
        setSuccess(`Nodo ${nodeId} detenido`);
        fetchActiveSensors();
        setTimeout(() => setSuccess(''), 3000);
      }
    } catch (err) {
      setError('Error: ' + err.message);
      setTimeout(() => setError(''), 3000);
    }
  };

  // Delete node
  const handleDeleteNode = async (nodeId) => {
    if (!confirm(`¿Eliminar nodo ${nodeId}?`)) return;
    try {
      const response = await fetch(`${API_BASE_URL}/multi-sensor/nodes/${nodeId}`, { method: 'DELETE' });
      if (response.ok) {
        setSuccess('Nodo eliminado');
        fetchSensorNodes();
        setTimeout(() => setSuccess(''), 3000);
      }
    } catch (err) {
      setError('Error: ' + err.message);
      setTimeout(() => setError(''), 3000);
    }
  };

  const chartData = latestData.slice(-10).map(d => ({
    timestamp: d.timestamp?.substring(11, 16) || 'N/A',
    temp: parseFloat(d.temperature),
    humidity: parseFloat(d.humidity),
  }));

  const avgTemp = latestData.length > 0 
    ? (latestData.reduce((sum, d) => sum + parseFloat(d.temperature), 0) / latestData.length).toFixed(2)
    : '0';
  
  const avgHumidity = latestData.length > 0
    ? (latestData.reduce((sum, d) => sum + parseFloat(d.humidity), 0) / latestData.length).toFixed(2)
    : '0';

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-900 via-gray-800 to-gray-900">
      {/* Header */}
      <header className="bg-gradient-to-r from-blue-600 to-blue-800 shadow-xl">
        <div className="max-w-7xl mx-auto px-4 py-6">
          <div className="flex justify-between items-center">
            <div className="flex items-center gap-3">
              <Activity className="w-8 h-8 text-white" />
              <h1 className="text-3xl font-bold text-white">IoT Sensor Dashboard</h1>
            </div>
            <div className="flex gap-2">
              <button
                onClick={handleStartStreaming}
                disabled={streaming || loading}
                className="flex items-center gap-2 px-4 py-2 bg-green-500 hover:bg-green-600 disabled:bg-gray-500 text-white rounded-lg transition"
              >
                <Play className="w-4 h-4" /> Iniciar
              </button>
              <button
                onClick={handleStopStreaming}
                disabled={!streaming || loading}
                className="flex items-center gap-2 px-4 py-2 bg-red-500 hover:bg-red-600 disabled:bg-gray-500 text-white rounded-lg transition"
              >
                <Square className="w-4 h-4" /> Detener
              </button>
            </div>
          </div>
        </div>
      </header>

      {/* Alerts */}
      {error && (
        <div className="bg-red-500 text-white px-4 py-3 mx-4 mt-4 rounded-lg flex justify-between items-center">
          <span>{error}</span>
          <button onClick={() => setError('')} className="text-xl font-bold">&times;</button>
        </div>
      )}
      {success && (
        <div className="bg-green-500 text-white px-4 py-3 mx-4 mt-4 rounded-lg flex justify-between items-center">
          <span>{success}</span>
          <button onClick={() => setSuccess('')} className="text-xl font-bold">&times;</button>
        </div>
      )}

      <main className="max-w-7xl mx-auto px-4 py-8 space-y-8">
        {/* Stats */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div className="bg-gradient-to-br from-blue-500 to-blue-600 rounded-xl p-6 text-white shadow-lg">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-blue-100 text-sm font-semibold">Estado</p>
                <p className="text-3xl font-bold">{streaming ? 'Activo' : 'Inactivo'}</p>
              </div>
              <Power className="w-10 h-10 opacity-50" />
            </div>
          </div>

          <div className="bg-gradient-to-br from-orange-500 to-orange-600 rounded-xl p-6 text-white shadow-lg">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-orange-100 text-sm font-semibold">Temp. Promedio</p>
                <p className="text-3xl font-bold">{avgTemp}°C</p>
              </div>
              <Thermometer className="w-10 h-10 opacity-50" />
            </div>
          </div>

          <div className="bg-gradient-to-br from-cyan-500 to-cyan-600 rounded-xl p-6 text-white shadow-lg">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-cyan-100 text-sm font-semibold">Humedad Promedio</p>
                <p className="text-3xl font-bold">{avgHumidity}%</p>
              </div>
              <Droplets className="w-10 h-10 opacity-50" />
            </div>
          </div>

          <div className="bg-gradient-to-br from-purple-500 to-purple-600 rounded-xl p-6 text-white shadow-lg">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-purple-100 text-sm font-semibold">Nodos Activos</p>
                <p className="text-3xl font-bold">{activeSensors.length}</p>
              </div>
              <Activity className="w-10 h-10 opacity-50" />
            </div>
          </div>
        </div>

        {/* Chart */}
        {chartData.length > 0 && (
          <div className="bg-gray-800 rounded-xl p-6 shadow-lg border border-gray-700">
            <h2 className="text-xl font-bold text-white mb-4 flex items-center gap-2">
              <RefreshCw className="w-5 h-5" /> Gráfico de Datos en Tiempo Real
            </h2>
            <ResponsiveContainer width="100%" height={300}>
              <LineChart data={chartData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#444" />
                <XAxis dataKey="timestamp" stroke="#888" />
                <YAxis stroke="#888" yAxisId="left" />
                <YAxis stroke="#888" yAxisId="right" orientation="right" />
                <Tooltip contentStyle={{ backgroundColor: '#222', border: '1px solid #444' }} />
                <Legend />
                <Line yAxisId="left" type="monotone" dataKey="temp" stroke="#ff7300" name="Temperatura (°C)" />
                <Line yAxisId="right" type="monotone" dataKey="humidity" stroke="#00d4ff" name="Humedad (%)" />
              </LineChart>
            </ResponsiveContainer>
          </div>
        )}

        {/* Sensor Nodes */}
        <div className="bg-gray-800 rounded-xl p-6 shadow-lg border border-gray-700">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-xl font-bold text-white">Nodos Sensores</h2>
            <button
              onClick={() => setShowNewNodeForm(!showNewNodeForm)}
              className="flex items-center gap-2 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg transition"
            >
              <Plus className="w-4 h-4" /> Nuevo Nodo
            </button>
          </div>

          {showNewNodeForm && (
            <div className="mb-6 p-4 bg-gray-700 rounded-lg border border-gray-600">
              <div className="grid grid-cols-2 gap-4 mb-4">
                <input
                  type="text"
                  placeholder="ID del Nodo"
                  value={newNode.nodeId}
                  onChange={(e) => setNewNode({ ...newNode, nodeId: e.target.value })}
                  className="px-3 py-2 bg-gray-600 border border-gray-500 text-white rounded-lg focus:outline-none focus:border-blue-400"
                />
                <input
                  type="text"
                  placeholder="Nombre del Nodo"
                  value={newNode.nodeName}
                  onChange={(e) => setNewNode({ ...newNode, nodeName: e.target.value })}
                  className="px-3 py-2 bg-gray-600 border border-gray-500 text-white rounded-lg focus:outline-none focus:border-blue-400"
                />
                <input
                  type="text"
                  placeholder="Ubicación"
                  value={newNode.location}
                  onChange={(e) => setNewNode({ ...newNode, location: e.target.value })}
                  className="px-3 py-2 bg-gray-600 border border-gray-500 text-white rounded-lg focus:outline-none focus:border-blue-400"
                />
                <input
                  type="text"
                  placeholder="Tópico MQTT"
                  value={newNode.mqttTopic}
                  onChange={(e) => setNewNode({ ...newNode, mqttTopic: e.target.value })}
                  className="px-3 py-2 bg-gray-600 border border-gray-500 text-white rounded-lg focus:outline-none focus:border-blue-400"
                />
              </div>
              <button
                onClick={handleRegisterNode}
                disabled={loading}
                className="w-full px-4 py-2 bg-green-600 hover:bg-green-700 disabled:bg-gray-500 text-white rounded-lg transition font-semibold"
              >
                Registrar Nodo
              </button>
            </div>
          )}

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {sensorNodes.map((node) => {
              const isActive = activeSensors.some(s => s.nodeId === node.nodeId);
              return (
                <div key={node.nodeId} className="bg-gray-700 border border-gray-600 rounded-lg p-4">
                  <div className="flex justify-between items-start mb-3">
                    <div>
                      <h3 className="text-lg font-bold text-white">{node.nodeName}</h3>
                      <p className="text-sm text-gray-400">{node.nodeId}</p>
                    </div>
                    <div className={`w-3 h-3 rounded-full ${isActive ? 'bg-green-500' : 'bg-red-500'}`} />
                  </div>
                  <p className="text-sm text-gray-300 mb-2">📍 {node.location}</p>
                  <p className="text-xs text-gray-400 mb-4 break-all">📡 {node.mqttTopic}</p>
                  <div className="flex gap-2">
                    <button
                      onClick={() => handleStartNode(node.nodeId)}
                      disabled={isActive}
                      className="flex-1 px-3 py-2 bg-green-600 hover:bg-green-700 disabled:bg-gray-600 text-white text-sm rounded-lg transition flex items-center justify-center gap-1"
                    >
                      <Play className="w-3 h-3" /> Iniciar
                    </button>
                    <button
                      onClick={() => handleStopNode(node.nodeId)}
                      disabled={!isActive}
                      className="flex-1 px-3 py-2 bg-red-600 hover:bg-red-700 disabled:bg-gray-600 text-white text-sm rounded-lg transition flex items-center justify-center gap-1"
                    >
                      <Square className="w-3 h-3" /> Detener
                    </button>
                    <button
                      onClick={() => handleDeleteNode(node.nodeId)}
                      className="px-3 py-2 bg-gray-600 hover:bg-gray-500 text-white text-sm rounded-lg transition"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        {/* Latest Data */}
        <div className="bg-gray-800 rounded-xl p-6 shadow-lg border border-gray-700">
          <h2 className="text-xl font-bold text-white mb-4">Últimos Registros</h2>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead className="bg-gray-700">
                <tr>
                  <th className="px-4 py-2 text-left text-gray-300">Timestamp</th>
                  <th className="px-4 py-2 text-left text-gray-300">Temperatura</th>
                  <th className="px-4 py-2 text-left text-gray-300">Humedad</th>
                </tr>
              </thead>
              <tbody>
                {latestData.slice(0, 10).map((data, idx) => (
                  <tr key={idx} className="border-t border-gray-700 hover:bg-gray-700 transition">
                    <td className="px-4 py-3 text-gray-300">{data.timestamp}</td>
                    <td className="px-4 py-3 text-orange-400 font-semibold">{parseFloat(data.temperature).toFixed(2)}°C</td>
                    <td className="px-4 py-3 text-cyan-400 font-semibold">{parseFloat(data.humidity).toFixed(2)}%</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </main>
    </div>
  );
}