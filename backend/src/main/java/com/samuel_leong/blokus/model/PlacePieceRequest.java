package com.samuel_leong.blokus.model;

public record PlacePieceRequest(PieceType piece, int row, int col, int orientation, PlayerColor player) {
}
