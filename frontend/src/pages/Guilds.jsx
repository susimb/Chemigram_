import useNotificaciones from '../hooks/useNotificaciones';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import {
  listarGremios,
  crearGremio,
  listarMiembros,
  unirseAGremio,
} from '../services/gremioService';
import '../styles/Guilds.css';

const ICONOS = ['⚗️', '🌙', '🔥', '🌿', '💎', '🜂', '☼', '✧'];

function Guilds() {
  const [gremios, setGremios] = useState([]);
  const [cantidades, setCantidades] = useState({});
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');
  const [mensaje, setMensaje] = useState('');
  const [creando, setCreando] = useState(false);
  const [uniendose, setUniendose] = useState(null);

  const [nombre, setNombre] = useState('');
  const [descripcion, setDescripcion] = useState('');
  const [icono, setIcono] = useState('⚗️');

  const [gremioSeleccionado, setGremioSeleccionado] = useState(null);
  const [miembros, setMiembros] = useState([]);
  const [cargandoMiembros, setCargandoMiembros] = useState(false);

  const gremioActivoId = gremioSeleccionado
  ? gremioSeleccionado.id ?? gremioSeleccionado.gremioId
  : null;

const {
  conectado,
  notificaciones,
  error: errorNotificaciones,
  limpiarNotificaciones,
} = useNotificaciones(gremioActivoId);

  const cargarGremios = async () => {
    setCargando(true);
    setError('');

    try {
      const datos = await listarGremios();
      setGremios(datos);

      // Consultamos los integrantes de cada gremio.
      const resultados = await Promise.all(
        datos.map(async (gremio) => {
          const id = gremio.id ?? gremio.gremioId;

          try {
            const integrantes = await listarMiembros(id);
            return [id, integrantes.length];
          } catch {
            return [id, null];
          }
        })
      );

      setCantidades(Object.fromEntries(resultados));
    } catch (err) {
      setError(
        err.response?.data?.message ||
        'No fue posible consultar los círculos alquímicos.'
      );
    } finally {
      setCargando(false);
    }
  };

  useEffect(() => {
    cargarGremios();
  }, []);

  const handleCrear = async (e) => {
    e.preventDefault();
    setError('');
    setMensaje('');

    if (!nombre.trim()) {
      setError('El nombre del gremio es obligatorio.');
      return;
    }

    setCreando(true);

    try {
      await crearGremio({
        nombre: nombre.trim(),
        descripcion: descripcion.trim(),
        icono,
      });

      setNombre('');
      setDescripcion('');
      setIcono('⚗️');
      setMensaje('El nuevo círculo ha sido fundado.');
      await cargarGremios();
    } catch (err) {
      setError(
        err.response?.data?.message ||
        'No fue posible fundar el gremio.'
      );
    } finally {
      setCreando(false);
    }
  };

  const handleUnirse = async (gremio) => {
    const id = gremio.id ?? gremio.gremioId;

    setUniendose(id);
    setError('');
    setMensaje('');

    try {
      await unirseAGremio(id);
      setMensaje(`Te has unido a ${gremio.nombre}.`);
      await cargarGremios();
    } catch (err) {
      setError(
        err.response?.data?.message ||
        'No fue posible unirse a este gremio.'
      );
    } finally {
      setUniendose(null);
    }
  };

  const verMiembros = async (gremio) => {
    const id = gremio.id ?? gremio.gremioId;

    setGremioSeleccionado(gremio);
    setMiembros([]);
    setCargandoMiembros(true);

    try {
      const datos = await listarMiembros(id);
      setMiembros(datos);
    } catch (err) {
      setError(
        err.response?.data?.message ||
        'No fue posible consultar los integrantes.'
      );
    } finally {
      setCargandoMiembros(false);
    }
  };

  const cerrarMiembros = () => {
    setGremioSeleccionado(null);
    setMiembros([]);
  };

  return (
    <main className="guilds-page grimoire">
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

      <section className="guilds-hero">
        <p className="guilds-inscription">
          LIBER ALCHEMIAE · SOCIETATES
        </p>

        <div className="guilds-emblem">⚗</div>

        <p className="guilds-kicker">LOS LAZOS DEL CONOCIMIENTO</p>

        <h1>
          Círculos de <span>alquimia</span>
        </h1>

        <div className="guilds-divider">
          <span>✧</span>
          <span>✦</span>
          <span>✧</span>
        </div>

        <p className="guilds-description">
          Ningún alquimista descubre todos los secretos por sí solo.
          Encuentra tu círculo, comparte tus fórmulas y construye
          un legado junto a otros buscadores del saber.
        </p>
      </section>

      <section className="guilds-content">
        <div className="guilds-section-heading">
          <div>
            <p className="guilds-kicker">ARCHIVO DE SOCIEDADES</p>
            <h2>Los círculos existentes</h2>
          </div>

          <span className="guilds-count">
            {gremios.length} {gremios.length === 1 ? 'CÍRCULO' : 'CÍRCULOS'}
          </span>
        </div>

        {error && (
          <p className="guilds-alert guilds-alert-error" role="alert">
            {error}
          </p>
        )}

        {mensaje && (
          <p className="guilds-alert guilds-alert-success" role="status">
            {mensaje}
          </p>
        )}

        {cargando ? (
          <div className="guilds-empty">
            <span>⚗</span>
            <p>Consultando los archivos del grimorio...</p>
          </div>
        ) : gremios.length === 0 ? (
          <div className="guilds-empty">
            <span>✧</span>
            <h3>Aún no existen círculos</h3>
            <p>Sé el primero en fundar una sociedad alquímica.</p>
          </div>
        ) : (
          <div className="guilds-grid">
            {gremios.map((gremio) => {
              const id = gremio.id ?? gremio.gremioId;

              return (
                <article className="guild-card" key={id}>
                  <span className="guild-card-seal">✧ ✦ ✧</span>

                  <p className="guild-card-number">
                    SOCIEDAD Nº {String(id).padStart(3, '0')}
                  </p>

                  <div className="guild-card-icon">
                    {gremio.icono || '⚗️'}
                  </div>

                  <h3>{gremio.nombre}</h3>

                  <p className="guild-card-description">
                    {gremio.descripcion ||
                      'Los secretos de este círculo aún esperan ser descubiertos.'}
                  </p>

                  <div className="guild-card-members">
                    <span>♙</span>
                    {cantidades[id] === null ||
                    cantidades[id] === undefined
                      ? 'Integrantes no disponibles'
                      : `${cantidades[id]} ${
                          cantidades[id] === 1
                            ? 'integrante'
                            : 'integrantes'
                        }`}
                  </div>

                  <div className="guild-card-actions">
                    <button
                      className="guild-button-outline"
                      onClick={() => verMiembros(gremio)}
                    >
                      Consultar
                    </button>

                    <button
                      className="guild-button-gold"
                      onClick={() => handleUnirse(gremio)}
                      disabled={uniendose === id}
                    >
                      {uniendose === id ? 'Uniéndose...' : 'Unirse ↗'}
                    </button>
                  </div>
                </article>
              );
            })}
          </div>
        )}
      </section>

      <section className="guild-create-section">
        <div className="guild-create-decoration">✧</div>

        <p className="guilds-kicker">UNA NUEVA SOCIEDAD</p>
        <h2>Funda tu propio círculo</h2>

        <p>
          Reúne a otros alquimistas y comienza a escribir
          un nuevo capítulo en la historia del conocimiento.
        </p>

        <form className="guild-create-form" onSubmit={handleCrear}>
          <label htmlFor="guild-name">Nombre del círculo</label>
          <input
            id="guild-name"
            value={nombre}
            onChange={(e) => setNombre(e.target.value)}
            placeholder="Ej. La Orden del Fénix"
            maxLength={80}
            required
          />

          <label htmlFor="guild-description">Descripción</label>
          <textarea
            id="guild-description"
            value={descripcion}
            onChange={(e) => setDescripcion(e.target.value)}
            placeholder="Describe el propósito de tu sociedad..."
            rows={3}
          />

          <label>Emblema del círculo</label>
          <div className="guild-icon-picker">
            {ICONOS.map((simbolo) => (
              <button
                type="button"
                key={simbolo}
                className={icono === simbolo ? 'selected' : ''}
                onClick={() => setIcono(simbolo)}
                aria-label={`Seleccionar emblema ${simbolo}`}
                aria-pressed={icono === simbolo}
              >
                {simbolo}
              </button>
            ))}
          </div>

          <button
            type="submit"
            className="guild-button-gold guild-create-submit"
            disabled={creando}
          >
            {creando ? 'Fundando círculo...' : 'Fundar sociedad ↗'}
          </button>
        </form>
      </section>

      <footer className="grimoire-footer">
        <span>CHEMIGRAM · LIBER ALCHEMIAE</span>
        <span>PER SCIENTIAM AD MYSTERIUM</span>
      </footer>

      {gremioSeleccionado && (
        <div className="guild-modal-backdrop" onClick={cerrarMiembros}>
          <section
            className="guild-members-modal"
            role="dialog"
            aria-modal="true"
            aria-labelledby="guild-modal-title"
            onClick={(e) => e.stopPropagation()}
          >
            <button
              className="guild-modal-close"
              onClick={cerrarMiembros}
              aria-label="Cerrar"
            >
              ×
            </button>

            <p className="guilds-kicker">REGISTRO DE INTEGRANTES</p>

            <div className="guild-modal-icon">
              {gremioSeleccionado.icono || '⚗️'}
            </div>

            <h2 id="guild-modal-title">
              {gremioSeleccionado.nombre}
            </h2>
            <div className="guild-notifications">
  <div className="guild-notifications-heading">
    <div>
      <p className="guilds-kicker">ACTIVIDAD DEL CÍRCULO</p>

      <span className={`guild-connection ${conectado ? 'online' : 'offline'}`}>
        <span className="guild-connection-dot"></span>
        {conectado ? 'Conectado en tiempo real' : 'Conectando...'}
      </span>
    </div>

    <button
      type="button"
      className="guild-notifications-clear"
      onClick={limpiarNotificaciones}
      disabled={notificaciones.length === 0}
    >
      Limpiar
    </button>
  </div>

  {errorNotificaciones && (
    <p className="guild-notification-error">
      {errorNotificaciones}
    </p>
  )}

  {notificaciones.length === 0 ? (
    <p className="guild-notifications-empty">
      No hay actividad reciente en este círculo.
    </p>
  ) : (
    <div className="guild-notifications-list">
      {notificaciones.map((notificacion, index) => (
        <article
          className="guild-notification"
          key={`${notificacion.formulaId}-${notificacion.categoria}-${index}`}
        >
          <span className="guild-notification-icon">✧</span>

          <div>
            <strong>
              {notificacion.tipo === 'VOTO_ACTUALIZADO'
                ? 'Actividad de votación'
                : 'Nueva actividad'}
            </strong>

            <p>
              {notificacion.tipo === 'VOTO_ACTUALIZADO'
                ? `Se registró un voto en la categoría ${notificacion.categoria || 'general'}.`
                : 'El círculo ha recibido una nueva notificación.'}
            </p>

            {notificacion.formulaId && (
              <small>Fórmula #{notificacion.formulaId}</small>
            )}
          </div>
        </article>
      ))}
    </div>
  )}
</div>

            {cargandoMiembros ? (
              <p>Consultando integrantes...</p>
            ) : miembros.length === 0 ? (
              <p>Este círculo todavía no tiene integrantes registrados.</p>
            ) : (
              <div className="guild-members-list">
                {miembros.map((miembro, index) => {
                  const usuario = miembro.usuario || miembro;

                  return (
                    <div
                      className="guild-member"
                      key={miembro.id ?? usuario.id ?? index}
                    >
                      <span className="guild-member-symbol">♙</span>

                      <span>
                        {usuario.nombre || 'Alquimista'}
                      </span>

                      {miembro.esCatador && (
                        <span className="guild-taster">
                          ✦ CATADOR
                        </span>
                      )}
                    </div>
                  );
                })}
              </div>
            )}
          </section>
        </div>
      )}
    </main>
  );
}

export default Guilds;
