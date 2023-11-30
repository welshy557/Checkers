package org.checkers;

import org.checkers.server.requests.Move;
import org.checkers.server.requests.StartGame;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private ObjectInputStream in;
    public ObjectOutputStream out;

    public Integer id;
    public Client() {
        try {
            Socket socket = new Socket("localhost", 5000);

            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            // Upon connection, server will send back an integer representing the clients ID to have other clients join
            this.id = (Integer) in.readObject();
            CheckerFrame.footer.clientID.setText(String.valueOf(this.id));

            new ClientThread(socket, in, out).start();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static class ClientThread extends Thread {
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        public ClientThread(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
            this.socket = socket;
            this.in = in;
            this.out = out;
        }

        @Override
        public void run() {
            try {
                Object input;

                while (true) {
                    input = in.readObject();

                    if (input instanceof StartGame startGame) {
                        Board.resetBoard(startGame.playersColor());
                        out.writeObject("Game Started");
                    }

                    if (input instanceof Move move) {
                        BoardPiece fromBoardPiece = Board.grid[move.fromRow()][move.fromCol()];
                        BoardPiece toBoardPiece = Board.grid[move.toRow()][move.toCol()];

                        fromBoardPiece.checker.getMoves();

                        fromBoardPiece.checker.toggleSelectChecker(false);
                        fromBoardPiece.checker.toggleSelectChecker(false);

                        BoardPiece.makeMove(fromBoardPiece, toBoardPiece);
                    }
                }
            } catch (EOFException e) {
                System.out.println("Disconnected From Server");

                try {
                    in.close();
                    out.close();
                    socket.close();
                } catch (IOException ex){
                    throw new RuntimeException(ex);
                }

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
