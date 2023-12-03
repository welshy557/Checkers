package org.checkers.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {
    public final int id;
    public final Socket socket;
    public final ObjectOutputStream out;
    public final ObjectInputStream in;

    public volatile boolean gameStarted = false;

    public volatile boolean shouldTerminate = false;
    public Connection(int id, Socket socket, ObjectOutputStream out, ObjectInputStream in) {
        this.id = id;
        this.socket = socket;
        this.out = out;
        this.in = in;
    }

    @Override
    public String toString() {
        return String.format("id: %d, gameStarted: %b, shouldTerminate: %b", id, gameStarted, shouldTerminate);
    }
}
