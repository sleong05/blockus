package com.samuel_leong.blokus.model;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import static com.samuel_leong.blokus.util.Colors.playerColors;

@Entity
@Table(name="games")
public class Game {

    public Map<String, PlayerColor> getPlayerIds() {
        return playerIds;
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private PlayerColor currentTurn;

    @Transient
    private Map<String, PlayerColor> playerIds = new HashMap<>();

    @Column(columnDefinition = "TEXT")
    private String playersJson;

    @Transient
    private Board board;

    @Column(columnDefinition = "TEXT")
    private String boardJson;

    public Game() {
        this.board = new Board();
        Random rand = new Random();
        this.currentTurn = playerColors[rand.nextInt(0,4)];
        this.id = generateId();
    }

    public String addPlayer() {
        for (PlayerColor color:playerColors) {
            if (!playerIds.containsValue(color)) {
                String playerId = UUID.randomUUID().toString().substring(0, 8);
                playerIds.put(playerId, color);
                return playerId;
            }
        }
        return null;
    }
    @PrePersist
    @PreUpdate
    public void saveBoardToJson() {
        System.out.println("saveBoardToJson called!");
        try {
            List<CellState> cells = board.getGrid();
            this.boardJson = objectMapper.writeValueAsString(cells);
            this.playersJson = objectMapper.writeValueAsString(playerIds);

            System.out.println("Saving playerIds: " + playerIds);
            System.out.println("Saving playersJson: " + playersJson);

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
            // load board
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

            // load playerIds
            this.playerIds = new HashMap<>();
            if (playersJson != null && !playersJson.isEmpty()) {
                this.playerIds = objectMapper.readValue(playersJson, new TypeReference<Map<String, PlayerColor>>() {
                });
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to load from json");
        }
    }

    public void nextTurn() {
        for (int i = 0; i<4; i++) {
            if (playerColors[i] == currentTurn) {
                this.currentTurn = playerColors[(i+1)%4];
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

    public boolean isValidPlayer(String playerId) {
        return playerIds.containsKey(playerId);
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

    public PlayerColor getPlayerColor(String playerId) {
        return playerIds.get(playerId);
    }

    public PlayerColor getCurrentTurn() {
        return this.currentTurn;
    }
}
