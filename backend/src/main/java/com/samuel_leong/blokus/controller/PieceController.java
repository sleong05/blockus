package com.samuel_leong.blokus.controller;

import com.samuel_leong.blokus.model.Coordinate;
import com.samuel_leong.blokus.model.Piece;
import com.samuel_leong.blokus.model.PieceShape;
import com.samuel_leong.blokus.model.PieceType;
import com.samuel_leong.blokus.service.PieceFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pieces")
public class PieceController {
    private final PieceFactory pieceFactory;

    public PieceController(PieceFactory pieceFactory) {
        this.pieceFactory = pieceFactory;
    }

    @GetMapping
    public Map<PieceType, PieceShape> getAllPieces() {
        return pieceFactory.getListOfAllPieces();
    }

    @GetMapping("/{pieceType}")
    public List<List<Coordinate>> getPieceOrientations(@PathVariable PieceType pieceType) {
        return pieceFactory.getPieceOrientations(pieceType);
    }
}
