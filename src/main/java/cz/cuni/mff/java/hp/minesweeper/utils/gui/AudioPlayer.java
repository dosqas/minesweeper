package cz.cuni.mff.java.hp.minesweeper.utils.gui;

import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The `AudioPlayer` class handles playing and managing background music and sound effects for the Minesweeper game.
 */
public class AudioPlayer {
    private static Clip musicClip;
    private static Clip sfxClip;
    private static int currentTheme = -1;

    /**
     * Stops the currently playing music.
     */
    public static void stopMusic() {
        if (musicClip != null) {
            musicClip.stop();
            musicClip.close();
        }
    }

    /**
     * Plays the specified music file.
     *
     * @param filePath the path to the music file
     */
    public static void playMusic(String filePath) {
        if (GlobalVariablesGUI.MUTE_MUSIC == 1) {
            return;
        }
        try {
            switch (filePath) {
                case "/audio/music/mainMenu.wav" -> currentTheme = 0;
                case "/audio/music/game1.wav" -> currentTheme = 1;
                case "/audio/music/game2.wav" -> currentTheme = 2;
                case "/audio/music/game3.wav" -> currentTheme = 3;
                case "/audio/music/win.wav" -> currentTheme = 4;
                case "/audio/music/lose.wav" -> currentTheme = 5;
                case "/audio/music/paused.wav" -> currentTheme = 6;
                case "/audio/music/timed.wav" -> currentTheme = 7;
            }

            try (InputStream audioStream = AudioPlayer.class.getResourceAsStream(filePath)) {
                if (audioStream == null) {
                    throw new IOException("Audio file not found: " + filePath);
                }

                byte[] audioData = audioStream.readAllBytes();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audioData);

                musicClip = AudioSystem.getClip();
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(byteArrayInputStream);
                stopMusic();
                musicClip.open(audioInputStream);

                setMusicVolume(GlobalVariablesGUI.MUSIC_VOLUME);

                musicClip.start();
            }

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the volume for the currently playing music.
     *
     * @param volume the volume level (0-100)
     */
    public static void setMusicVolume(int volume) {
        setVolumeForClip(volume, musicClip);
    }

    /**
     * Plays the specified sound effect file.
     *
     * @param filePath the path to the sound effect file
     */
    public static void playSFX(String filePath) {
        if (GlobalVariablesGUI.MUTE_SFX == 1) {
            return;
        }
        try {
            try (InputStream audioStream = AudioPlayer.class.getResourceAsStream(filePath)) {
                if (audioStream == null) {
                    throw new IOException("Audio file not found: " + filePath);
                }

                byte[] audioData = audioStream.readAllBytes();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audioData);

                Clip newSFXClip = AudioSystem.getClip();

                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(byteArrayInputStream);
                newSFXClip.open(audioInputStream);

                setSFXVolume(GlobalVariablesGUI.SFX_VOLUME, newSFXClip);

                newSFXClip.start();

                sfxClip = newSFXClip;
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the volume for the currently playing sound effect.
     *
     * @param volume the volume level (0-100)
     */
    public static void setSFXVolume(int volume) {
        setVolumeForClip(volume, sfxClip);
    }

    /**
     * Sets the volume for the specified sound effect clip.
     *
     * @param volume the volume level (0-100)
     * @param clip the sound effect clip
     */
    public static void setSFXVolume(int volume, Clip clip) {
        setVolumeForClip(volume, clip);
    }

    /**
     * Sets the volume for the specified clip.
     *
     * @param volume the volume level (0-100)
     * @param clip the audio clip
     */
    private static void setVolumeForClip(int volume, Clip clip) {
        if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume / 100.0) / Math.log(10.0) * 20.0);
            volumeControl.setValue(dB);
        }
    }

    /**
     * Gets the current theme being played.
     *
     * @return the current theme index
     */
    public static int getCurrentTheme() {
        return currentTheme;
    }
}