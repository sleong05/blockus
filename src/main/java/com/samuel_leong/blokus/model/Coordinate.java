package com.samuel_leong.blokus.model;

public record Coordinate(int row, int col) {

    public Coordinate add(Coordinate other) {
        return new Coordinate(other.row() + this.row, other.col() + this.col);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Coordinate otherObject = (Coordinate) o;
        return otherObject.row() == this.row && otherObject.col() == this.col;
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }


}
