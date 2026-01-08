package com.samuel_leong.blokus.service;

import com.samuel_leong.blokus.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.samuel_leong.blokus.util.Directions.SIDES;

@Service
public class MoveValidator {

    public boolean validMove(Board board, PlayerColor player, Coordinate coord, Piece piece){
        // apply absolute positioning to incoming piece
        List<Coordinate> incomingCells = new ArrayList<>();
        for (Coordinate relativeCoord:piece.getCells()) {
            incomingCells.add(relativeCoord.add(coord));
        }

        if (pieceOutOfBounds(incomingCells)) return false;

        if (overlappingOtherPieces(board, incomingCells)) return false;

        if (pieceAdjacentToOwnColor(board, player, incomingCells)) return false;

        // check for starting piece before checking corners because there are no pieces to connect to yet
        if (isStartingPiece(player, incomingCells)) return true;

        if (!cornersTouchingPiece(board, player, coord, piece)) return false;

        return true;
    }

    private static boolean isStartingPiece(PlayerColor player, List<Coordinate> incomingCells) {
        System.out.println(incomingCells);
        Coordinate startingCoord = getStartingCorner(player);

        for (Coordinate coord: incomingCells) {
            if (coord.equals(startingCoord)) return true;
         }
        return false;
    }

    private static boolean cornersTouchingPiece(Board board, PlayerColor player, Coordinate coord, Piece piece) {
        List<Coordinate> relativeCorners = piece.calculateCorners();

        List<Coordinate> absoluteCorners = new ArrayList<>();
        for (Coordinate relativeCoord:relativeCorners) {
            absoluteCorners.add(relativeCoord.add(coord));
        }

        // check that there is a corner touching the piece
        for (Coordinate absCoord:absoluteCorners) {
            if (coordOutOfBounds(absCoord)) continue;

            if (board.getCell(absCoord) == player) {
                return true;
            }
        }
        return false;
    }

    private static boolean pieceAdjacentToOwnColor(Board board, PlayerColor player, List<Coordinate> incomingCells) {
        for (Coordinate absCoord: incomingCells) {
            if (adjacentToOwnColor(board, player, absCoord)) return true;
        }
        return false;
    }

    private static boolean overlappingOtherPieces(Board board, List<Coordinate> incomingCells) {
        for (Coordinate absCoord: incomingCells) {
            if (!board.isEmpty(absCoord)) return true;
        }
        return false;
    }

    private static boolean pieceOutOfBounds(List<Coordinate> incomingCells) {
        for (Coordinate absCoord: incomingCells) {
            if (coordOutOfBounds(absCoord)) return true;

        }
        return false;
    }

    private static boolean coordOutOfBounds(Coordinate absCoord) {
        if (absCoord.row() < 0 || absCoord.row() > 19) return true;

        if (absCoord.col() < 0 || absCoord.col() > 19) return true;
        return false;
    }

    private static Coordinate getStartingCorner(PlayerColor player) {
        return switch (player) {
            case BLUE -> new Coordinate(0, 0);
            case YELLOW -> new Coordinate(0, 19);
            case RED -> new Coordinate(19, 19);
            case GREEN -> new Coordinate(19, 0);
            case EMPTY -> throw new IllegalArgumentException("EMPTY is not a player");
        };
    }

    private static boolean adjacentToOwnColor(Board board, PlayerColor player, Coordinate position) {
        for (Coordinate sideDir: SIDES) {
            Coordinate side = sideDir.add(position);
            if (coordOutOfBounds(side)) continue;
            if (board.getCell(side) == player) return true;
        }
        return false;
    }
}
