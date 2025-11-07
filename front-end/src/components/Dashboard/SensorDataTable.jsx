import React from 'react';
import { Thermometer, Droplets } from 'lucide-react';
import { Card } from '../Common/Card';
import { Loading } from '../Common/Loading';
import { formatters } from '../../utils/formatters';

export const SensorDataTable = ({ data, loading = false }) => {
  if (loading) return <Loading />;

  if (data.length === 0) {
    return (
      <Card title="Últimos Registros de Sensores">
        <p className="text-slate-400 text-center py-8">No hay datos de sensores</p>
      </Card>
    );
  }

  return (
    <Card title={`Últimos Registros de Sensores (${data.length})`}>
      <div className="overflow-x-auto">
        <table className="w-full text-sm">
          <thead>
            <tr className="border-b border-slate-600">
              <th className="text-left py-3 px-4 text-slate-300 font-semibold">
                Timestamp
              </th>
              <th className="text-left py-3 px-4 text-slate-300 font-semibold">
                <span className="flex items-center gap-2">
                  <Thermometer className="w-4 h-4" /> Temperatura
                </span>
              </th>
              <th className="text-left py-3 px-4 text-slate-300 font-semibold">
                <span className="flex items-center gap-2">
                  <Droplets className="w-4 h-4" /> Humedad
                </span>
              </th>
            </tr>
          </thead>
          <tbody>
            {data.map((record, index) => (
              <tr
                key={index}
                className="border-b border-slate-700 hover:bg-slate-700 transition"
              >
                <td className="py-3 px-4 text-slate-300">
                  {formatters.formatTimestamp(record.timestamp)}
                </td>
                <td className="py-3 px-4">
                  <span className="bg-red-900 text-red-200 px-3 py-1 rounded-full text-sm font-semibold">
                    {formatters.formatTemperature(record.temperature)}
                  </span>
                </td>
                <td className="py-3 px-4">
                  <span className="bg-blue-900 text-blue-200 px-3 py-1 rounded-full text-sm font-semibold">
                    {formatters.formatHumidity(record.humidity)}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </Card>
  );
};