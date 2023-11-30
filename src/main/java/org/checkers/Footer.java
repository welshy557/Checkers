package org.checkers;

import org.checkers.server.Connection;
import org.checkers.server.requests.ConnectionRequest;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Footer extends JPanel {
    JLabel clientID;
    JLabel playersTurn;
    JTextArea connectionID;

    JButton connectButton;
    public Footer() {
        super();
        setLayout(new FlowLayout(FlowLayout.CENTER, 50, 5)); // Adjust the gap values as needed
        this.setSize(Board.grid.length * BoardPiece.SIZE, 50);

        clientID = new JLabel("Connecting To Server...");
        this.add(clientID);


        playersTurn = new JLabel(String.format("Turn: %s", Board.playersTurn == Color.BLACK ? "Black" : "Red"));
        this.add(playersTurn);

        connectionID = new JTextArea();
        connectionID.setColumns(5);
        this.add(connectionID);


        connectButton = new JButton("Connect");
        connectButton.addActionListener(e -> {
            try {
                Main.client.out.writeObject(new ConnectionRequest(Integer.parseInt(connectionID.getText())));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        this.add(connectButton);
    }
}
