package com.samuel_leong.blokus.service;

import com.samuel_leong.blokus.model.*;
import com.samuel_leong.blokus.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    public Game placePiece(String gameId, PlayerColor player, Coordinate coord, Piece piece){
        Game game = getGame(gameId);

        if (game == null) return null;

        if (!moveValidator.validMove(game.getBoard(), player, coord, piece)) return null;

        game.placePiece(piece, player, coord);
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
