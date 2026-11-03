import { useEffect, useState } from "react";
import api from "../services/api";
import "../styles/Pociones.css";

function Gremios() {
  const [pociones, setPociones] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const cargarPociones = async () => {
      try {
        const response = await api.get("/api/pociones");
        setPociones(response.data);
      } catch (err) {
        console.error("Error al cargar pociones:", err);
        setError("No fue posible consultar las pociones.");
      } finally {
        setLoading(false);
      }
    };

    cargarPociones();
  }, []);

  return (
    <main className="potions-page">
      <header className="potions-header">
        <span className="potions-eyebrow">CHEMIGRAM · ARCHIVUM</span>
        <h1>El grimorio de las pociones</h1>
        <p>
          Un registro de los descubrimientos y creaciones
          de nuestra comunidad alquímica.
        </p>
      </header>

      {loading && <p className="potions-message">Consultando el grimorio...</p>}

      {error && <p className="potions-message">{error}</p>}

      {!loading && !error && pociones.length === 0 && (
        <section className="potions-empty">
          <span>⚗</span>
          <h2>El grimorio aguarda su primera creación</h2>
          <p>Cuando se creen nuevas pociones, aparecerán aquí.</p>
        </section>
      )}

      {!loading && !error && pociones.length > 0 && (
        <section className="potions-grid">
          {pociones.map((pocion) => (
            <article className="potion-card" key={pocion.id}>
              <span className="potion-icon">⚗</span>
              <h2>{pocion.nombre}</h2>
              <p>{pocion.descripcion}</p>
              <span className="potion-rarity">
                Rareza: {pocion.rareza}
              </span>
            </article>
          ))}
        </section>
      )}
    </main>
  );
}

export default Gremios;