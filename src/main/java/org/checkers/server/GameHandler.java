package org.checkers.server;

import org.checkers.server.requests.GameOver;
import org.checkers.server.requests.Move;
import org.checkers.server.requests.StartGame;

import java.awt.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class GameHandler extends Thread {

    private Connection client1;
    private Connection client2;
    private Connection clientsTurn;

    public GameHandler(Connection client1, Connection client2) {
        this.client1 = client1;
        this.client2 = client2;

        this.clientsTurn = client1;
    }
    public void run() {
        try {
            client1.out.writeObject(new StartGame(Color.BLACK));
            client2.out.writeObject(new StartGame(Color.RED));

            Object input;
            while ((input = clientsTurn.in.readObject()) != null) {
               if (input instanceof Move) {
                   ObjectOutputStream out = clientsTurn == client1 ? client2.out : client1.out;
                   out.writeObject(input);
                   out.flush();
                   clientsTurn = clientsTurn == client1 ? client2 : client1;

               }
               if (input instanceof GameOver) break;

            }

            client1.gameStarted = false;
            client2.gameStarted = false;

        } catch (IOException | ClassNotFoundException e) {
            if (!(e instanceof EOFException))
                e.printStackTrace();
        }
    }
}
