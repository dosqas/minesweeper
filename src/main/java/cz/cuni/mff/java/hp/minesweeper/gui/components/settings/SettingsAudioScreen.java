package cz.cuni.mff.java.hp.minesweeper.gui.components.settings;

import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.AudioPlayer;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.GUIUtils;

import javax.swing.*;
import java.util.concurrent.CompletableFuture;

/**
 * Class representing the audio settings screen.
 */
public class SettingsAudioScreen {
    /**
     * Method that runs the audio settings screen.
     *
     * @return the integer value of the future
     */
    public int run() {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        SwingUtilities.invokeLater(() -> {
            GlobalVariablesGUI.LAYERED_PANE.removeAll();
            GlobalVariablesGUI.LAYERED_PANE.revalidate();
            GlobalVariablesGUI.LAYERED_PANE.repaint();

            JPanel panel = GUIUtils.createPanel(true);

            JLabel title = GUIUtils.createLabel("Audio", 50);
            GUIUtils.addLabelToTopOfPanel(panel, title);

            JLabel sfxLabel = GUIUtils.createLabel(" SFX ", 30);
            JPanel sfxSlider = GUIUtils.createSlider(0, 100, GlobalVariablesGUI.SFX_VOLUME, 400, 30, 0);

            JButton sfxMuteButton = GUIUtils.createButton(" Mute ", 250, 60);
            if (GlobalVariablesGUI.MUTE_SFX == 1) {
                sfxMuteButton.setText("Unmute");
            }
            sfxMuteButton.addActionListener(_ -> {
                GlobalVariablesGUI.MUTE_SFX = 1 - GlobalVariablesGUI.MUTE_SFX;
                if (sfxMuteButton.getText().equals(" Mute ")) {
                    sfxMuteButton.setText("Unmute");
                } else {
                    sfxMuteButton.setText(" Mute ");
                }
            });

            JLabel musicLabel = GUIUtils.createLabel("Music", 30);
            JPanel musicSlider = GUIUtils.createSlider(0, 100, GlobalVariablesGUI.MUSIC_VOLUME, 400, 30, 1);

            JButton musicMuteButton = GUIUtils.createButton(" Mute ", 250, 60);
            if (GlobalVariablesGUI.MUTE_MUSIC == 1) {
                musicMuteButton.setText("Unmute");
            }
            musicMuteButton.addActionListener(_ -> {
                GlobalVariablesGUI.MUTE_MUSIC = 1 - GlobalVariablesGUI.MUTE_MUSIC;
                if (musicMuteButton.getText().equals(" Mute ")) {
                    musicMuteButton.setText("Unmute");
                    AudioPlayer.stopMusic();
                } else {
                    musicMuteButton.setText(" Mute ");
                    AudioPlayer.playMusic("/audio/music/mainMenu.wav");
                }
            });


            JPanel sfxPanel = GUIUtils.createPanel(false);
            GUIUtils.addComponentsToPanel(sfxPanel, true, sfxLabel, sfxSlider, sfxMuteButton);

            JPanel musicPanel = GUIUtils.createPanel(false);
            GUIUtils.addComponentsToPanel(musicPanel, true, musicLabel, musicSlider, musicMuteButton);

            GUIUtils.addComponentsToPanel(panel, false, sfxPanel, musicPanel);


            GUIUtils.addBackButtonAtBottomOfPanel(panel, future);
        });

        return future.join();
    }
}
