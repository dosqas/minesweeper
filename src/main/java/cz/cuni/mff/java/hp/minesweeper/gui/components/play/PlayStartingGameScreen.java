package cz.cuni.mff.java.hp.minesweeper.gui.components.play;

import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalGameDetails;
import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.AudioPlayer;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.GUIUtils;
import cz.cuni.mff.java.hp.minesweeper.utils.stats.StatsGame;

import javax.swing.*;
import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;

/**
 * Class representing the screen that is shown when the game is starting.
 */
public class PlayStartingGameScreen {
    /**
     * Method that runs the screen.
     *
     * @return 0
     */
    public int run() {
        AudioPlayer.stopMusic();
        AudioPlayer.playSFX("/audio/sfx/startingGame.wav");
        StatsGame.setPlayedMatches(StatsGame.getPlayedMatches() + 1);
        CompletableFuture<Integer> future = new CompletableFuture<>();

        SwingUtilities.invokeLater(() -> {
            GlobalVariablesGUI.LAYERED_PANE.removeAll();
            GlobalVariablesGUI.LAYERED_PANE.revalidate();
            GlobalVariablesGUI.LAYERED_PANE.repaint();

            JPanel panel = GUIUtils.createPanel(true);

            String text = "Starting ";
            switch (GlobalGameDetails.DIFFICULTY) {
                case 0 -> text += "Easy Game...";
                case 1 -> text += "Medium Game...";
                case 2 -> text += "Hard Game...";
                case 3 -> text += MessageFormat.format("Custom Game ({0}x{1}, {2} mines)...",
                        GlobalGameDetails.WIDTH, GlobalGameDetails.HEIGHT, GlobalGameDetails.MINE_COUNT);
            }
            JLabel startingGame;
            if (GlobalGameDetails.DIFFICULTY == 3) {
                startingGame = GUIUtils.createLabel(text, 31);
            }
            else startingGame = GUIUtils.createLabel(text, 40);

            if (GlobalGameDetails.TIME_LIMIT > 0) {
                String text2 = MessageFormat.format("Timed Challenge! ({0} seconds)...", GlobalGameDetails.TIME_LIMIT);
                JLabel timedChallenge = GUIUtils.createLabel(text2, 40);
                GUIUtils.addComponentsToPanel(panel, false, startingGame, timedChallenge);
            }
            else GUIUtils.addComponentsToPanel(panel, true, startingGame);

            GUIUtils.addBackgroundToLayeredPane(GlobalVariablesGUI.LAYERED_PANE, 1);
            GlobalVariablesGUI.LAYERED_PANE.add(panel, JLayeredPane.PALETTE_LAYER);

            GlobalVariablesGUI.FRAME.revalidate();
            GlobalVariablesGUI.FRAME.repaint();

            future.complete(0);
        });

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return future.join();
    }
}
