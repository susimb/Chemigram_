
import { Link } from 'react-router-dom';
import '../styles/Perfil.css';

function Perfil() {
  let usuario = null;

  try {
    const usuarioGuardado = localStorage.getItem('usuarioActual');
    usuario = usuarioGuardado ? JSON.parse(usuarioGuardado) : null;
  } catch {
    usuario = null;
  }

  if (!usuario) {
    return (
      <main className="perfil-page">
        <section className="perfil-panel">
          <p className="perfil-kicker">LIBER ALCHEMIAE</p>
          <h1>No se encontró tu registro</h1>
          <p>Inicia sesión para consultar tu perfil de alquimista.</p>
          <Link to="/login" className="perfil-button">
            Entrar al círculo ↗
          </Link>
        </section>
      </main>
    );
  }

  const nombre = usuario.nombre || 'Alquimista sin nombre';
  const inicial = nombre.charAt(0).toUpperCase();

  return (
    <main className="perfil-page">
      <header className="perfil-header">
        <Link to="/" className="perfil-brand">
          ☉ CHEMIGRAM
        </Link>

        <Link to="/" className="perfil-back">
          ← Volver al grimorio
        </Link>
      </header>

      <section className="perfil-panel">
        <p className="perfil-kicker">
          LIBER ALCHEMIAE · REGISTRO PERSONAL
        </p>

        <div className="perfil-emblema" aria-hidden="true">
          {inicial}
        </div>

        <h1>{nombre}</h1>

        <p className="perfil-description">
          Cada descubrimiento deja una huella en el grimorio.
        </p>

        <div className="perfil-divider">
          <span>✦</span>
        </div>

        <div className="perfil-datos">
          <article className="perfil-dato">
            <span>Correo electrónico</span>
            <p>{usuario.email || 'No registrado'}</p>
          </article>

          <article className="perfil-dato">
            <span>Especialidad alquímica</span>
            <p>{usuario.especialidad || 'Por descubrir'}</p>
          </article>

          <article className="perfil-dato">
            <span>Puntos de alquimia</span>
            <p>{usuario.puntosAlquimia ?? usuario.puntos ?? 0}</p>
          </article>

          <article className="perfil-dato">
            <span>Rareza</span>
            <p>{usuario.rarezaTotal ?? usuario.rareza ?? 0}</p>
          </article>
        </div>

        <p className="perfil-motto">
          PER SCIENTIAM AD MYSTERIUM
        </p>
      </section>
    </main>
  );
}

export default Perfil;
