
import { useEffect, useState } from 'react';
import {
  conectarNotificaciones,
  suscribirseAGremio,
  cancelarSuscripcion,
  desconectarNotificaciones,
} from "../services/notificationServices";

function useNotificaciones(gremioId) {
  const [conectado, setConectado] = useState(false);
  const [notificaciones, setNotificaciones] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    const token = localStorage.getItem('token');

    if (!token || !gremioId) {
      setConectado(false);
      return;
    }

    let activo = true;

    const iniciarConexion = () => {
      conectarNotificaciones(
        token,
        () => {
          if (!activo) return;

          setConectado(true);
          setError('');

          suscribirseAGremio(gremioId, (notificacion) => {
            if (!activo) return;

            setNotificaciones((anteriores) => [
              notificacion,
              ...anteriores,
            ]);
          });
        },
        (mensajeError) => {
          if (!activo) return;

          setError(mensajeError);
          setConectado(false);
        }
      );
    };

    iniciarConexion();

    return () => {
      activo = false;
      setConectado(false);
      cancelarSuscripcion();
      desconectarNotificaciones();
    };
  }, [gremioId]);

  const limpiarNotificaciones = () => {
    setNotificaciones([]);
  };

  return {
    conectado,
    notificaciones,
    error,
    limpiarNotificaciones,
  };
}

export default useNotificaciones;
