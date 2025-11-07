export const formatters = {
  formatTemperature: (temp) => {
    return typeof temp === 'number' ? `${temp.toFixed(2)}°C` : 'N/A';
  },

  formatHumidity: (humidity) => {
    return typeof humidity === 'number' ? `${humidity.toFixed(2)}%` : 'N/A';
  },

  formatTimestamp: (timestamp) => {
    if (!timestamp) return 'N/A';
    try {
      return new Date(timestamp).toLocaleString('es-ES');
    } catch {
      return timestamp;
    }
  },

  formatBytes: (bytes) => {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return Math.round((bytes / Math.pow(k, i)) * 100) / 100 + ' ' + sizes[i];
  },
};

