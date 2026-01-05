package com.samuel_leong.blokus.controller;

import com.samuel_leong.blokus.model.Coordinate;
import com.samuel_leong.blokus.model.Game;
import com.samuel_leong.blokus.model.Piece;
import com.samuel_leong.blokus.model.PlacePieceRequest;
import com.samuel_leong.blokus.service.GameService;
import com.samuel_leong.blokus.service.PieceFactory;
import org.springframework.web.bind.annotation.*;

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
    }

    @GetMapping("/{id}")
    public Game getGame(@PathVariable String id) {
        return gameService.getGame(id);
    }

    @PutMapping("/{id}/place")
    public Game placePiece(@PathVariable String id, @RequestBody PlacePieceRequest request) {
        // handle valid piece logic somewhere
        Piece piece = pieceFactory.createPiece(request.piece(), request.orientation());
        Coordinate placement = new Coordinate(request.row(), request.col());
        gameService.placePiece(id, request.player(), placement, piece);

        return gameService.getGame(id);
    }
}
