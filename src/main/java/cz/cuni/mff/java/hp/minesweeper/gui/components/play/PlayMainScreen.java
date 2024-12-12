package cz.cuni.mff.java.hp.minesweeper.gui.components.play;

import cz.cuni.mff.java.hp.minesweeper.gui.components.statistics.StatisticsMainScreen;
import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalGameDetails;
import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.GUIUtils;

import javax.swing.*;
import java.util.concurrent.CompletableFuture;

/**
 * Class representing the main screen for the Play section.
 */
public class PlayMainScreen {
    /**
     * Runs the screen.
     * @return 0 if the player wants to play a standard game, 1 if the player wants to play a time challenge
     */
    public int run() {
        GlobalGameDetails.TIME_LIMIT = -1;
        CompletableFuture<Integer> future = new CompletableFuture<>();

        SwingUtilities.invokeLater(() -> {
            GlobalVariablesGUI.LAYERED_PANE.removeAll();
            GlobalVariablesGUI.LAYERED_PANE.revalidate();
            GlobalVariablesGUI.LAYERED_PANE.repaint();

            JPanel panel = GUIUtils.createPanel(true);

            JLabel title = GUIUtils.createLabel("Play", 50);
            GUIUtils.addLabelToTopOfPanel(panel, title);

            JButton standardGame = GUIUtils.createButton("Standard Game", 600, 80);
            standardGame.addActionListener(_ -> future.complete(0));
            JButton timeChallenge = GUIUtils.createButton("Time Challenge", 600, 80);
            timeChallenge.addActionListener(_ -> future.complete(1));
            JButton back = GUIUtils.createButton("Back", 600, 80);
            StatisticsMainScreen.addBackAtBottom(future, panel, standardGame, timeChallenge, back);
        });

        return future.join();
    }
}
