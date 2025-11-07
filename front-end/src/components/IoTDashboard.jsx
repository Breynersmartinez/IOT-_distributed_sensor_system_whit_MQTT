import React, { useState } from 'react';
import { Header } from './Layout/Header';
import { Footer } from './Layout/Footer';
import { SensorActions } from './Sensors/SensorActions';
import { SensorList } from './Sensors/SensorList';
import { DashboardStats } from './Dashboard/DashboardStats';
import { SensorDataTable } from './Dashboard/SensorDataTable';
import { Alert } from './Common/Alert';
import { useSensors } from '../hooks/useSensors';
import { useSensorData } from '../hooks/useSensorData';

export const IoTDashboard = () => {
  const [error, setError] = useState(null);
  const [actionLoading, setActionLoading] = useState(false);

  // Hooks para sensores
  const {
    nodes,
    activeNodes,
    loading: sensorsLoading,
    registerNode,
    deleteNode,
    startNode,
    stopNode,
    fetchNodes,
    fetchActiveNodes,
  } = useSensors();

  // Hook para datos de sensores
  const { data: sensorData, loading: dataLoading, refetch: refetchData } = useSensorData(10);

  // Handlers
  const handleRegisterNode = async (formData) => {
    try {
      setActionLoading(true);
      await registerNode(formData);
      setError(null);
    } catch (err) {
      setError(`Error al registrar nodo: ${err.message}`);
    } finally {
      setActionLoading(false);
    }
  };

  const handleStartNode = async (nodeId) => {
    try {
      setActionLoading(true);
      await startNode(nodeId);
      setError(null);
    } catch (err) {
      setError(`Error al iniciar nodo: ${err.message}`);
    } finally {
      setActionLoading(false);
    }
  };

  const handleStopNode = async (nodeId) => {
    try {
      setActionLoading(true);
      await stopNode(nodeId);
      setError(null);
    } catch (err) {
      setError(`Error al detener nodo: ${err.message}`);
    } finally {
      setActionLoading(false);
    }
  };

  const handleDeleteNode = async (nodeId) => {
    if (!window.confirm(`¿Estás seguro de que deseas eliminar el nodo ${nodeId}?`)) {
      return;
    }

    try {
      setActionLoading(true);
      await deleteNode(nodeId);
      setError(null);
    } catch (err) {
      setError(`Error al eliminar nodo: ${err.message}`);
    } finally {
      setActionLoading(false);
    }
  };

  const handleRefresh = async () => {
    try {
      setActionLoading(true);
      await Promise.all([fetchNodes(), fetchActiveNodes(), refetchData()]);
      setError(null);
    } catch (err) {
      setError(`Error al actualizar: ${err.message}`);
    } finally {
      setActionLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 flex flex-col">
      {/* Header */}
      <Header />

      {/* Contenido Principal */}
      <main className="flex-1 max-w-7xl mx-auto w-full px-8 py-12">
        {/* Errores */}
        {error && (
          <div className="mb-6">
            <Alert type="error" message={error} onClose={() => setError(null)} />
          </div>
        )}

        {/* Acciones */}
        <SensorActions
          onRegister={handleRegisterNode}
          onRefresh={handleRefresh}
          loading={actionLoading}
        />

        {/* Grid Principal */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 mb-8">
          {/* Panel Izquierdo - Nodos */}
          <div className="lg:col-span-2">
            <SensorList
              nodes={nodes}
              activeNodes={activeNodes}
              loading={sensorsLoading}
              onStart={handleStartNode}
              onStop={handleStopNode}
              onDelete={handleDeleteNode}
            />
          </div>

          {/* Panel Derecho - Estadísticas */}
          <div>
            <DashboardStats
              totalNodes={nodes.length}
              activeNodesCount={activeNodes.length}
              recordsCount={sensorData.length}
            />
          </div>
        </div>

        {/* Tabla de Datos */}
        <SensorDataTable data={sensorData} loading={dataLoading} />
      </main>

      {/* Footer */}
      <Footer />
    </div>
  );
};
