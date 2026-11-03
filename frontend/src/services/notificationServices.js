
import { Client } from '@stomp/stompjs';

const WS_URL = import.meta.env.VITE_WS_URL || 'ws://localhost:8080/ws-chemigram';

let cliente = null;
let suscripcionActual = null;

/**
 * Conecta al servidor WebSocket utilizando el JWT.
 */
export const conectarNotificaciones = (token, alConectar, alError) => {
  if (!token) {
    alError?.('No existe un token de autenticación.');
    return null;
  }

  // Evita crear conexiones duplicadas.
  if (cliente?.active) {
    return cliente;
  }

  cliente = new Client({
    brokerURL: WS_URL,

    connectHeaders: {
      Authorization: `Bearer ${token}`,
    },

    reconnectDelay: 5000,
    heartbeatIncoming: 10000,
    heartbeatOutgoing: 10000,

    onConnect: () => {
      console.log('Conexión WebSocket establecida.');
      alConectar?.();
    },

    onStompError: (frame) => {
      console.error(
        'Error STOMP:',
        frame.headers?.message || frame.body
      );

      alError?.(
        frame.headers?.message || 'Error en la conexión STOMP.'
      );
    },

    onWebSocketError: (error) => {
      console.error('Error WebSocket:', error);
      alError?.('No fue posible establecer la conexión WebSocket.');
    },

    onWebSocketClose: () => {
      console.log('Conexión WebSocket cerrada.');
    },
  });

  cliente.activate();

  return cliente;
};

/**
 * Se suscribe a las notificaciones de un gremio.
 */
export const suscribirseAGremio = (gremioId, alRecibir) => {
  if (!cliente?.connected) {
    console.error('Primero debes conectar el WebSocket.');
    return null;
  }

  // Evita mantener dos suscripciones activas desde este servicio.
  if (suscripcionActual) {
    suscripcionActual.unsubscribe();
    suscripcionActual = null;
  }

  suscripcionActual = cliente.subscribe(
    `/topic/gremio/${gremioId}`,
    (mensaje) => {
      try {
        const notificacion = JSON.parse(mensaje.body);
        alRecibir?.(notificacion);
      } catch (error) {
        console.error('No se pudo interpretar la notificación:', error);
      }
    }
  );

  console.log(`Suscrito al gremio ${gremioId}.`);

  return suscripcionActual;
};

/**
 * Cancela la suscripción actual.
 */
export const cancelarSuscripcion = () => {
  if (suscripcionActual) {
    suscripcionActual.unsubscribe();
    suscripcionActual = null;
  }
};

/**
 * Desconecta completamente el cliente.
 */
export const desconectarNotificaciones = async () => {
  cancelarSuscripcion();

  if (cliente) {
    await cliente.deactivate();
    cliente = null;
  }
};
