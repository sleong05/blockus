package com.samuel_leong.blokus.model;

import java.util.Random;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="games")
public class Game {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private PlayerColor currentTurn;

    @Transient
    private PlayerColor[] players = new PlayerColor[] {
        PlayerColor.BLUE, PlayerColor.YELLOW, PlayerColor.RED, PlayerColor.GREEN
    };

    @Transient
    private Board board;

    @Column(columnDefinition = "TEXT")
    private String boardJson;

    public Game() {
        this.board = new Board();
        Random rand = new Random();
        this.currentTurn = players[rand.nextInt(4)];
        this.id = generateId();
    }

    @PrePersist
    @PreUpdate
    private void saveBoardToJson() {
        try {
            List<CellState> cells = board.getGrid();
            this.boardJson = objectMapper.writeValueAsString(cells);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert board to json", e);
        }
    }

    private String generateId() {
        return java.util.UUID.randomUUID().toString().substring(0, 8);
    }

    @PostLoad
    public void loadBoardFromJson() {
        try {
            this.board = new Board();
            if (boardJson!= null && !boardJson.isEmpty()) {
                List<CellState> cells = objectMapper.readValue(
                    boardJson,
                    new TypeReference<List<CellState>>() {}
                );
            for (CellState cell: cells) {
                board.setCell(new Coordinate(cell.row(), cell.col()), cell.color());
            }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to load board from json");
        }
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


    // getters and setters

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
