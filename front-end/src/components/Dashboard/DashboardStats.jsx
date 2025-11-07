import React from 'react';
import { StatsCard } from './StatsCard';
import { Activity, Zap } from 'lucide-react';

export const DashboardStats = ({ totalNodes, activeNodesCount, recordsCount }) => {
  return (
    <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
      <StatsCard
        title="NODOS REGISTRADOS"
        value={totalNodes}
        icon={Activity}
        gradient="from-blue-900 to-blue-800 border border-blue-700"
      />
      <StatsCard
        title="NODOS ACTIVOS"
        value={activeNodesCount}
        icon={Zap}
        gradient="from-green-900 to-green-800 border border-green-700"
      />
      <StatsCard
        title="REGISTROS TOTALES"
        value={recordsCount}
        icon={Activity}
        gradient="from-purple-900 to-purple-800 border border-purple-700"
      />
    </div>
  );
};
