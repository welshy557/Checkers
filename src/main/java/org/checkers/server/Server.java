package org.checkers.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
    public static volatile HashMap<Integer, Connection> connections = new HashMap<>();
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(5000);
    }

    private ServerSocket serverSocket;


    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Listening on port " + port);
        int count = 0;
        while (true) {
            Socket client = serverSocket.accept();
            new ClientHandler(client, count++).start();

        }
    }
}
