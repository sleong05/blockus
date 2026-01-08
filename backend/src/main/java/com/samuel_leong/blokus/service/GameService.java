package com.samuel_leong.blokus.service;

import com.samuel_leong.blokus.model.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class GameService {

    private final MoveValidator moveValidator;

    public GameService(MoveValidator moveValidator) {
        this.moveValidator = moveValidator;
    }
    private Map<String, Game> games = new HashMap<>();

    public Game createGame() {
        Game game = new Game();
        game.setId(generateId());
        games.put(game.getId(), game);
        return game;
    }

    public Optional<Game> placePiece(String gameId, PlayerColor player, Coordinate coord, Piece piece){
        // lookup game
        Game game = getGame(gameId);


        // move validation
        if (!moveValidator.validMove(game.getBoard(), player, coord, piece)) {
            return Optional.empty();
        }
        // place piece
        game.placePiece(piece, player, coord);

        // next turn
        game.nextTurn();

        return Optional.of(game);
    }

    public Game getGame(String gameId) {
        return games.get(gameId);
    }

    private String generateId() {
        return "abc123";
    }
}
