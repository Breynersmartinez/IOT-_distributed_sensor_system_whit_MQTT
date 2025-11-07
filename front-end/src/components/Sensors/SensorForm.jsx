import React, { useState } from 'react';
import { Button } from '../Common/Button';
import { Alert } from '../Common/Alert';
import { validators } from '../../utils/validators';

export const SensorForm = ({ onSubmit, loading = false }) => {
  const [formData, setFormData] = useState({
    nodeId: '',
    nodeName: '',
    location: '',
    mqttTopic: '',
  });
  const [errors, setErrors] = useState({});

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    // Limpiar error cuando el usuario empieza a escribir
    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: '' }));
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    // Validar
    const newErrors = {};
    if (!validators.validateNodeId(formData.nodeId)) {
      newErrors.nodeId = 'ID del nodo es requerido';
    }
    if (!validators.validateNodeName(formData.nodeName)) {
      newErrors.nodeName = 'Nombre del nodo es requerido';
    }
    if (!validators.validateLocation(formData.location)) {
      newErrors.location = 'Ubicación es requerida';
    }
    if (!validators.validateMqttTopic(formData.mqttTopic)) {
      newErrors.mqttTopic = 'Tópico MQTT inválido (ej: sensors/office/temp)';
    }

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    onSubmit(formData);
    setFormData({
      nodeId: '',
      nodeName: '',
      location: '',
      mqttTopic: '',
    });
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div>
        <label className="block text-sm font-semibold text-white mb-2">
          ID del Nodo
        </label>
        <input
          type="text"
          name="nodeId"
          value={formData.nodeId}
          onChange={handleChange}
          placeholder="SENSOR-01"
          className="w-full bg-slate-700 text-white px-4 py-2 rounded border border-slate-600 focus:border-blue-500 outline-none"
        />
        {errors.nodeId && <Alert type="error" message={errors.nodeId} />}
      </div>

      <div>
        <label className="block text-sm font-semibold text-white mb-2">
          Nombre del Nodo
        </label>
        <input
          type="text"
          name="nodeName"
          value={formData.nodeName}
          onChange={handleChange}
          placeholder="Temperatura Oficina"
          className="w-full bg-slate-700 text-white px-4 py-2 rounded border border-slate-600 focus:border-blue-500 outline-none"
        />
        {errors.nodeName && <Alert type="error" message={errors.nodeName} />}
      </div>

      <div>
        <label className="block text-sm font-semibold text-white mb-2">
          Ubicación
        </label>
        <input
          type="text"
          name="location"
          value={formData.location}
          onChange={handleChange}
          placeholder="Piso 3, Oficina 302"
          className="w-full bg-slate-700 text-white px-4 py-2 rounded border border-slate-600 focus:border-blue-500 outline-none"
        />
        {errors.location && <Alert type="error" message={errors.location} />}
      </div>

      <div>
        <label className="block text-sm font-semibold text-white mb-2">
          Tópico MQTT
        </label>
        <input
          type="text"
          name="mqttTopic"
          value={formData.mqttTopic}
          onChange={handleChange}
          placeholder="sensors/office/temp-01"
          className="w-full bg-slate-700 text-white px-4 py-2 rounded border border-slate-600 focus:border-blue-500 outline-none"
        />
        {errors.mqttTopic && <Alert type="error" message={errors.mqttTopic} />}
      </div>

      <Button type="submit" variant="primary" loading={loading} className="w-full">
        Registrar Nodo
      </Button>
    </form>
  );
};