package views;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class BackgroundMusic {
    private static BackgroundMusic instance;
    private MediaPlayer mediaPlayer;
    private boolean mediaPlaying;

    private BackgroundMusic() {
        // Initialize the MediaPlayer here
        Media sound = new Media(new File("../assignment2/Games/Group_37_Final/sounds/Background Music.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlaying = false;
    }

    public static BackgroundMusic getInstance() {
        if (instance == null) {
            instance = new BackgroundMusic();
        }
        return instance;
    }

    public void playBackgroundMusic() {
        mediaPlayer.setVolume(0.7);
        mediaPlayer.play();
        mediaPlaying = true;
    }

    public void adjustVolume(double volume) {
        mediaPlayer.setVolume(volume);
    }

    public void stopBackgroundMusic() {
        mediaPlayer.stop();
        mediaPlaying = false;
    }

    public boolean isMediaPlaying() {
        return mediaPlaying;
    }
}
