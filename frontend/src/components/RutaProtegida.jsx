
import { Navigate, Outlet, useLocation } from 'react-router-dom';

function RutaProtegida() {
  const location = useLocation();
  const token = localStorage.getItem('token');

  if (!token) {
    return (
      <Navigate
        to="/login"
        replace
        state={{ desde: location.pathname }}
      />
    );
  }

  return <Outlet />;
}

export default RutaProtegida;
