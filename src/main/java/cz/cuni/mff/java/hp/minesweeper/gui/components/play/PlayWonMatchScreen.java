package cz.cuni.mff.java.hp.minesweeper.gui.components.play;

import cz.cuni.mff.java.hp.minesweeper.service.Service;
import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalGameDetails;
import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.AudioPlayer;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.CustomOverlay;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.GUIUtils;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.InputField;
import cz.cuni.mff.java.hp.minesweeper.utils.stats.StatsGame;

import javax.swing.*;
import java.util.concurrent.CompletableFuture;

import static java.lang.Math.max;

/**
 * Class representing the screen that is shown when the player wins a match.
 */
public class PlayWonMatchScreen {
    /**
     * Method that runs the screen.
     * @return 0
     */
    public int run() {
        if (AudioPlayer.getCurrentTheme() != 4) {
            AudioPlayer.stopMusic();
            AudioPlayer.playMusic("/audio/music/win.wav");
        }
        StatsGame.setWonMatches(StatsGame.getWonMatches() + 1);
        GlobalGameDetails.TIME_LIMIT = -1;
        CompletableFuture<Integer> future = new CompletableFuture<>();

        SwingUtilities.invokeLater(() -> {
            GlobalVariablesGUI.LAYERED_PANE.removeAll();
            GlobalVariablesGUI.LAYERED_PANE.revalidate();
            GlobalVariablesGUI.LAYERED_PANE.repaint();

            JPanel panel = GUIUtils.createPanel(true);

            JLabel wonLabel = GUIUtils.createLabel("Congratulations! You won! :)", 35);
            JLabel nameLabel = null;
            InputField nameField = null;
            JButton submitButton = null;
            int score = -1;

            if (GlobalGameDetails.DIFFICULTY != 3) {
                score = (int) (2 * GlobalGameDetails.DIFFICULTY + 1.75 * (max(300 - GlobalGameDetails.FINAL_TIME, 0)) + 0.25 * (GlobalGameDetails.WIDTH * GlobalGameDetails.HEIGHT - GlobalGameDetails.MINE_COUNT) - 0.5 * GlobalGameDetails.MINE_COUNT);

                if (Service.isATopScore(score)) {
                    nameLabel = GUIUtils.createLabel("TopScore! Enter your name:", 30);
                    nameField = new InputField(330,
                            50,
                            8,
                            "",
                            35);

                    submitButton = GUIUtils.createButton("Submit", 300, 50);

                    InputField finalNameField = nameField;
                    int finalScore = score;
                    submitButton.addActionListener(_ -> {
                        try {
                            String finalName = finalNameField.getTextField().getText();
                            if (finalName.length() < 3 || finalName.length() > 8) {
                                CustomOverlay.showOverlayWithMessage(GlobalVariablesGUI.LAYERED_PANE, "Name must be between 3 and 8 characters!", 2000, 20);
                            } else {
                                Service.addTopScore(finalName, GlobalGameDetails.FINAL_TIME, finalScore);
                                future.complete(0);
                            }
                        }
                        catch (Exception e) {
                            CustomOverlay.showOverlayWithMessage(GlobalVariablesGUI.LAYERED_PANE, "Name must be between 3 and 8 characters!", 2000, 20);
                        }
                    });
                }
            }

            JButton checkBoardButton = GUIUtils.createButton("Check board", 250, 35, 20);
            checkBoardButton.addActionListener(_ -> CustomOverlay.showBoardOverlayWhenMatchEnds(GlobalVariablesGUI.LAYERED_PANE));

            JButton returnToMenuButton = GUIUtils.createButton("Return to Menu", 600, 60);
            returnToMenuButton.addActionListener(_ -> future.complete(0));

            if (GlobalGameDetails.DIFFICULTY != 3 && Service.isATopScore(score)) {
                GUIUtils.addComponentsToPanel(panel, false, wonLabel, (JComponent)Box.createVerticalStrut(15), nameLabel, nameField, submitButton, (JComponent)Box.createVerticalStrut(15), checkBoardButton, returnToMenuButton);
            } else {
                GUIUtils.addComponentsToPanel(panel, false, wonLabel, (JComponent)Box.createVerticalStrut(15), checkBoardButton, returnToMenuButton);
            }

            GUIUtils.addBackgroundToLayeredPane(GlobalVariablesGUI.LAYERED_PANE, 2);
            GlobalVariablesGUI.LAYERED_PANE.add(panel, JLayeredPane.PALETTE_LAYER);

            GlobalVariablesGUI.FRAME.revalidate();
            GlobalVariablesGUI.FRAME.repaint();
        });

        return future.join();
    }
}
