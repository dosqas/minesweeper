package cz.cuni.mff.java.hp.minesweeper.gui.components.settings;

import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.GUIUtils;

import javax.swing.*;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents the screen where the user can select the theme option.
 */
public class SettingsThemesScreen {
    private JPanel panel;
    private JButton classicButton;
    private JButton darkButton;
    private JButton desertButton;
    private JButton winterButton;
    private JButton pinkButton;
    private JButton catsButton;

    /**
     * This method is used to run the screen.
     *
     * @return code when returning from the screen
     */
    public int run() {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        SwingUtilities.invokeLater(() -> {
            GlobalVariablesGUI.LAYERED_PANE.removeAll();
            GlobalVariablesGUI.LAYERED_PANE.revalidate();
            GlobalVariablesGUI.LAYERED_PANE.repaint();

            panel = GUIUtils.createPanel(true);

            JLabel title = GUIUtils.createLabel("Themes", 50);
            GUIUtils.addLabelToTopOfPanel(panel, title);

            JLabel themeLabel = GUIUtils.createLabel("Choose a theme for the game:", 30);

            classicButton = GUIUtils.createButton("Classic", 550, 50);
            classicButton.addActionListener(_ -> {
                GlobalVariablesGUI.THEME = 0;
                future.complete(1);
            });

            darkButton = GUIUtils.createButton("Dark", 550, 50);
            darkButton.addActionListener(_ -> {
                GlobalVariablesGUI.THEME = 1;
                future.complete(1);
            });

            desertButton = GUIUtils.createButton("Desert", 550, 50);
            desertButton.addActionListener(_ -> {
                GlobalVariablesGUI.THEME = 2;
                future.complete(1);
            });

            winterButton = GUIUtils.createButton("Winter", 550, 50);
            winterButton.addActionListener(_ -> {
                GlobalVariablesGUI.THEME = 3;
                future.complete(1);
            });

            pinkButton = GUIUtils.createButton("Pink", 550, 50);
            pinkButton.addActionListener(_ -> {
                GlobalVariablesGUI.THEME = 4;
                future.complete(1);
            });

            catsButton = GUIUtils.createButton("Cats", 550, 50);
            catsButton.addActionListener(_ -> {
                GlobalVariablesGUI.THEME = 5;
                future.complete(1);
            });

            switch(GlobalVariablesGUI.THEME) {
                case 0 -> classicButton.setText("[X] Classic");
                case 1 -> darkButton.setText("[X] Dark");
                case 2 -> desertButton.setText("[X] Desert");
                case 3 -> winterButton.setText("[X] Winter");
                case 4 -> pinkButton.setText("[X] Pink");
                case 5 -> catsButton.setText("[X] Cats");
            }

            GUIUtils.addComponentsToPanel(panel, false, themeLabel, classicButton, darkButton, desertButton, winterButton, pinkButton, catsButton);

            GUIUtils.addBackButtonAtBottomOfPanel(panel, future);
        });

        return future.join();
    }
}