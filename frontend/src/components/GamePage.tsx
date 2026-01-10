import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import Board from './Board';
import PieceInventory from './PieceInventory';

type Coordinate = { row: number; col: number };

type SelectedPiece = {
    name: string;
    orientations: Coordinate[][];
} | null;

function GamePage() {
    const { gameId } = useParams();
    const [selectedPiece, setSelectedPiece] = useState<SelectedPiece>(null);
    const [orientation, setOrientation] = useState(0);

    const handleSelectPiece = async (name: string) => {
        const response = await fetch(`${import.meta.env.VITE_BACKEND_API}/api/pieces/${name}`);
        const orientations = await response.json();

        setSelectedPiece({
            name: name,
            orientations: orientations
        });
        setOrientation(0);
    };

    useEffect(() => {
        const handleKeyDown = (event: KeyboardEvent) => {
            if (!selectedPiece) return;

            if (event.key.toLowerCase() === 'r') {
                setOrientation(prev => {
                    const isFlipped = prev >= 4;
                    const base = isFlipped ? 4 : 0;
                    return base + ((prev - base + 1) % 4);
                });
            }

            if (event.key.toLowerCase() === 'l') {
                setOrientation(prev => {
                    const isFlipped = prev >= 4;
                    const base = isFlipped ? 4 : 0;
                    return base + ((prev - base + 3) % 4);
                });
            }

            if (event.key.toLowerCase() === 'f') {
                setOrientation(prev => (prev < 4) ? prev + 4 : prev - 4);
            }
        };

        window.addEventListener('keydown', handleKeyDown);
        return () => window.removeEventListener('keydown', handleKeyDown);
    }, [selectedPiece]);

    if (!gameId) {
        return <div>No game ID</div>;
    }

    return (
        <>
            <p>Game: {gameId}</p>
            <p>Selected: {selectedPiece?.name || "None"}</p>
            <div className="game-container">
                <Board
                    gameId={gameId}
                    selectedPiece={selectedPiece}
                    currentOrientation={orientation}
                />
            </div>
            <PieceInventory onSelectPiece={handleSelectPiece} />
        </>
    );
}

export default GamePage;