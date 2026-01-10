import { Routes, Route } from 'react-router-dom';
import Home from './components/Home';
import GamePage from './components/GamePage';
import './App.css';

function App() {
  return (
    <div className="app">
      <h1>Blockus</h1>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/game/:gameId" element={<GamePage />} />
      </Routes>
    </div>
  );
}

export default App;