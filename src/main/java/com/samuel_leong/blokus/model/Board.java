package com.samuel_leong.blokus.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private PlayerColor[][] grid;


    public Board() {
        this.grid = new PlayerColor[20][20];

        for (int row = 0; row <20; row++) {
            for (int col = 0; col < 20; col++) {
                grid[row][col] = PlayerColor.EMPTY;
            }
        }
    }

    public boolean isEmpty(Coordinate coord) {
        return grid[coord.row()][coord.col()] == PlayerColor.EMPTY;
    }
    public PlayerColor getCell(Coordinate coord) {
        return grid[coord.row()][coord.col()];
    }

    public void setCell(Coordinate coord, PlayerColor player) {
        grid[coord.row()][coord.col()] = player;
    }

    public List<CellState> getGrid() {
        List<CellState> occupiedCellsList = new ArrayList<>();

        for (int row = 0; row<20; row++) {
            for (int col=0; col<20; col++) {
                PlayerColor colorOnSquare = grid[row][col];
                if (colorOnSquare != PlayerColor.EMPTY) {
                    CellState cellState = new CellState(row, col, colorOnSquare);
                    occupiedCellsList.add(cellState);
                }
            }
        }
        return occupiedCellsList;
    }
}
