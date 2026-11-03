import '../styles/home.css';
import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';


function Home() {
  const navigate = useNavigate();
const [usuario, setUsuario] = useState(null);

useEffect(() => {
  const usuarioGuardado = localStorage.getItem('usuarioActual');

  if (usuarioGuardado) {
    try {
      setUsuario(JSON.parse(usuarioGuardado));
    } catch {
      localStorage.removeItem('usuarioActual');
      setUsuario(null);
    }
  }
}, []);

const cerrarSesion = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('usuarioActual');
  setUsuario(null);
  navigate('/');
};
  return (
    <main className="home grimoire">

      {/* Navegación */}
      <header className="grimoire-header">
        <Link to="/" className="grimoire-brand">
          <span className="brand-mark">☉</span>
          <span>CHEMIGRAM</span>
        </Link>

        <nav className="grimoire-nav">
          <Link to="/gremios">Los gremios</Link>
          <Link to="/ranking">Los maestros</Link>
          {usuario ? (
            <>
            <Link to="/perfil" className="nav-enter">
            {usuario.nombre || usuario.email}
          </Link>
          <button type="button" className="nav-enter nav-logout"
           onClick={cerrarSesion} >
           Cerrar sesión
      </button>
  </>
) : (
  <Link to="/login" className="nav-enter">
    Entrar al círculo ↗
  </Link>
)}
         
        </nav>
      </header>

      <section className="grimoire-hero">

        <div className="hero-inscription">
          ✦ &nbsp; ARCANUM ALCHEMIAE &nbsp; ✦
        </div>

        <div className="hero-emblem" aria-hidden="true">
          <span className="emblem-orbit">☽</span>
          <span className="emblem-center">🜍</span>
          <span className="emblem-orbit">☉</span>
        </div>

        <p className="hero-kicker">
          EL CONOCIMIENTO COMIENZA EN EL MISTERIO
        </p>

        <h1>
          El arte de
          <br />
          <span>transmutar</span>
          <br />
          lo imposible.
        </h1>

        <div className="hero-divider" aria-hidden="true">
          <span>✧</span>
          <span>☉</span>
          <span>✧</span>
        </div>

        <p className="hero-description">
          Un conocimiento antiguo, una comunidad de mentes
          inquietas y fórmulas que esperan ser descubiertas.
          Escribe tu propio capítulo en la historia de la alquimia.
        </p>

        <div className="hero-actions">
          <Link to="/registro" className="button-gold">
            Iniciar mi travesía <span>↗</span>
          </Link>

          <Link to="/gremios" className="button-outline">
            Abrir el grimorio
          </Link>
        </div>

        <p className="hero-motto">
          «Per scientiam ad mysterium»
        </p>

      </section>

      {/* Accesos a las secciones */}
      <section className="grimoire-folios">
        
<article className="folio folio-grimoire">
  <span className="folio-number">FOLIO N.º I</span>

  <div className="folio-symbol" aria-hidden="true">
    <span className="symbol-orbit orbit-outer">✧</span>
    <span className="symbol-main">☽</span>
    <span className="symbol-star">✦</span>
    <span className="symbol-caption">PRIMA MATERIA</span>
  </div>

  <h2>El grimorio</h2>
  <p>
    Descubre ingredientes, métodos y fórmulas
    que aguardan una nueva interpretación.
  </p>

  <Link to="/gremios">Explorar el saber ↗</Link>
</article>

<article className="folio folio-guilds">
  <span className="folio-number">FOLIO N.º II</span>

  <div className="folio-symbol" aria-hidden="true">
    <span className="symbol-orbit orbit-outer">✧</span>
    <span className="symbol-main">⚗</span>
    <span className="symbol-star">☉</span>
    <span className="symbol-caption">OPUS ALCHEMICUM</span>
  </div>

  <h2>Los gremios</h2>
  <p>
    Reúne a otros alquimistas y transforma
    ideas individuales en experimentos colectivos.
  </p>

  <Link to="/gremios">Encontrar mi círculo ↗</Link>
</article>

<article className="folio folio-legacy">
  <span className="folio-number">FOLIO N.º III</span>

  <div className="folio-symbol" aria-hidden="true">
    <span className="symbol-orbit orbit-outer">✧</span>
    <span className="symbol-main">☉</span>
    <span className="symbol-star">✦</span>
    <span className="symbol-caption">MAGNUM OPUS</span>
  </div>

  <h2>El legado</h2>
  <p>
    Registra tus logros, acumula puntos de alquimia
    y asciende entre los grandes maestros.
  </p>

  <Link to="/ranking">Consultar los maestros ↗</Link>
</article>

      </section>

      <footer className="grimoire-footer">
        <span>CHEMIGRAM · MMXXVI</span>
        <span>AD ASTRA PER ALCHEMIAM ✦</span>
      </footer>

    </main>
  );
}

export default Home;
