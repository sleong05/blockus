package com.samuel_leong.blokus.service;
import com.samuel_leong.blokus.model.Coordinate;
import com.samuel_leong.blokus.model.PieceShape;
import com.samuel_leong.blokus.model.PieceType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.samuel_leong.blokus.model.PieceType.*;
import static java.util.Map.entry;

@Service
public class PieceShapeFactory {
    private final Map<PieceType, PieceShape> pieces;

    public PieceShapeFactory() {
        this.pieces = Map.ofEntries(
                entry(PieceType.O1, new PieceShape(c(0, 0))),
                entry(PieceType.I2, new PieceShape(c(0, 0), c(1, 0))),
                entry(PieceType.V3, new PieceShape(c(0, 0), c(1, 0), c(0, -1))),
                entry(PieceType.I3, new PieceShape(c(0, 0), c(1, 0), c(-1, 0))),
                entry(PieceType.I4, new PieceShape(c(0, 0), c(1, 0), c(-1, 0), c(-2, 0))),
                entry(PieceType.T4, new PieceShape(c(0, 0), c(1, 0), c(0, -1), c(0, 1))),
                entry(PieceType.L4, new PieceShape(c(0, 0), c(1, 0), c(2, 0), c(0, 1))),
                entry(PieceType.Z4, new PieceShape(c(0, 0), c(-1, 0), c(1, 1), c(0, 1))),
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
    }

    public getPiece

    private Coordinate c(int row, int col) {
        return new Coordinate(row, col);
    }
}
