package org.checkers;

import org.checkers.server.Util;

import javax.swing.*;
import java.awt.*;

public class ArrayUtil extends JPanel {
    public final static BoardPiece[][] grid = new BoardPiece[8][8];
    public static Checker selectedChecker = null;
    public static Color playersTurn = Color.BLACK;

    public static Color playersColor;

    public static int redCheckerCount = 0;
    public static int blackCheckerCount = 0;


    public ArrayUtil() {
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


//    @Override
//    public void paintComponent(Graphics g){
//        if (Board.playersColor != null && Board.playersColor.equals(Color.BLACK)) {
//            Graphics2D g2d = (Graphics2D) g;
//            int w2 = getWidth() / 2;
//            int h2 = getHeight() / 2;
//            g2d.rotate(Math.PI, w2, h2);
//        }
//
//        super.paintComponent(g);
//    }

    public static void flipTurn(Color color) {
        playersTurn = color == Color.black ? Color.RED : Color.BLACK;
        CheckerFrame.footer.playersTurn.setText(String.format("Turn: %s", ArrayUtil.playersTurn == Color.BLACK ? "Black" : "Red"));
    }

    public static void setTurn(Color color) {
        playersTurn = color;
        CheckerFrame.footer.playersTurn.setText(String.format("Turn: %s", ArrayUtil.playersTurn == Color.BLACK ? "Black" : "Red"));
    }

    public static void resetBoard(Color playersColor) {
        ArrayUtil.playersColor = playersColor;

        if (playersColor.equals(Color.RED)) {
            Util.flipMatrix();
        }



        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                BoardPiece boardPiece = grid[row][col];
                boardPiece.removeChecker();

                if ((row < 3 || row > 4) && (row + col) % 2 != 0) {
                    boardPiece.addChecker(new Checker(row < 3 ? Color.BLACK : Color.RED, boardPiece));
                }

            }
        }

//        CheckerFrame.board.repaint();

        redCheckerCount = 12;
        blackCheckerCount = 12;
        setTurn(Color.BLACK);
    }

    public static void flipBoard() {

    }
    public static void gameOver() {
        // TODO Implement game over logic
        System.out.println("GAME OVER");
    }
}
