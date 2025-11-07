export const Alert = ({ type = 'info', message, onClose }) => {
  const alertColors = {
    info: 'bg-blue-900 border-blue-700 text-blue-200',
    success: 'bg-green-900 border-green-700 text-green-200',
    warning: 'bg-yellow-900 border-yellow-700 text-yellow-200',
    error: 'bg-red-900 border-red-700 text-red-200',
  };

  return (
    <div className={`border rounded-lg p-4 ${alertColors[type]}`}>
      <div className="flex justify-between items-center">
        <p>{message}</p>
        {onClose && (
          <button onClick={onClose} className="text-xl font-bold">
            ×
          </button>
        )}
      </div>
    </div>
  );
};