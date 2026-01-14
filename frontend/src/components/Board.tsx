import { useEffect, useState } from "react";
import './Board.css';


type Coordinate = { row: number; col: number };

type SelectedPiece = {
    name: string,
    orientations: Coordinate[][]
} | null


type BoardProps = {
    selectedPiece: SelectedPiece;
    currentOrientation: number;
    gameId: string;
    playerId: string;
};

function Board({ selectedPiece, currentOrientation, gameId, playerId }: BoardProps) {
    // declare grid
    const [grid, setGrid] = useState(
        Array(20).fill(null).map(() => Array(20).fill("EMPTY"))
    );
    // declare hover cells
    const [hoverCell, setHoverCell] = useState<Coordinate | null>(null);

    useEffect(() => {
        fetchBoard();
    }, [])

    const handlePlacePiece = async () => {
        if (!selectedPiece || !hoverCell) return;

        const moveData = {
            piece: selectedPiece.name,
            row: hoverCell.row,
            col: hoverCell.col,
            orientation: currentOrientation,
            playerId: playerId
        }

        try {
            const response = await fetch(`${import.meta.env.VITE_BACKEND_API}/api/games/${gameId}/place`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(moveData),
            });

            if (response.ok) {
                const updatedBoard = await response.json();
                console.log("Response:", updatedBoard);
                updateBoard(updatedBoard);
            }

            // clear the current piece
        } catch (error) {
            console.error("Error placing piece")
        }
    }
    // sets the cell based on the mouse positon rather than hover effects to avoid margin issues
    const handleMouseMove = (e: React.MouseEvent<HTMLDivElement>) => {
        const rect = e.currentTarget.getBoundingClientRect();

        // 1. Get mouse position relative to the board
        const x = e.clientX - rect.left;
        const y = e.clientY - rect.top;

        // 2. Use the ACTUAL current width/height of the board
        // This works even if the board is resized via CSS/Window resize
        const col = Math.floor((x / rect.width) * 20);
        const row = Math.floor((y / rect.height) * 20);

        // 3. Boundary check
        if (row >= 0 && row < 20 && col >= 0 && col < 20) {
            if (hoverCell?.row !== row || hoverCell?.col !== col) {
                setHoverCell({ row, col });
            }
        }
    };

    const fetchBoard = async () => {
        try {
            const response = await fetch(`${import.meta.env.VITE_BACKEND_API}/api/games/${gameId}`)

            if (!response.ok) {
                console.error("Game not found");
                return;
            }

            const board = await response.json();
            updateBoard(board);
        } catch (error) {
            console.error("Error fetching game:", error);
        }
    }


    const isPreviewCell = (row: number, col: number): boolean => {
        if (!selectedPiece || !hoverCell) return false;

        return selectedPiece.orientations[currentOrientation].some(
            (cell: Coordinate) => cell.row + hoverCell.row === row && cell.col + hoverCell.col === col
        );
    };

    function updateBoard(board: any) {
        const newGrid = Array(20).fill(null).map(() => Array(20).fill("EMPTY"));

        for (const cell of board.grid) {
            newGrid[cell.row][cell.col] = cell.color;
        }
        setGrid(newGrid);
    }

    return (
        <div className="board">
            {grid.map((row, rowIndex) => (
                row.map((cell, colIndex) => (
                    <div
                        key={`${rowIndex}-${colIndex}`}
                        className={`cell ${cell.toLowerCase()} ${isPreviewCell(rowIndex, colIndex) ? 'preview' : ''}`}
                        onMouseEnter={() => setHoverCell({ row: rowIndex, col: colIndex })}
                        onMouseLeave={() => setHoverCell(null)}
                        onClick={handlePlacePiece}
                    >
                    </div>
                ))
            ))
            }
        </div >
    );

}
export default Board