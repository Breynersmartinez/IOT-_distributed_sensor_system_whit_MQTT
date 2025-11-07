import React from 'react';
import { Play, Square, Trash2 } from 'lucide-react';
import { Button } from '../Common/Button';
import { Badge } from '../Common/Badge';

export const SensorCard = ({ node, isActive, onStart, onStop, onDelete, loading }) => {
  return (
    <div
      className={`p-4 rounded-lg border transition ${
        isActive
          ? 'bg-green-900 border-green-600'
          : 'bg-slate-700 border-slate-600'
      }`}
    >
      <div className="flex items-center justify-between mb-3">
        <div className="flex-1">
          <h3 className="text-lg font-bold text-white">{node.nodeName}</h3>
          <p className="text-sm text-slate-300">ID: {node.nodeId}</p>
          <p className="text-sm text-slate-400">📍 {node.location}</p>
          <p className="text-xs text-slate-500 mt-1">Tópico: {node.mqttTopic}</p>
        </div>

        <div className="flex gap-2 ml-4">
          {isActive ? (
            <Button
              size="sm"
              variant="danger"
              onClick={() => onStop(node.nodeId)}
              disabled={loading}
              title="Detener"
            >
              <Square className="w-4 h-4" />
            </Button>
          ) : (
            <Button
              size="sm"
              variant="success"
              onClick={() => onStart(node.nodeId)}
              disabled={loading}
              title="Iniciar"
            >
              <Play className="w-4 h-4" />
            </Button>
          )}
          <Button
            size="sm"
            variant="secondary"
            onClick={() => onDelete(node.nodeId)}
            disabled={loading}
            title="Eliminar"
          >
            <Trash2 className="w-4 h-4" />
          </Button>
        </div>
      </div>

      <div className="flex items-center gap-2">
        <span
          className={`inline-block w-3 h-3 rounded-full ${
            isActive ? 'bg-green-400 animate-pulse' : 'bg-slate-500'
          }`}
        />
        <Badge variant={isActive ? 'success' : 'warning'}>
          {isActive ? 'Transmitiendo' : 'Inactivo'}
        </Badge>
      </div>
    </div>
  );
};