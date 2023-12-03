package org.checkers.server;

import org.checkers.server.requests.ConnectionRequest;
import org.checkers.server.requests.StartGame;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class ClientHandler extends Thread {
    private final Connection client;
    public ClientHandler(Socket client, int id) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());

            this.client = new Connection(id, client, out, in);
            Server.connections.put(id, this.client);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void run() {
        try {
            System.out.println("Client with ID: " + client.id + " Connected");

            // Send client their ID
            client.out.writeObject(client.id);

            Object input;
            while (true) {
                if (client.shouldTerminate) {
                    dispose();
                    break;
                }

                if (client.gameStarted) continue; // Game is already on going, wait until game is over

                input = client.in.readObject();

                if (input instanceof String s && s.equals("Game Started")) client.gameStarted = true;

                if (
                    input instanceof ConnectionRequest connectionRequest
                    && Server.connections.containsKey(connectionRequest.id())
                    && !Server.connections.get(connectionRequest.id()).gameStarted
                ) {


                    Connection client1Connection = client;
                    Connection client2Connection = Server.connections.get(connectionRequest.id());

                    client1Connection.gameStarted = true;

                    GameHandler handler = new GameHandler(client1Connection, client2Connection);
                    handler.start();
                }

            }

        }
        catch (EOFException e) {
            dispose();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void dispose() {
        System.out.println("Client with ID: " + client.id + " Disconnected");
        try {
            this.client.in.close();
            this.client.out.close();
            this.client.socket.close();
            Server.connections.remove(client.id);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
