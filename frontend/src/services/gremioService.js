
import api from './api';

export const listarGremios = async () => {
  const response = await api.get('/api/gremios');
  return response.data;
};

export const crearGremio = async (datos) => {
  const response = await api.post('/api/gremios', null, {
    params: {
      nombre: datos.nombre,
      descripcion: datos.descripcion,
      icono: datos.icono,
    },
  });

  return response.data;
};

export const listarMiembros = async (gremioId) => {
  const response = await api.get(
    `/api/gremios/${gremioId}/miembros`
  );

  return response.data;
};

export const unirseAGremio = async (gremioId) => {
  const response = await api.post(
    `/api/gremios/${gremioId}/miembros`
  );

  return response.data;
};

export const asignarCatador = async (gremioId, usuarioId) => {
  const response = await api.put(
    `/api/gremios/${gremioId}/catador/${usuarioId}`
  );

  return response.data;
};

export const retirarCatador = async (gremioId) => {
  await api.delete(`/api/gremios/${gremioId}/catador`);
};
