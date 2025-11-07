import React, { useState } from 'react';
import { Plus, RefreshCw } from 'lucide-react';
import { Button } from '../Common/Button';
import { Modal } from '../Common/Modal';
import { SensorForm } from './SensorForm';

export const SensorActions = ({ onRegister, onRefresh, loading = false }) => {
  const [showModal, setShowModal] = useState(false);

  const handleSubmit = async (formData) => {
    try {
      await onRegister(formData);
      setShowModal(false);
    } catch (error) {
      console.error('Error registrando nodo:', error);
    }
  };

  return (
    <>
      <div className="flex gap-4 flex-wrap mb-8">
        <Button
          variant="primary"
          onClick={() => setShowModal(true)}
          disabled={loading}
        >
          <Plus className="w-5 h-5" />
          Nuevo Nodo
        </Button>

        <Button
          variant="secondary"
          onClick={onRefresh}
          disabled={loading}
        >
          <RefreshCw className="w-5 h-5" />
          Actualizar
        </Button>
      </div>

      <Modal
        isOpen={showModal}
        title="Registrar Nuevo Nodo"
        onClose={() => setShowModal(false)}
        onSubmit={() => {}}
        submitLabel="Registrar"
      >
        <SensorForm onSubmit={handleSubmit} loading={loading} />
      </Modal>
    </>
  );
};