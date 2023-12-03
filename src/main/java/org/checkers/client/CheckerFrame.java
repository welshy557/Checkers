package org.checkers.client;


import org.checkers.util.AudioPlayer;

import java.awt.*;
import javax.swing.*;


public class CheckerFrame extends JFrame {
    public static Board board;
    public static Footer footer;

    public CheckerFrame() {
        this.setTitle("Checkers");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        board = new Board();
        footer = new Footer();

        this.getContentPane().add(board);
        this.getContentPane().add(footer, BorderLayout.SOUTH);

        this.setSize(board.getSize().width, board.getSize().height + footer.getSize().height);

        this.setResizable(false);
        this.setVisible(true);
    }

    @Override
    public void dispose() {
        Main.client.dispose();
        super.dispose();
    }



}