package cz.cuni.mff.java.hp.minesweeper.gui.components.play;

import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.AudioPlayer;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.CustomOverlay;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.GUIUtils;

import javax.swing.*;
import java.util.concurrent.CompletableFuture;

/**
 * Class representing the screen shown when the player loses a match.
 */
public class PlayLostMatchScreen {
    /**
     * Runs the screen.
     * @return 0 if the player wants to return to the menu, 1 if the player wants to restart the game
     */
    public int run() {
        if (AudioPlayer.getCurrentTheme() != 5) {
            AudioPlayer.stopMusic();
            AudioPlayer.playMusic("/audio/music/lose.wav");
        }
        CompletableFuture<Integer> future = new CompletableFuture<>();

        SwingUtilities.invokeLater(() -> {
            GlobalVariablesGUI.LAYERED_PANE.removeAll();
            GlobalVariablesGUI.LAYERED_PANE.revalidate();
            GlobalVariablesGUI.LAYERED_PANE.repaint();

            JPanel panel = GUIUtils.createPanel(true);

            JLabel lostLabel = GUIUtils.createLabel("You lost! Better luck next time... :(", 35, 5);

            JButton checkBoardButton = GUIUtils.createButton("Check board", 250, 35, 20);
            checkBoardButton.addActionListener(_ -> CustomOverlay.showBoardOverlayWhenMatchEnds(GlobalVariablesGUI.LAYERED_PANE));

            JButton restartButton = GUIUtils.createButton("Restart", 400, 60);
            restartButton.addActionListener(_ -> future.complete(1));

            JButton returnToMenuButton = GUIUtils.createButton("Return to Menu", 600, 60);
            returnToMenuButton.addActionListener(_ -> future.complete(0));

            GUIUtils.addComponentsToPanel(panel, false, lostLabel, (JComponent)Box.createVerticalStrut(15), checkBoardButton, restartButton, returnToMenuButton);

            GUIUtils.addBackgroundToLayeredPane(GlobalVariablesGUI.LAYERED_PANE, 3);
            GlobalVariablesGUI.LAYERED_PANE.add(panel, JLayeredPane.PALETTE_LAYER);

            GlobalVariablesGUI.FRAME.revalidate();
            GlobalVariablesGUI.FRAME.repaint();
        });

        return future.join();
    }
}
