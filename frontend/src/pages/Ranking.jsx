
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { obtenerRanking } from '../services/rankingService';
import '../styles/Ranking.css';

function Ranking() {
  const [alquimistas, setAlquimistas] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const cargarRanking = async () => {
      try {
        setError('');
        const datos = await obtenerRanking();
        setAlquimistas(datos);
      } catch (err) {
        setError(
          err.response?.data?.message ||
          'No fue posible consultar el registro de alquimistas.'
        );
      } finally {
        setCargando(false);
      }
    };

    cargarRanking();
  }, []);

  const obtenerMedalla = (posicion) => {
    if (posicion === 0) return '✧';
    if (posicion === 1) return '✦';
    if (posicion === 2) return '❖';
    return String(posicion + 1).padStart(2, '0');
  };

  return (
    <main className="ranking-page grimoire">
      <header className="grimoire-header">
        <Link to="/" className="grimoire-brand">
          <span className="brand-mark">☉</span>
          CHEMIGRAM
        </Link>

        <nav className="grimoire-nav">
          <Link to="/">Inicio</Link>
          <Link to="/gremios">Círculos</Link>
          <Link to="/ranking">Ranking</Link>
        </nav>
      </header>

      <section className="ranking-hero">
        <p className="ranking-inscription">
          LIBER ALCHEMIAE · TABULA HONORIS
        </p>

        <div className="ranking-emblem">♜</div>

        <p className="ranking-kicker">EL LEGADO DE LOS ALQUIMISTAS</p>

        <h1>
          Salón de <span>honor</span>
        </h1>

        <div className="ranking-divider">
          <span>✧</span>
          <span>✦</span>
          <span>✧</span>
        </div>

        <p className="ranking-description">
          El conocimiento se construye con paciencia, experimentación
          y colaboración. Este registro reconoce a quienes han
          acumulado experiencia y rareza en el arte de la alquimia.
        </p>
      </section>

      <section className="ranking-content">
        <div className="ranking-heading">
          <div>
            <p className="ranking-kicker">REGISTRO OFICIAL</p>
            <h2>Los alquimistas destacados</h2>
          </div>

          <span className="ranking-total">
            {alquimistas.length} REGISTRADOS
          </span>
        </div>

        {error && (
          <p className="ranking-alert" role="alert">
            {error}
          </p>
        )}

        {cargando ? (
          <div className="ranking-empty">
            <span>✧</span>
            <p>Consultando el registro de honor...</p>
          </div>
        ) : alquimistas.length === 0 ? (
          <div className="ranking-empty">
            <span>♜</span>
            <h3>El registro aún está vacío</h3>
            <p>Los primeros logros están por escribirse.</p>
          </div>
        ) : (
          <>
            {alquimistas.length > 0 && (
              <article className="ranking-featured">
                <div className="featured-decoration">✧</div>

                <p className="ranking-kicker">PRIMER LUGAR</p>

                <div className="featured-symbol">♛</div>

                <h3>{alquimistas[0].nombre}</h3>

                <p className="featured-specialty">
                  {alquimistas[0].especialidad || 'Alquimista'}
                </p>

                <div className="featured-stats">
                  <div>
                    <strong>
                      {alquimistas[0].puntosAlquimia ?? 0}
                    </strong>
                    <span>PUNTOS DE ALQUIMIA</span>
                  </div>

                  <div>
                    <strong>
                      {alquimistas[0].rarezaTotal ?? 0}
                    </strong>
                    <span>RAREZA ACUMULADA</span>
                  </div>
                </div>
              </article>
            )}

            <div className="ranking-table-wrapper">
              <div className="ranking-table-heading">
                <h3>Tabla de posiciones</h3>
                <span>ORDEN DE MÉRITO</span>
              </div>

              <div className="ranking-table">
                <div className="ranking-row ranking-table-header">
                  <span>POS.</span>
                  <span>ALQUIMISTA</span>
                  <span>ESPECIALIDAD</span>
                  <span>PUNTOS</span>
                  <span>RAREZA</span>
                </div>

                {alquimistas.map((alquimista, index) => (
                  <div
                    className={`ranking-row ${
                      index < 3 ? 'ranking-top-three' : ''
                    }`}
                    key={alquimista.id ?? index}
                  >
                    <span className="ranking-position">
                      {obtenerMedalla(index)}
                    </span>

                    <span className="ranking-name">
                      {alquimista.nombre}
                    </span>

                    <span className="ranking-specialty">
                      {alquimista.especialidad || 'Sin especialidad'}
                    </span>

                    <span className="ranking-points">
                      {alquimista.puntosAlquimia ?? 0}
                    </span>

                    <span className="ranking-rarity">
                      {alquimista.rarezaTotal ?? 0}
                    </span>
                  </div>
                ))}
              </div>
            </div>
          </>
        )}
      </section>

      <footer className="grimoire-footer">
        <span>CHEMIGRAM · LIBER ALCHEMIAE</span>
        <span>PER SCIENTIAM AD MYSTERIUM</span>
      </footer>
    </main>
  );
}

export default Ranking;
