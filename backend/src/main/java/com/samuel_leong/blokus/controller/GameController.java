package com.samuel_leong.blokus.controller;

import com.samuel_leong.blokus.model.*;
import com.samuel_leong.blokus.service.GameService;
import com.samuel_leong.blokus.service.PieceFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public Map<String, String> createGame() {
        Game game = gameService.createGame();
        return Map.of("id", game.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBoard(@PathVariable String id) {
        Game game = gameService.getGame(id);

        return (game == null) ?
            ResponseEntity.notFound().build():
            ResponseEntity.ok(game.getBoard());
    }

    @PutMapping("/{id}/place")
    public ResponseEntity<?> placePiece(@PathVariable String id, @RequestBody PlacePieceRequest request) {
        Piece piece = pieceFactory.createPiece(request.piece(), request.orientation());
        Coordinate placement = new Coordinate(request.row(), request.col());
        Game game = gameService.placePiece(id, request.player(), placement, piece);

        return (game != null) ?
            ResponseEntity.ok(game.getBoard()) :
            ResponseEntity.badRequest().body("invalid move");
    }
}
