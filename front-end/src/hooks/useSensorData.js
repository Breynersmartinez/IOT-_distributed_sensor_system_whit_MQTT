import { useState, useEffect, useCallback } from 'react';
import { sensorService } from '../services/sensorService';
import { POLLING_INTERVALS } from '../utils/constants';

export const useSensorData = (limit = 10) => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchData = useCallback(async () => {
    try {
      setLoading(true);
      const result = await sensorService.getLatestSensorData(limit);
      setData(result || []);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [limit]);

  useEffect(() => {
    fetchData();

    // Polling
    const interval = setInterval(fetchData, POLLING_INTERVALS.SENSOR_DATA);

    return () => clearInterval(interval);
  }, [fetchData]);

  return { data, loading, error, refetch: fetchData };
};