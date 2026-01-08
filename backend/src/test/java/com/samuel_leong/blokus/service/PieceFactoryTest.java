package com.samuel_leong.blokus.service;

import com.samuel_leong.blokus.model.Coordinate;
import com.samuel_leong.blokus.model.Piece;
import com.samuel_leong.blokus.model.PieceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PieceFactoryTest {

    private PieceFactory factory;

    @BeforeEach
    void setUp() {
        factory = new PieceFactory();

    }

    // --- Orientation count tests ---

    @Test
    void eachPieceTypeHasEightOrientations() {
        for (PieceType type : PieceType.values()) {
            Piece piece = factory.createPiece(type, 7);
            assertNotNull(piece);
        }
    }

    @Test
    void invalidOrientationThrowsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            factory.createPiece(PieceType.O1, 8);
        });
    }

    // --- Base shape tests ---

    @Test
    void o1BaseShapeIsCorrect() {
        Piece piece = factory.createPiece(PieceType.O1, 0);
        List<Coordinate> cells = piece.getCells();

        assertEquals(1, cells.size());
        assertTrue(cells.contains(new Coordinate(0, 0)));
    }

    @Test
    void i2BaseShapeIsCorrect() {
        Piece piece = factory.createPiece(PieceType.I2, 0);
        List<Coordinate> cells = piece.getCells();

        assertEquals(2, cells.size());
        assertTrue(cells.contains(new Coordinate(0, 0)));
        assertTrue(cells.contains(new Coordinate(1, 0)));
    }

    @Test
    void l4BaseShapeIsCorrect() {
        Piece piece = factory.createPiece(PieceType.L4, 0);
        List<Coordinate> cells = piece.getCells();

        assertEquals(4, cells.size());
        assertTrue(cells.contains(new Coordinate(0, 0)));
        assertTrue(cells.contains(new Coordinate(1, 0)));
        assertTrue(cells.contains(new Coordinate(2, 0)));
        assertTrue(cells.contains(new Coordinate(0, 1)));
    }

    @Test
    void x5BaseShapeIsCorrect() {
        Piece piece = factory.createPiece(PieceType.X5, 0);
        List<Coordinate> cells = piece.getCells();

        assertEquals(5, cells.size());
        assertTrue(cells.contains(new Coordinate(0, 0)));
        assertTrue(cells.contains(new Coordinate(0, 1)));
        assertTrue(cells.contains(new Coordinate(0, -1)));
        assertTrue(cells.contains(new Coordinate(1, 0)));
        assertTrue(cells.contains(new Coordinate(-1, 0)));
    }

    // --- Piece size tests ---

    @Test
    void allPiecesHaveCorrectNumberOfCells() {
        for (PieceType type : PieceType.values()) {
            Piece piece = factory.createPiece(type, 0);
            int expectedSize = getExpectedSize(type);
            assertEquals(expectedSize, piece.getCells().size(),
                    "Wrong size for " + type);
        }
    }

    private int getExpectedSize(PieceType type) {
        String name = type.name();
        if (name.endsWith("1")) return 1;
        if (name.endsWith("2")) return 2;
        if (name.endsWith("3")) return 3;
        if (name.endsWith("4")) return 4;
        if (name.endsWith("5")) return 5;
        return -1;
    }

    // --- Orientation difference tests ---

    @Test
    void orientation0And1AreDifferent() {
        Piece o0 = factory.createPiece(PieceType.L4, 0);
        Piece o1 = factory.createPiece(PieceType.L4, 1);

        assertNotEquals(o0.getCells(), o1.getCells());
    }

    @Test
    void orientation0And4AreDifferent() {
        Piece o0 = factory.createPiece(PieceType.L4, 0);
        Piece o4 = factory.createPiece(PieceType.L4, 4);

        assertNotEquals(o0.getCells(), o4.getCells());
    }

    @Test
    void allOrientationsForL4AreDifferent() {
        for (int i = 0; i < 8; i++) {
            for (int j = i + 1; j < 8; j++) {
                Piece p1 = factory.createPiece(PieceType.L4, i);
                Piece p2 = factory.createPiece(PieceType.L4, j);
                assertNotEquals(p1.getCells(), p2.getCells(),
                        "Orientation " + i + " and " + j + " should be different");
            }
        }
    }

    // --- Rotation tests ---

    @Test
    void i2RotatesCorrectly() {
        Piece base = factory.createPiece(PieceType.I2, 0);
        Piece rotated = factory.createPiece(PieceType.I2, 1);

        // Base: (0,0), (1,0) - horizontal
        // Rotated 90°: (0,0), (0,-1) - vertical
        assertTrue(base.getCells().contains(new Coordinate(0, 0)));
        assertTrue(base.getCells().contains(new Coordinate(1, 0)));

        assertTrue(rotated.getCells().contains(new Coordinate(0, 0)));
        assertTrue(rotated.getCells().contains(new Coordinate(0, -1)));
    }

    @Test
    void fourRotationsPreservesSize() {
        for (PieceType type : PieceType.values()) {
            int baseSize = factory.createPiece(type, 0).getCells().size();
            for (int i = 0; i < 8; i++) {
                Piece piece = factory.createPiece(type, i);
                assertEquals(baseSize, piece.getCells().size(),
                        type + " orientation " + i + " has wrong size");
            }
        }
    }

    // --- All piece types exist ---

    @Test
    void allPieceTypesAreInFactory() {
        for (PieceType type : PieceType.values()) {
            Piece piece = factory.createPiece(type, 0);
            assertNotNull(piece, "Missing piece type: " + type);
            assertFalse(piece.getCells().isEmpty(),
                    "Empty piece for type: " + type);
        }
    }

    // --- Coordinate consistency ---

    @Test
    void allPiecesContainOrigin() {
        for (PieceType type : PieceType.values()) {
            Piece piece = factory.createPiece(type, 0);
            assertTrue(piece.getCells().contains(new Coordinate(0, 0)),
                    type + " base shape should contain origin");
        }
    }
}


