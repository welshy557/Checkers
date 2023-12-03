package org.checkers.util;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AudioPlayer {
    private static void playSound(String filePath) {
        new Thread(() -> {
            try {
                ExtendedAdvancedPlayer player = new ExtendedAdvancedPlayer(new FileInputStream(filePath));
                player.play();
            } catch (JavaLayerException | FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
    public static void playCapture() {playSound("src/audio/capture.mp3");}

    public static void playPromote() {playSound("src/audio/promote.mp3");}

    public static void playMove() {playSound("src/audio/move.mp3");}


    private static class ExtendedAdvancedPlayer extends AdvancedPlayer {
        public ExtendedAdvancedPlayer(InputStream stream) throws JavaLayerException {
            super(stream);
            this.setPlayBackListener(new ExtendedPlaybackListener(this));
        }
    }

    private static class ExtendedPlaybackListener extends PlaybackListener {
        private final AdvancedPlayer player;
        public ExtendedPlaybackListener(AdvancedPlayer player) {
            super();
            this.player = player;
        }
        @Override
        public void playbackFinished(PlaybackEvent evt) {
            player.close();
        }
    }
}