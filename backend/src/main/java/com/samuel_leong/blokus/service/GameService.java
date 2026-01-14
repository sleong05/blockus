package com.samuel_leong.blokus.service;

import com.samuel_leong.blokus.model.*;
import com.samuel_leong.blokus.repository.GameRepository;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final MoveValidator moveValidator;
    private final GameRepository gameRepository;

    public GameService(MoveValidator moveValidator, GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        this.moveValidator = moveValidator;
    }

    public Game createGame() {
        Game game = new Game();
        return gameRepository.save(game);
    }

    public JoinResult joinGame(String gameId) {
        // returns null if game is full
        Game game = getGame(gameId);

        String playerId = game.addPlayer();

        if (playerId == null) return null;

        PlayerColor color = game.getPlayerColor(playerId);
        gameRepository.save(game);

        return new JoinResult(playerId, color);
    }

    public Game placePiece(String gameId, String playerId, Coordinate coord, Piece piece){
        Game game = getGame(gameId);

        if (game == null) return null;

        PlayerColor playerColor = game.getPlayerColor(playerId);

        if (playerColor == null) return null;

        if (!moveValidator.validMove(game.getBoard(), playerColor, coord, piece)) return null;

        game.placePiece(piece, playerColor, coord);
        game.nextTurn();

        gameRepository.save(game);

        return game;
    }

    public Game getGame(String gameId) {
        return gameRepository.findById(gameId).orElse(null);
    }

    private String generateId() {
        return "abc123";
    }
}
