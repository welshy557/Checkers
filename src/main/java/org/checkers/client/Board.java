package org.checkers;

import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    public final static BoardPiece[][] grid = new BoardPiece[8][8];
    public static Checker selectedChecker = null;
    public static Color playersTurn = Color.BLACK;

    public static Color playersColor;

    public static int redCheckerCount = 0;
    public static int blackCheckerCount = 0;


    public Board() {
        this.setLayout(new GridLayout(8, 8));
        this.setSize(BoardPiece.SIZE * grid.length, grid[0].length * BoardPiece.SIZE);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                BoardPiece boardPiece = new BoardPiece(row, col);
                this.add(boardPiece);
                grid[row][col] = boardPiece;
            }
        }
    }

    public static void flipTurn(Color color) {
        playersTurn = color == Color.black ? Color.RED : Color.BLACK;
        CheckerFrame.footer.playersTurn.setText(String.format("Turn: %s", Board.playersTurn == Color.BLACK ? "Black" : "Red"));
    }

    public static void setTurn(Color color) {
        playersTurn = color;
        CheckerFrame.footer.playersTurn.setText(String.format("Turn: %s", Board.playersTurn == Color.BLACK ? "Black" : "Red"));
    }

    public static void resetBoard(Color playersColor) {
        Board.playersColor = playersColor;
        if (playersColor.equals(Color.BLACK)) flipBoard();

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                BoardPiece boardPiece = grid[row][col];
                boardPiece.removeChecker();

                if ((row < 3 || row > 4) && (row + col) % 2 != 0) {
                    boardPiece.addChecker(new Checker(row < 3 ? Color.BLACK : Color.RED, boardPiece));
                }

            }
        }

        redCheckerCount = 12;
        blackCheckerCount = 12;
        setTurn(Color.BLACK);
    }


    public static void flipBoard() {
        // Reverse elements in each row
        for (BoardPiece[] row : grid) {
            ArrayUtil.reverseArray(row);
        }

        // Reverse the order of rows
        ArrayUtil.reverseArray(grid);

        // Update BoardPiece Row and Columns
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                BoardPiece boardPiece = grid[row][col];
                boardPiece.row = row;
                boardPiece.col = col;
            }
        }
    }
    public static void gameOver() {
        // TODO Implement game over logic
        System.out.println("GAME OVER");
    }
}