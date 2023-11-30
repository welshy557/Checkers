package org.checkers;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;

public class Checker extends JComponent {
    public final Color color;
    public BoardPiece boardPiece;

    public HashMap<BoardPiece, List<BoardPiece>> moves;

    public boolean isKing = false;

    public Checker(Color color, BoardPiece boardPiece) {
        this.color = color;
        setPreferredSize(new Dimension(50, 50));
        this.boardPiece = boardPiece;

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!(color.equals(Board.playersColor) && color.equals(Board.playersTurn))) return;
                moves = getMoves();
                toggleSelectChecker(false);
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(this.color);

        int x = 0;
        int y = 0;

        g.fillOval(x, y, getWidth(), getHeight());

        if (isKing) {
            g.setColor(Color.YELLOW);

            y = (getHeight() / 2) + 5;
            // Draw the base of the crown
            g.fillRect(x + 15, y - 5, 20, 5);

            // Draw the top part of the crown
            int[] crownTopX = {x + 15, x + 20, x + 25, x + 30, x + 35};
            int[] crownTopY = {y - 5, y - 15, y - 10, y - 15, y - 5};
            g.fillPolygon(crownTopX, crownTopY, 5);

            // Draw small circles on the top part of the crown
            g.fillOval(x + 18, y - 18, 4, 4);
            g.fillOval(x + 28, y - 18, 4, 4);

        }
    }

    public void toggleSelectChecker(boolean recursive) {
        // Unselect current selected checker, if any
        if (Board.selectedChecker != null) {
            Checker selectedChecker = Board.selectedChecker;
            Board.selectedChecker = null;

            selectedChecker.toggleSelectChecker(true);

            // Do not reselect a checker being unselected
            if (this.equals(selectedChecker)) return;
        }


        if (Board.selectedChecker == null && !recursive) Board.selectedChecker = this;

        boardPiece.toggleSelect();

        for (BoardPiece b: moves.keySet()) {
            b.toggleSelect();
        }
    }

    public HashMap<BoardPiece, List<BoardPiece>> getMoves() {
        HashMap<BoardPiece, List<BoardPiece>> moves = new HashMap<>();
        boolean[][] visited = new boolean[Board.grid.length][Board.grid[0].length];
        BoardPiece[] diagnolSquares = boardPiece.getDiagnolSquares(color, isKing,true);

        for (int direction = 0; direction <= (isKing ? 3: 1); direction++) {
            if (diagnolSquares[direction] != null)
                getMoves(moves, new ArrayList<>(), diagnolSquares[direction], boardPiece, visited, direction);
        }

        this.moves = moves;
        return moves;
    }


    private void getMoves(
            HashMap<BoardPiece,
            List<BoardPiece>> moves,
            List<BoardPiece> piecesToRemove,
            BoardPiece current,
            BoardPiece prev,
            boolean[][] visited,
            int fromDirection
    ) {

        if (visited[current.row][current.col]) return;

        if (!prev.equals(boardPiece)) {

            // Two empty squares in a row
            if (prev.checker == null && current.checker == null) return;

            // Two non-empty squares in a row
            if (prev.checker != null && current.checker != null) {
                piecesToRemove.remove(piecesToRemove.size() - 1);
                return;
            }
        }

        visited[current.row][current.col] = true;
        Color opposingColor = color == Color.BLACK ? Color.RED : Color.BLACK;
        BoardPiece[] diagnolSquares = current.getDiagnolSquares(color, isKing,true);

        if (current.checker != null && current.checker.color == opposingColor && diagnolSquares[fromDirection] != null) {
            // Reached a piece being jumped
            piecesToRemove.add(current);

            // Keep exploring in same direction
            getMoves(moves, piecesToRemove, diagnolSquares[fromDirection], current, visited, fromDirection);
        }

        if (current.checker == null) {
            // Reached a valid jump
            moves.put(current, piecesToRemove);

            if (prev == boardPiece) return;

            // Keep exploring for further jumps in all valid directions
            for (int direction = 0; direction <= (isKing ? 3 : 1); direction++) {
                if (diagnolSquares[direction] != null) {
                    getMoves(moves, new ArrayList<>(piecesToRemove), diagnolSquares[direction], current, visited, direction);

                }
            }
        }
    }


    @Override
    public String toString() {
        return color.toString();
    }
}
