package org.checkers;


import org.checkers.server.requests.Move;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

public class BoardPiece extends JPanel  {
    private final BoardPieceMouseListener listener = new BoardPieceMouseListener();
    public static int SIZE = 75;
    public int row;
    public int col;
    public boolean isSelected = false;
    private final Color color;
    public Checker checker;
    public BoardPiece(int row, int col) {
        this.row = row;
        this.col = col;


        this.setLayout(new GridBagLayout());
        this.setSize(SIZE, SIZE);
        this.color = (row + col) % 2 == 0 ? Color.WHITE : Color.GRAY;
        this.setBackground(this.color);

    }

    public void addChecker(Checker checker) {
        this.checker = checker;
        checker.boardPiece = this;
        this.add(checker);
        this.revalidate();
        this.repaint();
    }
    public void removeChecker() {
        if (checker == null) return;

        this.remove(this.checker);
        this.revalidate();
        this.repaint();

        Checker removedChecker = this.checker;
        removedChecker.boardPiece = null;
        this.checker = null;

        if (removedChecker.color == Color.BLACK) Board.blackCheckerCount--;
        else Board.redCheckerCount--;

        if (Board.redCheckerCount == 0 || Board.blackCheckerCount == 0) Board.gameOver();

    }
    public void toggleSelect() {
        this.isSelected = !this.isSelected;
        this.setBackground(isSelected ? Color.YELLOW : this.color);
        this.revalidate();
        this.repaint();

        if (this.isSelected) this.addMouseListener(this.listener);
        else this.removeMouseListener(this.listener);
    }

    public BoardPiece[] getDiagnolSquares(Color color, boolean isKing, boolean ignoreBlockingCheckers) {
        BoardPiece[] diagnolSquares = new BoardPiece[4];

        if (color == Color.BLACK ? this.row < Board.grid.length - 1 : this.row > 0) {
            int row = color == Color.BLACK ? this.row + 1 : this.row - 1;

            if (this.col > 0) {
                BoardPiece left = Board.grid[row][this.col - 1];
                if (left.checker == null || ignoreBlockingCheckers) diagnolSquares[0] = left;

            }

            if (this.col < Board.grid[0].length - 1) {
                BoardPiece right = Board.grid[row][this.col + 1];
                if (right.checker == null || ignoreBlockingCheckers) diagnolSquares[1] = right;
            }
        }

        // If checker is a king, get other two diagnols
        if (isKing) {
            int kingRow = color == Color.BLACK ?  this.row - 1 : this.row + 1;

            if (color == Color.BLACK ? this.row > 0 : this.row < Board.grid.length - 1) {
                if (this.col > 0) {
                    BoardPiece left = Board.grid[kingRow][this.col - 1];
                    if (left.checker == null || ignoreBlockingCheckers) diagnolSquares[2] = left;

                }

                if (this.col < Board.grid[0].length - 1) {
                    BoardPiece right = Board.grid[kingRow][this.col + 1];
                    if (right.checker == null || ignoreBlockingCheckers) diagnolSquares[3] = right;
                }
            }
        }

        return diagnolSquares;
    }


    public static void makeMove(BoardPiece fromBoardPiece, BoardPiece toBoardPiece) {
        Checker selectedChecker = fromBoardPiece.checker;
        if (selectedChecker == null) throw new NullPointerException("Selected Checker is null");

        fromBoardPiece.removeChecker();
        toBoardPiece.addChecker(selectedChecker);

        Board.flipTurn(selectedChecker.color);

        // Check for king promotion
        if (selectedChecker.color == Color.BLACK ? toBoardPiece.row == Board.grid.length - 1 : toBoardPiece.row == 0) {
            selectedChecker.isKing = true;
        }

        // Remove pieces that were jumped from board
        List<BoardPiece> checkersToRemove = selectedChecker.moves.get(toBoardPiece);

        if (checkersToRemove == null) return;

        for (BoardPiece b: checkersToRemove) {
            b.removeChecker();
        }



    }

    @Override
    public String toString() {
        return String.format(
                "Row: %d, Col: %d, Checker: %s",
                row, col, checker == null ? "null" : checker.toString()
        );
    }


    static class BoardPieceMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            Checker selectedChecker = Board.selectedChecker;
            selectedChecker.toggleSelectChecker(false);

            BoardPiece fromBoardPiece = selectedChecker.boardPiece;
            BoardPiece toBoardPiece = (BoardPiece) e.getComponent();

            // Send move to opponent
            try {
                Main.client.out.writeObject(new Move(fromBoardPiece.row, fromBoardPiece.col, toBoardPiece.row, toBoardPiece.col));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            makeMove(fromBoardPiece, toBoardPiece);

        }
    }
}
