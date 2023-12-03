package org.checkers.client;

import org.checkers.server.requests.GameOver;
import org.checkers.util.ArrayUtil;

import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    public final static BoardPiece[][] grid = new BoardPiece[8][8];
    public static Checker selectedChecker = null;
    public static Color playersTurn = Color.BLACK;

    public static Color playersColor;
    public static Color playersPrevColor;

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

        Board.setGameStarted(true);
        Board.playersColor = playersColor;
        redCheckerCount = 12;
        blackCheckerCount = 12;


        // If player's first game is black, flip the board.
        // If player's later game color is not the same as their prev game color, flip the board
        if ((playersColor.equals(Color.BLACK) && playersPrevColor == null) || (playersPrevColor != null && !playersColor.equals(playersPrevColor)))
            flipBoard();



        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                BoardPiece boardPiece = grid[row][col];
                boardPiece.removeChecker(false);

                if ((row < 3 || row > 4) && (row + col) % 2 != 0) {
                    boardPiece.addChecker(new Checker(row < 3 ? Color.BLACK : Color.RED, boardPiece));
                }
            }
        }


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

    private static void setGameStarted(boolean started) {
        playersPrevColor = playersColor;
        CheckerFrame.footer.updateFooter(started);
    }

    public static void gameOver() {
        Main.client.out(new GameOver());
        setGameStarted(false);
    }
}
