package com.samuel_leong.blokus.model;

import java.util.Random;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.List;


public class Game {
    private String id;
    private PlayerColor currentTurn;
    private PlayerColor[] players;

    private final Board board;

    public Game() {
        this.board = new Board();
        this.players = new PlayerColor[] {PlayerColor.BLUE, PlayerColor.YELLOW, PlayerColor.RED, PlayerColor.GREEN};
        Random rand = new Random();
        this.currentTurn = players[rand.nextInt(4)];
    }

    public void nextTurn() {
        for (int i = 0; i<4; i++) {
            if (players[i] == currentTurn) {
                this.currentTurn = players[(i+1)%4];
                return;
            }
        }
    }

    public boolean isPlayersTurn(PlayerColor player) {
        return player == currentTurn;
    }

    public void placePiece(Piece piece, PlayerColor player, Coordinate center) {
        for (Coordinate cell: piece.getCells()) {
            Coordinate cellCentered = cell.add(center);
            board.setCell(cellCentered, player);
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public Board getBoard() {
        return this.board;
    }

    public PlayerColor getCurrentTurn() {
        return this.currentTurn;
    }

    public PlayerColor[] getPlayers() {
        return this.players;
    }
}
