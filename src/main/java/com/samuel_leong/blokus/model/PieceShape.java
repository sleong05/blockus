package com.samuel_leong.blokus.model;

import java.util.List;

public class PieceShape {
    private final List<Coordinate> squares;
    PieceShape(List<Coordinate> coordinates) {
        this.squares = coordinates;
    }

    public PieceShape(Coordinate... coords) {
        this.squares = List.of(coords);
    }

    public List<Coordinate> getShape() {
        return squares;
    }
}
