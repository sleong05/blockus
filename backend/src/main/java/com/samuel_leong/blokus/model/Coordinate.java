package com.samuel_leong.blokus.model;

public record Coordinate(int row, int col) {

    public Coordinate add(Coordinate other) {
        return new Coordinate(other.row() + this.row, other.col() + this.col);
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) return false;
        Coordinate other = (Coordinate) obj;
        return other.row() == this.row && other.col() == this.col;
    }
}
