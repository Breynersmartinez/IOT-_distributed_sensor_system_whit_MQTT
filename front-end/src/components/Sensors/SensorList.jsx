import React from 'react';
import { SensorCard } from './SensorCard';
import { Loading } from '../Common/Loading';
import { Card } from '../Common/Card';

export const SensorList = ({
  nodes,
  activeNodes,
  loading,
  onStart,
  onStop,
  onDelete,
}) => {
  if (loading) return <Loading />;

  if (nodes.length === 0) {
    return (
      <Card title="Nodos Sensores (0)">
        <p className="text-slate-400 text-center py-8">No hay nodos registrados</p>
      </Card>
    );
  }

  return (
    <Card title={`Nodos Sensores (${nodes.length})`}>
      <div className="space-y-3 max-h-96 overflow-y-auto">
        {nodes.map((node) => (
          <SensorCard
            key={node.nodeId}
            node={node}
            isActive={activeNodes.includes(node.nodeId)}
            onStart={onStart}
            onStop={onStop}
            onDelete={onDelete}
            loading={loading}
          />
        ))}
      </div>
    </Card>
  );
};