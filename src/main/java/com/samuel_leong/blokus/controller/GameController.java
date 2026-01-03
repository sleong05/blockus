package com.samuel_leong.blokus.controller;

import com.samuel_leong.blokus.model.Game;
import com.samuel_leong.blokus.service.GameService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public Game createGame() {
        return gameService.createGame();
    }

    @GetMapping("/{id}")
    public Game getGame(@PathVariable String id) {
        return gameService.getGame(id);
    }

//    @PutMapping("/api/games")
}
