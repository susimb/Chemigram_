
import api from './api';

export const obtenerRanking = async () => {
  const response = await api.get('/api/ranking');
  return response.data;
};
