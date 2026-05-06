package com.project.jdr.services;

import java.net.URL;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public final class AudioManager {

    private static final String DEFAULT_BGM   = "/audio/bgm.mp3";
    private static final String DEFAULT_CLICK = "/audio/click.mp3";

    private static MediaPlayer backgroundPlayer;
    private static AudioClip   clickClip;

    private static double backgroundVolume = 0.30;
    private static double clickVolume      = 0.60;

    private AudioManager() {
    }

    public static void startBackground() {
        ensureBackgroundPlayer();
        if (backgroundPlayer != null) {
            backgroundPlayer.play();
        }
    }

    public static double getBackgroundVolume() {
        return backgroundVolume;
    }

    public static void setBackgroundVolume(double volume) {
        backgroundVolume = clamp01(volume);
        if (backgroundPlayer != null) {
            backgroundPlayer.setVolume(backgroundVolume);
        }
    }

    public static double getClickVolume() {
        return clickVolume;
    }

    public static void setClickVolume(double volume) {
        clickVolume = clamp01(volume);
        if (clickClip != null) {
            clickClip.setVolume(clickVolume);
        }
    }

    public static void stopBackground() {
        if (backgroundPlayer != null) {
            backgroundPlayer.stop();
        }
    }

    public static void applyToScene(Scene scene) {
        if (scene == null) {
            return;
        }
        ensureClickClip();
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, AudioManager::handleMouseClick);
    }

    private static void handleMouseClick(MouseEvent event) {
        Object target = event.getTarget();
        if (!(target instanceof Node)) {
            return;
        }
        Node node = (Node) target;
        while (node != null) {
            if (node instanceof ButtonBase) {
                playClick();
                return;
            }
            node = node.getParent();
        }
    }

    private static void playClick() {
        if (clickClip != null) {
            clickClip.play(clickVolume);
        }
    }

    private static void ensureBackgroundPlayer() {
        if (backgroundPlayer != null) {
            return;
        }
        Media media = loadMedia(DEFAULT_BGM);
        if (media == null) {
            return;
        }
        backgroundPlayer = new MediaPlayer(media);
        backgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundPlayer.setVolume(backgroundVolume);
    }

    private static void ensureClickClip() {
        if (clickClip != null) {
            return;
        }
        AudioClip clip = loadClip(DEFAULT_CLICK);
        if (clip == null) {
            return;
        }
        clickClip = clip;
        clickClip.setVolume(clickVolume);
    }

    private static Media loadMedia(String resourcePath) {
        URL url = AudioManager.class.getResource(resourcePath);
        if (url == null) {
            return null;
        }
        try {
            return new Media(url.toExternalForm());
        } catch (Exception ex) {
            return null;
        }
    }

    private static AudioClip loadClip(String resourcePath) {
        URL url = AudioManager.class.getResource(resourcePath);
        if (url == null) {
            return null;
        }
        try {
            return new AudioClip(url.toExternalForm());
        } catch (Exception ex) {
            return null;
        }
    }

    private static double clamp01(double value) {
        if (value < 0.0) {
            return 0.0;
        }
        if (value > 1.0) {
            return 1.0;
        }
        return value;
    }
}
