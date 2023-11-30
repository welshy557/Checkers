package org.checkers;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Test {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5000);

        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

        out.writeObject("Hello World");

        socket.close();
    }
}
