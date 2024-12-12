package cz.cuni.mff.java.hp.minesweeper.gui.components.settings;

import cz.cuni.mff.java.hp.minesweeper.utils.gui.GUIUtils;
import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;

import javax.swing.*;
import java.util.concurrent.CompletableFuture;

/**
 * Main screen for the settings.
 */
public class SettingsMainScreen {
    /**
     * Runs the main screen for the settings.
     *
     * @return the index of the button that was clicked
     */
    public int run() {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        SwingUtilities.invokeLater(() -> {
            GlobalVariablesGUI.LAYERED_PANE.removeAll();
            GlobalVariablesGUI.LAYERED_PANE.revalidate();
            GlobalVariablesGUI.LAYERED_PANE.repaint();

            JButton powerUpsButton = GUIUtils.createButton("Power-ups", 500, 80);
            powerUpsButton.addActionListener(_ -> future.complete(0));
            JButton themesButton = GUIUtils.createButton("Themes", 500, 80);
            themesButton.addActionListener(_ -> future.complete(1));
            JButton controlsButton = GUIUtils.createButton("Controls", 500, 80);
            controlsButton.addActionListener(_ -> future.complete(2));
            JButton audioButton = GUIUtils.createButton("Audio", 500, 80);
            audioButton.addActionListener(_ -> future.complete(3));
            JButton backButton = GUIUtils.createButton("Back", 500, 80);
            backButton.addActionListener(_ -> future.complete(4));

            JPanel panel = GUIUtils.createPanel(true);

            JLabel title = GUIUtils.createLabel("Settings", 50);
            GUIUtils.addLabelToTopOfPanel(panel, title);


            GUIUtils.addComponentsToPanel(panel, false, powerUpsButton, themesButton, controlsButton, audioButton, backButton);

            GUIUtils.addBackgroundToLayeredPane(GlobalVariablesGUI.LAYERED_PANE, 1);
            GlobalVariablesGUI.LAYERED_PANE.add(panel, JLayeredPane.PALETTE_LAYER);

            GlobalVariablesGUI.FRAME.revalidate();
            GlobalVariablesGUI.FRAME.repaint();
        });

        return future.join();
    }
}
