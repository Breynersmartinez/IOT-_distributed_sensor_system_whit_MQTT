import { useState, useEffect, useCallback } from 'react';
import { sensorService } from '../services/sensorService';
import { POLLING_INTERVALS } from '../utils/constants';

export const useSensors = () => {
  const [nodes, setNodes] = useState([]);
  const [activeNodes, setActiveNodes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchNodes = useCallback(async () => {
    try {
      setLoading(true);
      const data = await sensorService.getAllNodes();
      setNodes(data || []);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, []);

  const fetchActiveNodes = useCallback(async () => {
    try {
      const data = await sensorService.getActiveNodes();
      setActiveNodes(data ? data.map(n => n.nodeId) : []);
    } catch (err) {
      setError(err.message);
    }
  }, []);

  const registerNode = useCallback(async (nodeData) => {
    try {
      await sensorService.registerNode(nodeData);
      await fetchNodes();
    } catch (err) {
      setError(err.message);
      throw err;
    }
  }, [fetchNodes]);

  const deleteNode = useCallback(async (nodeId) => {
    try {
      await sensorService.deleteNode(nodeId);
      await fetchNodes();
    } catch (err) {
      setError(err.message);
      throw err;
    }
  }, [fetchNodes]);

  const startNode = useCallback(async (nodeId) => {
    try {
      await sensorService.startNode(nodeId);
      await fetchActiveNodes();
    } catch (err) {
      setError(err.message);
      throw err;
    }
  }, [fetchActiveNodes]);

  const stopNode = useCallback(async (nodeId) => {
    try {
      await sensorService.stopNode(nodeId);
      await fetchActiveNodes();
    } catch (err) {
      setError(err.message);
      throw err;
    }
  }, [fetchActiveNodes]);

  // Cargar datos al montar
  useEffect(() => {
    fetchNodes();
    fetchActiveNodes();

    // Polling
    const interval = setInterval(() => {
      fetchActiveNodes();
    }, POLLING_INTERVALS.SENSOR_DATA);

    return () => clearInterval(interval);
  }, [fetchNodes, fetchActiveNodes]);

  return {
    nodes,
    activeNodes,
    loading,
    error,
    fetchNodes,
    fetchActiveNodes,
    registerNode,
    deleteNode,
    startNode,
    stopNode,
  };
};