package cz.cuni.mff.java.hp.minesweeper.gui.components;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.concurrent.CompletableFuture;

import cz.cuni.mff.java.hp.minesweeper.utils.gui.GUIUtils;
import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;
import org.jetbrains.annotations.NotNull;

/**
 * The `MainMenu` class represents the main menu of the Minesweeper game.
 * It allows the user to navigate to different parts of the game.
 */
public class MainMenu {
    /**
     * Runs the main menu and displays the available options.
     *
     * @return the code of the selected option
     */
    public int run() {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        SwingUtilities.invokeLater(() -> {
            GlobalVariablesGUI.LAYERED_PANE.removeAll();
            GlobalVariablesGUI.LAYERED_PANE.revalidate();
            GlobalVariablesGUI.LAYERED_PANE.repaint();

            JButton playButton = GUIUtils.createButton("Play", 500, 80);
            playButton.addActionListener(_ -> future.complete(0));
            JButton tutorialButton = GUIUtils.createButton("Tutorial",500, 80);
            tutorialButton.addActionListener(_ -> future.complete(1));
            JButton statisticsButton = GUIUtils.createButton("Statistics",500, 80);
            statisticsButton.addActionListener(_ -> future.complete(2));
            JButton settingsButton = GUIUtils.createButton("Settings",500, 80);
            settingsButton.addActionListener(_ -> future.complete(3));
            JButton exitButton = GUIUtils.createButton("Exit", 500, 80);
            exitButton.addActionListener(_ -> future.complete(4));

            JLabel label = getTitleLabel();

            JPanel panel = GUIUtils.createPanel(true);

            Box verticalBox = Box.createVerticalBox();
            verticalBox.add(Box.createVerticalGlue());
            verticalBox.add(label);
            verticalBox.add(Box.createVerticalGlue());

            panel.add(verticalBox);

            GUIUtils.addComponentsToPanel(panel, false, playButton, tutorialButton, statisticsButton, settingsButton, exitButton);

            GUIUtils.addBackgroundToLayeredPane(GlobalVariablesGUI.LAYERED_PANE, 0);
            GlobalVariablesGUI.LAYERED_PANE.add(panel, JLayeredPane.PALETTE_LAYER);

            GlobalVariablesGUI.FRAME.revalidate();
            GlobalVariablesGUI.FRAME.repaint();
        });

        return future.join();
    }

    /**
     * Creates a label with the title of the main menu.
     *
     * @return the created label
     */
    private static @NotNull JLabel getTitleLabel() {
        JLabel label = new JLabel(" Minesweeper ");
        label.setFont(GlobalVariablesGUI.FONT.deriveFont((float) 70));
        label.setForeground(Color.WHITE);
        Border border = BorderFactory.createLineBorder(Color.WHITE, 5);
        label.setBorder(border);
        label.setBackground(new Color(0, 0, 0, 100));

        label.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
        label.setOpaque(true);
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        return label;
    }
}
