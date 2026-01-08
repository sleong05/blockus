package com.samuel_leong.blokus.model;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.samuel_leong.blokus.util.Directions.SIDES;
import static com.samuel_leong.blokus.util.Directions.CORNERS;

public class Piece {
    private final PieceShape shape;

    public Piece(PieceShape shape) {
        this.shape = shape;
    }

    public List<Coordinate> getCells() {
        return shape.getShape();
    }

    public List<Coordinate> calculateCorners() {
        Set<Coordinate> squareSet = new HashSet<>(shape.getShape());
        Set<Coordinate> cornerSet = new HashSet<>();

        // add the corners of every square on a piece to cornerSet
        for (Coordinate square:squareSet) {
            for (Coordinate cornerDirection:CORNERS) {
                Coordinate potential = square.add(cornerDirection);

                if (!squareSet.contains(potential)) {
                    cornerSet.add(potential);
                }
            }
        }

        // filter out any coordinates in the cornerSet by removing spots next to the original squares
        for (Coordinate square:squareSet) {
            for (Coordinate sideDir: SIDES) {
                cornerSet.remove(square.add(sideDir));
            }
        }

        return new ArrayList<>(cornerSet);
    }
}
