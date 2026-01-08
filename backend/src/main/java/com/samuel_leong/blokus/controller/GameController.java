package com.samuel_leong.blokus.controller;

import com.samuel_leong.blokus.model.*;
import com.samuel_leong.blokus.service.GameService;
import com.samuel_leong.blokus.service.PieceFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameService gameService;
    private final PieceFactory pieceFactory;
    public GameController(GameService gameService, PieceFactory pieceFactory) {
        this.gameService = gameService;
        this.pieceFactory = pieceFactory;
    }

    @PostMapping
    public Game createGame() {
        return gameService.createGame();
    } // should not return game later should just return board

    @GetMapping("/{id}")
    public Board getBoard(@PathVariable String id) {
        return gameService.getGame(id).getBoard();
    }

    @PutMapping("/{id}/place")
    public ResponseEntity<?> placePiece(@PathVariable String id, @RequestBody PlacePieceRequest request) {
        Piece piece = pieceFactory.createPiece(request.piece(), request.orientation());
        Coordinate placement = new Coordinate(request.row(), request.col());
        Optional<Game> game = gameService.placePiece(id, request.player(), placement, piece);

        return (game.isPresent()) ?
            ResponseEntity.ok(game.get().getBoard()) :
            ResponseEntity.badRequest().body("invalid move");
    }
}
