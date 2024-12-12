package cz.cuni.mff.java.hp.minesweeper.gui.components.exit;

import cz.cuni.mff.java.hp.minesweeper.utils.gui.AudioPlayer;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.GUIUtils;
import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;

import javax.swing.*;
import java.util.concurrent.CompletableFuture;

/**
 * Screen that asks the user if they really want to exit the game.
 */
public class ExitConfirmScreen {
    /**
     * Runs the screen.
     *
     * @return 0 if the user wants to exit, 1 otherwise
     */
    public int run() {
        AudioPlayer.stopMusic();
        CompletableFuture<Integer> future = new CompletableFuture<>();

        SwingUtilities.invokeLater(() -> {
            GlobalVariablesGUI.LAYERED_PANE.removeAll();
            GlobalVariablesGUI.LAYERED_PANE.revalidate();
            GlobalVariablesGUI.LAYERED_PANE.repaint();

            JButton yesButton = GUIUtils.createButton("Yes", 500, 80);
            yesButton.addActionListener(_ -> future.complete(0));
            JButton noButton = GUIUtils.createButton("No", 500, 80);
            noButton.addActionListener(_ -> future.complete(1));

            JPanel panel = GUIUtils.createPanel(true);

            JLabel label = GUIUtils.createLabel("Do you really want to exit?", 40);
            label.setAlignmentX(JLabel.CENTER_ALIGNMENT);

            Box verticalBox = Box.createVerticalBox();
            verticalBox.add(Box.createVerticalGlue());
            verticalBox.add(label);

            panel.add(verticalBox);

            GUIUtils.addComponentsToPanel(panel, true, yesButton, noButton);

            GUIUtils.addBackgroundToLayeredPane(GlobalVariablesGUI.LAYERED_PANE, 1);
            GlobalVariablesGUI.LAYERED_PANE.add(panel, JLayeredPane.PALETTE_LAYER);

            GlobalVariablesGUI.FRAME.revalidate();
            GlobalVariablesGUI.FRAME.repaint();
        });

        return future.join();
    }
}
