import React from 'react';
import { Thermometer } from 'lucide-react';

export const Header = () => {
  return (
    <header className="bg-gradient-to-r from-slate-900 to-slate-800 border-b border-slate-700 sticky top-0 z-40">
      <div className="max-w-7xl mx-auto px-8 py-6">
        <div className="flex items-center gap-3">
          <div className="w-12 h-12 bg-blue-500 rounded-lg flex items-center justify-center">
            <Thermometer className="w-6 h-6 text-white" />
          </div>
          <div>
            <h1 className="text-3xl font-bold text-white">IoT MQTT Dashboard</h1>
            <p className="text-slate-400 text-sm">
              Sistema de Sensores Distribuidos en Tiempo Real
            </p>
          </div>
        </div>
      </div>
    </header>
  );
};