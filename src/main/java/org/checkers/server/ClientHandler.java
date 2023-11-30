package org.checkers.server;

import org.checkers.server.requests.ConnectionRequest;
import org.checkers.server.requests.StartGame;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class ClientHandler extends Thread {
    private Connection client;
    private HashMap<Integer, Connection> connections;

    private int id;
    public ClientHandler(Socket client, HashMap<Integer, Connection> connections, int id) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());

            this.client = new Connection(client, out, in);
            this.connections = connections;
            this.connections.put(id, this.client);
            this.id = id;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void run() {
        try {
            System.out.println("Client with ID: " + this.id + " Connected");

            // Send client their ID
            client.out.writeObject(id);

            Object input;
            while (true) {
                if (client.gameStarted) continue; // Game is already on going, wait until game is over

                input = client.in.readObject();

                System.out.println("RUNS " + id);
                if (input instanceof String s && s.equals("Game Started")) client.gameStarted = true;

                if (
                    input instanceof ConnectionRequest connectionRequest
                    && connections.containsKey(connectionRequest.id())
                    && !this.connections.get(connectionRequest.id()).gameStarted
                ) {
                    Connection client1Connection = client;
                    Connection client2Connection = connections.get(connectionRequest.id());

                    client1Connection.gameStarted = true;

                    new GameHandler(client1Connection, client2Connection).start();
                }
            }

        }
        catch (EOFException e) {
            System.out.println("Client with ID: " + this.id + " Disconnected");

            try {
                this.client.in.close();
                this.client.out.close();
                this.client.socket.close();
                this.connections.remove(this.id);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
