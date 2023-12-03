package org.checkers.client;

import org.checkers.util.AudioPlayer;

public class Main {
    public static Client client;
    public static AudioPlayer audioPlayer;
    public static void main(String[] args) {
        new CheckerFrame();
        client = new Client();
        audioPlayer = new AudioPlayer();
    }
}
