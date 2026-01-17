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

    const [playerId, setPlayerId] = useState<string | null>(null);
    const [color, setColor] = useState<string | null>(null);
    const [error, setError] = useState<string | null>(null);


    useEffect(() => {
        console.log("useEffect running, gameId:", gameId);
        if (!gameId) return;

        const storedPlayerId = localStorage.getItem(`playerId_${gameId}`);
        const storedColor = localStorage.getItem(`color_${gameId}`);

        console.log("storedPlayerId:", storedPlayerId);
        console.log("storedColor:", storedColor);

        if (storedPlayerId && storedColor) {
            console.log("Already joined, using stored values");
            setPlayerId(storedPlayerId);
            setColor(storedColor);
        } else {
            console.log("Not joined, calling joinGame");
            joinGame();
        }
    }, [gameId])

    const joinGame = async () => {
        console.log("joinGame started");
        try {
            const response = await fetch(`${import.meta.env.VITE_BACKEND_API}/api/games/${gameId}/join`, {
                method: 'POST'
            });

            console.log("joinGame response status:", response.status);

            if (!response.ok) {
                setError('Game is full or no game with that code found');
                return;
            }

            const data = await response.json();
            console.log("joinGame data:", data);

            localStorage.setItem(`playerId_${gameId}`, data.playerId);
            localStorage.setItem(`color_${gameId}`, data.color);

            setPlayerId(data.playerId);
            setColor(data.color);
            console.log("joinGame complete, ready set to true");
        } catch (err) {
            console.log("joinGame error:", err);
            setError('Failed to join game');
        }
    };

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

    if (error) {
        return <div>{error}</div>;
    }

    if (!playerId) {
        return <div>Joining game...</div>;
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
                    playerId={playerId}
                />
            </div>

            <PieceInventory gameId={gameId} playerId={playerId} onSelectPiece={handleSelectPiece} />

        </>
    );
}

export default GamePage;