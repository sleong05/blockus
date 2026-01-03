package com.samuel_leong.blokus.service;

import com.samuel_leong.blokus.model.Coordinate;
import com.samuel_leong.blokus.model.Game;
import com.samuel_leong.blokus.model.Piece;
import com.samuel_leong.blokus.model.PlayerColor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameService {

    private Map<String, Game> games = new HashMap<>();

    public Game createGame() {
        Game game = new Game();
        game.setId(generateId());
        games.put(game.getId(), game);
        return game;
    }

    public void placePiece(String gameId, PlayerColor player, Coordinate coord, Piece piece){
        // lookup game
        Game game = getGame(gameId);


        // move validation

        // place piece
        game.placePiece(piece, player, coord);

        // next turn
        game.nextTurn();
    }

    public Game getGame(String gameId) {
        return games.get(gameId);
    }

    private String generateId() {
        return "abc123";
    }
}
