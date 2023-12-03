package org.checkers.client;

import org.checkers.server.requests.ConnectionRequest;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;

public class Footer extends JPanel {
    JLabel clientID;
    JLabel playersTurn;

    JLabel connectionIdLabel;
    JTextArea connectionID;
    JButton connectButton;
    public Footer() {
        super();
        setLayout(new FlowLayout(FlowLayout.CENTER, 50, 5));
        this.setSize(Board.grid.length * BoardPiece.SIZE, 50);

        clientID = new JLabel("Connecting To Server...");
        this.add(clientID);


        playersTurn = new JLabel(String.format("Turn: %s", Board.playersTurn == Color.BLACK ? "Black" : "Red"));
        playersTurn.setVisible(false);
        this.add(playersTurn);

        connectionIdLabel = new JLabel("Game ID:");
        this.add(connectionIdLabel);

        connectionID = new JTextArea();
        connectionID.setColumns(3);
        connectionID.setBorder(new LineBorder(Color.BLACK, 1));
        connectionID.setMaximumSize(connectionID.getSize());

        this.add(connectionID);


        connectButton = new JButton("Connect");
        connectButton.addActionListener(e ->
                Main.client.out(new ConnectionRequest(Integer.parseInt(connectionID.getText())))
        );

        this.add(connectButton);
    }

    public void updateFooter(boolean gameStarted) {
        playersTurn.setVisible(gameStarted);
        connectionIdLabel.setVisible(!gameStarted);
        connectionID.setVisible(!gameStarted);
        clientID.setVisible(!gameStarted);
        connectButton.setVisible(!gameStarted);
    }
}
