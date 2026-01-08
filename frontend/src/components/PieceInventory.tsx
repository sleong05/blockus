import { useState, useEffect } from "react"
import './PieceInventory.css'

type Coordinate = { row: number; col: number };
type PieceData = { shape: Coordinate[] };
type PiecesMap = Record<string, PieceData>;

type PieceInventoryProps = {
    onSelectPiece: (pieceName: string) => void;
}

function PieceInventory({ onSelectPiece }: PieceInventoryProps) {
    const [pieces, setPieces] = useState<PiecesMap>({})
    const pieceOrder = ['O1', 'I2', 'I3', 'V3', 'I4', 'L4', 'T4', 'O4', 'Z4', 'I5', 'L5', 'T5', 'X5', 'F5', 'Y5', 'N5', 'Z5', 'U5', 'P5', 'V5', 'W5'];

    useEffect(() => {
        fetchPieces();
    }, []);

    const fetchPieces = async () => {
        const response = await fetch(`${import.meta.env.VITE_BACKEND_API}/api/pieces`);
        const data = await response.json();
        setPieces(data);
    }

    const centerPiece = (shape: Coordinate[]) => {
        const rows = shape.map(c => c.row);
        const cols = shape.map(c => c.col);

        const minRow = Math.min(...rows);
        const maxRow = Math.max(...rows);
        const minCol = Math.min(...cols);
        const maxCol = Math.max(...cols);

        const pieceHeight = maxRow - minRow + 1;
        const pieceWidth = maxCol - minCol + 1;

        // Center in 75px box with 15px cells
        const boxSize = 75;
        const cellSize = 15;

        const rowOffset = (boxSize - pieceHeight * cellSize) / 2 - minRow * cellSize;
        const colOffset = (boxSize - pieceWidth * cellSize) / 2 - minCol * cellSize;

        return { rowOffset, colOffset };
    };


    return (
        <div className="inventory">
            {pieceOrder.map(name => {
                if (!pieces[name]) return null;

                const { rowOffset, colOffset } = centerPiece(pieces[name].shape);

                return (
                    <div
                        key={name}
                        className="piece-button"
                        onClick={() => onSelectPiece(name)}
                    >
                        <div className="piece-preview">
                            {pieces[name].shape.map((cell, index) => (
                                <div
                                    key={index}
                                    className="piece-cell"
                                    style={{
                                        top: cell.row * 15 + rowOffset,
                                        left: cell.col * 15 + colOffset
                                    }}>
                                </div>
                            ))}
                        </div>
                    </div>
                );
            })}
        </div>
    )
}

export default PieceInventory