import { useEffect, useState } from 'react'
import './App.css'
import Board from "./components/Board"
import PieceInventory from './components/PieceInventory'

type Coordinate = { row: number, col: number }

type SelectedPiece = {
  name: string,
  orientations: Coordinate[][],
} | null

function App() {
  // hook for selected piece
  const [selectedPiece, setSelectedPiece] = useState<SelectedPiece>(null);
  const [orientation, setOrientation] = useState(0);

  // function that allows PieceInventory to alter selectectPiece
  const handleSelectPiece = async (name: string) => {
    const response = await fetch(`${import.meta.env.VITE_BACKEND_API}/api/pieces/${name}`);
    const orientations = await response.json();

    setSelectedPiece({
      name: name,
      orientations: orientations
    });
    setOrientation(0);
  }

  // function for handling scroll wheel inputs
  useEffect(() => {
    const handleKeyDown = (event: KeyboardEvent) => {
      if (!selectedPiece) return;

      // r -> rotate right
      if (event.key.toLowerCase() === 'r') {
        setOrientation(prev => {
          const isFlipped = prev >= 4;
          const base = isFlipped ? 4 : 0;
          return base + ((prev - base + 1) % 4);
        })
      }

      // l -> left right
      if (event.key.toLowerCase() === 'l') {
        setOrientation(prev => {
          const isFlipped = prev >= 4;
          const base = isFlipped ? 4 : 0;
          return base + ((prev - base + 3) % 4);
        })
      }

      // f -> flip
      if (event.key.toLocaleLowerCase() === 'f') {
        setOrientation(prev => (prev < 4) ? prev + 4 : prev - 4)
      }
    }
    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown)



  }, [selectedPiece])

  return (
    <>
      <div>
        <h1>Blockus</h1>
        <p>Selected: {selectedPiece?.name || "None"}</p>
        <div className="game-container">
          <Board selectedPiece={selectedPiece} currentOrientation={orientation} />
        </div>

        <PieceInventory onSelectPiece={handleSelectPiece} />
      </div>
    </>
  )
}

export default App
