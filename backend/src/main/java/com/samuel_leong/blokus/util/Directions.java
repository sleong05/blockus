package com.samuel_leong.blokus.util;

import com.samuel_leong.blokus.model.Coordinate;

import java.util.List;

public final class Directions {
    public static final List<Coordinate> SIDES = List.of(
        new Coordinate(1, 0),
        new Coordinate(0, 1),
        new Coordinate(0, -1),
        new Coordinate(-1, 0)
    );

    public static final List<Coordinate> CORNERS = List.of(
        new Coordinate(1, 1),
        new Coordinate(-1, -1),
        new Coordinate(1, -1),
        new Coordinate(-1, 1)
    );

    private Directions() {}
}
