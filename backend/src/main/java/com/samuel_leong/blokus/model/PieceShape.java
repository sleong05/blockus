package com.samuel_leong.blokus.model;

import java.util.ArrayList;
import java.util.List;

public class PieceShape {
    private final List<Coordinate> squares;

    public PieceShape(List<Coordinate> coordinates) {
        this.squares = List.copyOf(coordinates);
    }

    public PieceShape(Coordinate... coords) {
        this.squares = List.of(coords);

    }



    public List<Coordinate> getShape() {
        return new ArrayList<>(squares);
    }
}
