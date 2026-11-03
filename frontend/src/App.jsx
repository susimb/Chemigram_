import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import Login from './pages/Login';
import Registro from './pages/Registro';
import Gremios from './pages/Gremios';
import Ranking from './pages/Ranking';
import Perfil from './pages/Perfil';
import Guilds from './pages/Guilds';
import RutaProtegida from './components/RutaProtegida';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/ranking" element={<Ranking />} />
        <Route path="/login" element={<Login />} />
        <Route path="/registro" element={<Registro />} />
        <Route path="/gremios" element={<Guilds />} />
        <Route element={<RutaProtegida />}>
          <Route path="/gremios" element={<Gremios />} />
          <Route path="/ranking" element={<Ranking />} />
          <Route path="/perfil" element={<Perfil />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
