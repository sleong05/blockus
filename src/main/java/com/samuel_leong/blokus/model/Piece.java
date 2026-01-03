package com.samuel_leong.blokus.model;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Piece {
    private PieceShape shape;
    // for checking around tiles for corners
    private final List<Coordinate> corners = List.of(
            new Coordinate(1, 1),
            new Coordinate(-1,-1),
            new Coordinate(1, -1),
            new Coordinate(-1, 1)
    );
    private final List<Coordinate> sides = List.of(
            new Coordinate(0, 1),
            new Coordinate(0,-1),
            new Coordinate(1, 0),
            new Coordinate(-1, 0)
    );
    Piece(PieceShape shape) {
        this.shape = shape;
    }


    public void rightRotate() {
        List<Coordinate> rightRotated = shape.getShape()
                .stream()
                .map(coord -> new Coordinate(coord.col(), coord.row() * -1))
                .toList();
        this.shape = new PieceShape(rightRotated);
    }

    public void leftRotate() {
        List<Coordinate> leftRotated = shape.getShape()
                .stream()
                .map(coord -> new Coordinate(coord.col()*-1, coord.row()))
                .toList();
        this.shape = new PieceShape(leftRotated);
    }

    public void flip() {
        List<Coordinate> flipped = shape.getShape()
                .stream()
                .map(coord -> new Coordinate(coord.row()*-1, coord.col()))
                .toList();
        this.shape = new PieceShape(flipped);
    }

    public List<Coordinate> getCells() {
        return shape.getShape();
    }


    public List<Coordinate> getCorners() {
        Set<Coordinate> squareSet = new HashSet<>(shape.getShape());
        Set<Coordinate> cornerSet = new HashSet<>();

        // add the corners of every square on a piece to cornerSet
        for (Coordinate square:squareSet) {
            for (Coordinate cornerDirection:corners) {
                Coordinate potential = square.add(cornerDirection);

                if (!squareSet.contains(potential)) {
                    cornerSet.add(potential);
                }
            }
        }

        // filter out any coordinates in the cornerSet by removing spots next to the original squares
        for (Coordinate square:squareSet) {
            for (Coordinate sideDir: sides) {
                cornerSet.remove(square.add(sideDir));
            }
        }

        return new ArrayList<>(cornerSet);
    }
}
