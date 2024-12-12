package cz.cuni.mff.java.hp.minesweeper.gui.components.statistics;

import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.GUIUtils;

import javax.swing.*;
import java.util.concurrent.CompletableFuture;

/**
 * The main screen for the statistics menu.
 */
public class StatisticsMainScreen {
    /**
     * Runs the statistics menu.
     *
     * @return 0 if the user wants to see the top scores, 1 if the user wants to see the game stats, 2 if the user wants to go back
     */
    public int run() {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        SwingUtilities.invokeLater(() -> {
            GlobalVariablesGUI.LAYERED_PANE.removeAll();
            GlobalVariablesGUI.LAYERED_PANE.revalidate();
            GlobalVariablesGUI.LAYERED_PANE.repaint();

            JPanel panel = GUIUtils.createPanel(true);

            JLabel title = GUIUtils.createLabel("Statistics", 50);
            GUIUtils.addLabelToTopOfPanel(panel, title);

            JButton topScores = GUIUtils.createButton("Top Scores", 500, 80);
            topScores.addActionListener(_ -> future.complete(0));
            JButton gameStats = GUIUtils.createButton("Game Stats", 500, 80);
            addButtonAtBottomOfMenu(future, panel, topScores, gameStats);
        });

        return future.join();
    }

    /**
     * Adds the buttons to the bottom of the menu.
     *
     * @param future the future to complete when a button is pressed
     * @param panel the panel to add the buttons to
     * @param topScores the button to show the top scores
     * @param gameStats the button to show the game stats
     */
    public static void addButtonAtBottomOfMenu(CompletableFuture<Integer> future, JPanel panel, JButton topScores, JButton gameStats) {
        gameStats.addActionListener(_ -> future.complete(1));
        JButton back = GUIUtils.createButton("Back", 500, 80);
        addBackAtBottom(future, panel, topScores, gameStats, back);
    }

    /**
     * Adds the back button to the bottom of the menu.
     *
     * @param future the future to complete when the back button is pressed
     * @param panel the panel to add the back button to
     * @param topScores the button to show the top scores
     * @param gameStats the button to show the game stats
     * @param back the back button
     */
    public static void addBackAtBottom(CompletableFuture<Integer> future, JPanel panel, JButton topScores, JButton gameStats, JButton back) {
        back.addActionListener(_ -> future.complete(2));

        GUIUtils.addComponentsToPanel(panel, false, topScores, gameStats, back);

        GUIUtils.addBackgroundToLayeredPane(GlobalVariablesGUI.LAYERED_PANE, 1);
        GlobalVariablesGUI.LAYERED_PANE.add(panel, JLayeredPane.PALETTE_LAYER);

        GlobalVariablesGUI.FRAME.revalidate();
        GlobalVariablesGUI.FRAME.repaint();
    }
}
