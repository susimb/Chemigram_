
import { useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import api from '../services/api';
import '../styles/Login.css';

function Login() {
  const navigate = useNavigate();
  const location = useLocation();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [cargando, setCargando] = useState(false);

  const mensajeRegistro = location.state?.mensaje;

  const iniciarSesion = async (e) => {
    e.preventDefault();
    setError('');
    setCargando(true);

    try {
      const respuesta = await api.post('/api/usuarios/login', {
        email,
        password,
      });

      const { token, usuario } = respuesta.data;

      if (!token || !usuario) {
        setError('El servidor no devolvió los datos de sesión esperados.');
        return;
      }

      localStorage.setItem('token', token);
      localStorage.setItem('usuarioActual', JSON.stringify(usuario));

      navigate('/');
    } catch (err) {
      if (err.response) {
        setError(
          err.response.status === 401
            ? 'El correo o la contraseña no son correctos.'
            : err.response.data?.message ||
              'No fue posible iniciar sesión.'
        );
      } else {
        setError(
          'No se pudo conectar con el servidor. Comprueba que el backend esté funcionando.'
        );
      }
    } finally {
      setCargando(false);
    }
  };

  return (
    <main className="login-page">
      <Link to="/" className="login-brand">
        <span aria-hidden="true">☉</span>
        CHEMIGRAM
      </Link>

      <section className="login-panel">
        <div className="login-emblema" aria-hidden="true">
          ⚗
        </div>

        <p className="login-kicker">LIBER ALCHEMIAE · FOLIO V</p>

        <h1>Regresa al círculo</h1>

        <p className="login-description">
          El grimorio conserva sus secretos para quienes conocen
          el camino de regreso.
        </p>

        {mensajeRegistro && (
          <p className="login-success" role="status">
            {mensajeRegistro}
          </p>
        )}

        <form onSubmit={iniciarSesion}>
          <label htmlFor="login-email">Correo electrónico</label>
          <input
            id="login-email"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="alquimista@ejemplo.com"
            autoComplete="email"
            required
          />

          <label htmlFor="login-password">Contraseña</label>
          <input
            id="login-password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Tu contraseña secreta"
            autoComplete="current-password"
            required
          />

          {error && (
            <p className="login-error" role="alert">
              {error}
            </p>
          )}

          <button
            className="login-submit"
            type="submit"
            disabled={cargando}
          >
            {cargando ? 'Abriendo el grimorio...' : 'Entrar al círculo ↗'}
          </button>
        </form>

        <p className="login-register">
          ¿Aún no eres alquimista?{' '}
          <Link to="/registro">Inscríbete aquí</Link>
        </p>

        <p className="login-motto">
          PER SCIENTIAM AD MYSTERIUM
        </p>
      </section>
    </main>
  );
}

export default Login;
