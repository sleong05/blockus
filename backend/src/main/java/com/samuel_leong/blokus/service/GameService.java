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
        System.out.println("After getGame, playerIds: " + game.getPlayerIds());  // Add getter
        String playerId = game.addPlayer();
        System.out.println("After addPlayer, playerIds: " + game.getPlayerIds());

        if (playerId == null) return null;

        System.out.println("Checking color");
        PlayerColor color = game.getPlayerColor(playerId);
        System.out.println("color is " + color);
        game.saveBoardToJson();
        gameRepository.save(game);

        return new JoinResult(playerId, color);
    }

    public Game placePiece(String gameId, String playerId, Coordinate coord, Piece piece, PieceType pieceType){
        Game game = getGame(gameId);

        if (game == null) return null;

        PlayerColor player = game.getPlayerColor(playerId);
        if (game.getCurrentTurn() != player) {
            System.out.println("Not " + player + " player's turn");
            return null;
        }

        PlayerColor playerColor = game.getPlayerColor(playerId);

        if (playerColor == null) return null;

        if (!moveValidator.validMove(game.getBoard(), playerColor, coord, piece)) return null;

        game.placePiece(piece, playerColor, coord, playerId, pieceType);
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
