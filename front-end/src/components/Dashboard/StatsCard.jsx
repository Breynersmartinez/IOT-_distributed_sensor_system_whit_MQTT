import { Card } from '../Common/Card';

export const StatsCard = ({ title, value, icon: Icon, gradient = 'from-blue-900 to-blue-800' }) => {
  return (
    <Card className={`bg-gradient-to-br ${gradient} border-0`}>
      <div className="flex items-center justify-between">
        <div>
          <p className="text-sm font-semibold text-slate-300 mb-2">{title}</p>
          <p className="text-4xl font-bold text-white">{value}</p>
        </div>
        {Icon && <Icon className="w-12 h-12 text-white opacity-20" />}
      </div>
    </Card>
  );
};