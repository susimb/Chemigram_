
import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../services/api';
import '../styles/Registro.css';

function Registro() {
  const navigate = useNavigate();

  const [formulario, setFormulario] = useState({
    nombre: '',
    email: '',
    password: '',
    especialidad: '',
  });

  const [error, setError] = useState('');
  const [cargando, setCargando] = useState(false);

  const actualizarCampo = (e) => {
    const { name, value } = e.target;

    setFormulario((actual) => ({
      ...actual,
      [name]: value,
    }));
  };

  const registrarAlquimista = async (e) => {
    e.preventDefault();
    setError('');

    if (formulario.password.length < 8) {
      setError('La contraseña debe tener al menos 8 caracteres.');
      return;
    }

    setCargando(true);

    try {
      await api.post('/api/usuarios', formulario);

      navigate('/login', {
        state: { mensaje: 'Tu registro fue exitoso. Ya puedes entrar al círculo.' },
      });
    } catch (err) {
      const mensajeServidor = err.response?.data?.message;

      setError(
        mensajeServidor ||
        'No fue posible completar el registro. Comprueba los datos e inténtalo de nuevo.'
      );
    } finally {
      setCargando(false);
    }
  };

  return (
    <main className="registro-page">
      <Link to="/" className="registro-brand">
        <span>☉</span>
        CHEMIGRAM
      </Link>

      <section className="registro-panel">
        <div className="registro-emblema" aria-hidden="true">
          ⚗
        </div>

        <p className="registro-kicker">LIBER ALCHEMIAE · FOLIO IV</p>

        <h1>Inicia tu travesía</h1>

        <p className="registro-descripcion">
          Todo gran descubrimiento comienza con quien se atreve a buscarlo.
          Inscríbete en el grimorio.
        </p>

        <form onSubmit={registrarAlquimista}>
          <label htmlFor="nombre">Nombre de alquimista</label>
          <input
            id="nombre"
            name="nombre"
            type="text"
            value={formulario.nombre}
            onChange={actualizarCampo}
            placeholder="¿Cómo te conocerá el círculo?"
            autoComplete="name"
            required
          />

          <label htmlFor="email">Correo electrónico</label>
          <input
            id="email"
            name="email"
            type="email"
            value={formulario.email}
            onChange={actualizarCampo}
            placeholder="alquimista@ejemplo.com"
            autoComplete="email"
            required
          />

          <label htmlFor="especialidad">Especialidad alquímica</label>
          <select
            id="especialidad"
            name="especialidad"
            value={formulario.especialidad}
            onChange={actualizarCampo}
            required
          >
            <option value="">Elige tu camino</option>
            <option value="Herbología">Herbología</option>
            <option value="Transmutación">Transmutación</option>
            <option value="Elixires">Elixires</option>
            <option value="Mineralogía">Mineralogía</option>
            <option value="Astroalquimia">Astroalquimia</option>
          </select>

          <label htmlFor="password">Contraseña</label>
          <input
            id="password"
            name="password"
            type="password"
            value={formulario.password}
            onChange={actualizarCampo}
            placeholder="Mínimo 8 caracteres"
            autoComplete="new-password"
            minLength={8}
            required
          />

          {error && (
            <p className="registro-error" role="alert">
              {error}
            </p>
          )}

          <button
            className="registro-submit"
            type="submit"
            disabled={cargando}
          >
            {cargando ? 'Inscribiendo...' : 'Inscribirme en el grimorio ↗'}
          </button>
        </form>

        <p className="registro-login">
          ¿Ya perteneces al círculo?{' '}
          <Link to="/login">Inicia sesión</Link>
        </p>

        <p className="registro-motto">
          PER SCIENTIAM AD MYSTERIUM
        </p>
      </section>
    </main>
  );
}

export default Registro;
