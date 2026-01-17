package com.samuel_leong.blokus.controller;

import com.samuel_leong.blokus.model.*;
import com.samuel_leong.blokus.service.GameService;
import com.samuel_leong.blokus.service.PieceFactory;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<?> createGame() {
        Game game = gameService.createGame();
        String gameId = game.getId();
        JoinResult result = gameService.joinGame(gameId);

        return ResponseEntity.ok(
            Map.of(
                "gameId", gameId,
                "playerId", result.playerId(),
                "color", result.color()
            )
        );
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<?> joinGame(@PathVariable String id) {
        JoinResult result = gameService.joinGame(id);

        return (result == null) ?
            ResponseEntity.badRequest().body("Game is full"):
            ResponseEntity.ok(
                Map.of(
                    "playerId", result.playerId(),
                    "color", result.color()
                )
            );
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
        Game game = gameService.placePiece(id, request.playerId(), placement, piece, request.piece());

        return (game != null) ?
            ResponseEntity.ok(game.getBoard()) :
            ResponseEntity.badRequest().body("invalid move");
    }

    @GetMapping("/{gameId}/inventory")
    public ResponseEntity<?> getInventory(@PathVariable String gameId, @RequestParam String playerId) {
        Game game = gameService.getGame(gameId);

        if (game == null) return ResponseEntity.notFound().build();

        List<PieceType> inventory = game.getInventory(playerId);

        if (inventory == null) return ResponseEntity.badRequest().body("Invalid Player");

        Map<PieceType, PieceShape> pieceInventory = pieceFactory.getPiecesForTypes(inventory);
        return ResponseEntity.ok(pieceInventory);

    }
}
