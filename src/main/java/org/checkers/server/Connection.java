package org.checkers.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {
    public int id;

    public Socket socket;
    public ObjectOutputStream out;
    public ObjectInputStream in;

    public boolean gameStarted = false;

    public Connection(Socket socket, ObjectOutputStream out, ObjectInputStream in) {
        this.socket = socket;
        this.out = out;
        this.in = in;
    }
}
