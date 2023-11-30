package org.checkers.client;


import java.awt.*;
import javax.swing.*;


public class CheckerFrame {
    public static Board board;
    public static Footer footer;


    public CheckerFrame() {
        JFrame frame = new JFrame("Checkers");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        board = new Board();
        footer = new Footer();

        frame.getContentPane().add(board);
        frame.getContentPane().add(footer, BorderLayout.SOUTH);

        frame.setSize(board.getSize().width, board.getSize().height + footer.getSize().height);

        frame.setResizable(false);
        frame.setVisible(true);


    }

}