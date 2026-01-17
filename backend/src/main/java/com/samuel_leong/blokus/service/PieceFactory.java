package com.samuel_leong.blokus.service;

import com.samuel_leong.blokus.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Service
public class PieceFactory {
    private final Map<PieceType, PieceShape> pieces;
    private final Map<PieceType, List<Piece>> orientations;
    public PieceFactory() {
        this.pieces = Map.ofEntries(
                entry(PieceType.O1, new PieceShape(c(0, 0))),
                entry(PieceType.I2, new PieceShape(c(0, 0), c(-1, 0))),
                entry(PieceType.V3, new PieceShape(c(0, 0), c(-1, 0), c(0, 1))),
                entry(PieceType.I3, new PieceShape(c(0, 0), c(1, 0), c(-1, 0))),
                entry(PieceType.I4, new PieceShape(c(0, 0), c(1, 0), c(-1, 0), c(-2, 0))),
                entry(PieceType.T4, new PieceShape(c(0, 0), c(1, 0), c(0, -1), c(0, 1))),
                entry(PieceType.L4, new PieceShape(c(0, 0), c(1, 0), c(2, 0), c(0, 1))),
                entry(PieceType.Z4, new PieceShape(c(0, 0), c(-1, 0), c(1, 1), c(0, 1))),
                entry(PieceType.O4, new PieceShape(c(0, 0), c(1, 0), c(1, 1), c(0, 1))),
                entry(PieceType.I5, new PieceShape(c(0, 0), c(-1, 0), c(-2, 0), c(1, 0), c(2, 0))),
                entry(PieceType.L5, new PieceShape(c(0, 0), c(0, 1), c(3, 0), c(1, 0), c(2, 0))),
                entry(PieceType.T5, new PieceShape(c(0, 0), c(0, 1), c(0, -1), c(1, 0), c(2, 0))),
                entry(PieceType.X5, new PieceShape(c(0, 0), c(0, 1), c(0, -1), c(1, 0), c(-1, 0))),
                entry(PieceType.F5, new PieceShape(c(0, 0), c(1, 1), c(0, -1), c(1, 0), c(-1, 0))),
                entry(PieceType.Y5, new PieceShape(c(0, 0), c(2, 0), c(0, -1), c(1, 0), c(-1, 0))),
                entry(PieceType.N5, new PieceShape(c(0, 0), c(2, 0), c(0, 1), c(1, 0), c(-1, 1))),
                entry(PieceType.Z5, new PieceShape(c(0, 0), c(0, -1), c(0, 1), c(1, -1), c(-1, 1))),
                entry(PieceType.U5, new PieceShape(c(0, 0), c(0, -1), c(0, 1), c(-1, -1), c(-1, 1))),
                entry(PieceType.P5, new PieceShape(c(0, 0), c(0, -1), c(0, 1), c(-1, 0), c(-1, 1))),
                entry(PieceType.V5, new PieceShape(c(0, 0), c(0, -1), c(0, -2), c(-1, 0), c(-2, 0))),
                entry(PieceType.W5, new PieceShape(c(0, 0), c(0, -1), c(1, -1), c(-1, 0), c(-1, 1)))
        );

        this.orientations = new HashMap<>();
        generateAllOrientations();
    }

    private void generateAllOrientations() {
        for (Map.Entry<PieceType, PieceShape> entry:pieces.entrySet()) {
            List<Coordinate> shape = entry.getValue().getShape();
            PieceType type = entry.getKey();

            List<Piece> pieceOrientations = new ArrayList<>();

            addRotations(pieceOrientations, shape);
            flip(shape);
            addRotations(pieceOrientations, shape);

            orientations.put(type, pieceOrientations);
        }
    }

    private void addRotations(List<Piece> pieceOrientations, List<Coordinate> shape) {
        for (int i = 0; i < 4; i++) {
            pieceOrientations.add(new Piece(new PieceShape(shape)));
            rightRotate(shape);
        }
    }
    private void rightRotate(List<Coordinate> shape) {
        for (int i = 0; i < shape.size(); i++) {
            Coordinate c = shape.get(i);
            shape.set(i, new Coordinate(c.col(), -c.row()));
        }
    }

    private void flip(List<Coordinate> shape) {
        for (int i = 0; i < shape.size(); i++) {
            Coordinate coord = shape.get(i);
            shape.set(i, new Coordinate(coord.row()*-1, coord.col()));
        }
    }

    private static Coordinate c(int row, int col) {
        return new Coordinate(row, col);
    }

    public Piece createPiece(PieceType pieceType, int orientation) {
        List<Piece> availableOrientations = orientations.get(pieceType);

        if (availableOrientations == null) {
            throw new IllegalArgumentException("PieceType " + pieceType + " not found in factory!");
        }

        if (orientation < 0 || orientation >= availableOrientations.size()) {
            throw new IllegalArgumentException("Invalid orientation index: " + orientation);
        }

        return availableOrientations.get(orientation);
    }

    public Map<PieceType, PieceShape> getPiecesForTypes(List<PieceType> pieceTypes) {
        Map<PieceType, PieceShape> pieces = new HashMap<>();

        for (PieceType pieceType: pieceTypes) {
            pieces.put(pieceType, this.pieces.get(pieceType));
        }
        return pieces;
    }

    public List<List<Coordinate>> getPieceOrientations(PieceType pieceType) {
        return orientations.get(pieceType).stream()
            .map(Piece::getCells)
            .toList();
    }

    public Map<PieceType, PieceShape> getListOfAllPieces() {
        return pieces;
    }
}
