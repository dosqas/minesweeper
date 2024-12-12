package cz.cuni.mff.java.hp.minesweeper.gui.components.tutorial;

import cz.cuni.mff.java.hp.minesweeper.gui.components.statistics.StatisticsMainScreen;
import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.GUIUtils;

import javax.swing.*;
import java.util.concurrent.CompletableFuture;

/**
 * Tutorial main screen.
 */
public class TutorialMainScreen {
    /**
     * Runs the tutorial main screen.
     *
     * @return 0 if the user wants to see the "How to play" tutorial, 1 if the user wants to see the "PowerUps" tutorial
     */
    public int run() {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        SwingUtilities.invokeLater(() -> {
            GlobalVariablesGUI.LAYERED_PANE.removeAll();
            GlobalVariablesGUI.LAYERED_PANE.revalidate();
            GlobalVariablesGUI.LAYERED_PANE.repaint();

            JPanel panel = GUIUtils.createPanel(true);

            JLabel title = GUIUtils.createLabel("Tutorial", 50);
            GUIUtils.addLabelToTopOfPanel(panel, title);

            JButton howToPlay = GUIUtils.createButton("How to play", 500, 80);
            howToPlay.addActionListener(_ -> future.complete(0));
            JButton powerUpsTutorial = GUIUtils.createButton("PowerUps", 500, 80);
            StatisticsMainScreen.addButtonAtBottomOfMenu(future, panel, howToPlay, powerUpsTutorial);
        });

        return future.join();
    }
}
