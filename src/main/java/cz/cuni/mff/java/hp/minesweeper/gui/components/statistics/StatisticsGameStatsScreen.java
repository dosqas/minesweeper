package cz.cuni.mff.java.hp.minesweeper.gui.components.statistics;

import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;
import cz.cuni.mff.java.hp.minesweeper.utils.stats.StatsGame;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.GUIUtils;


import javax.swing.*;
import java.util.concurrent.CompletableFuture;

/**
 * Class for displaying game statistics screen.
 */
public class StatisticsGameStatsScreen {
    /**
     * Method for running the game statistics screen.
     * @return int representing the next screen to be displayed
     */
    public int run() {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        SwingUtilities.invokeLater(() -> {
            GlobalVariablesGUI.LAYERED_PANE.removeAll();
            GlobalVariablesGUI.LAYERED_PANE.revalidate();
            GlobalVariablesGUI.LAYERED_PANE.repaint();

            JPanel panel = GUIUtils.createPanel(true);

            JLabel title = GUIUtils.createLabel("Game stats", 50);
            GUIUtils.addLabelToTopOfPanel(panel, title);

            long wonMatches = StatsGame.getWonMatches();
            String wonMatchesString = Long.toString(wonMatches);
            long playedMatches = StatsGame.getPlayedMatches();
            String playedMatchesString = Long.toString(playedMatches);
            long winRate = StatsGame.getWinRate();
            String winRateString = Long.toString(winRate) + '%';
            long discoveredMines = StatsGame.getDiscoveredMines();
            String discoveredMinesString = Long.toString(discoveredMines);
            long discoveredNonMineTiles = StatsGame.getDiscoveredNonMineTiles();
            String discoveredNonMineTilesString = Long.toString(discoveredNonMineTiles);

            int longestLength = Math.max(Math.max(Math.max(Math.max(wonMatchesString.length(), playedMatchesString.length()), winRateString.length()), discoveredMinesString.length()), discoveredNonMineTilesString.length());
            String wonMatchesLabelString = "Won matches    | " + " ".repeat(longestLength - wonMatchesString.length()) + wonMatchesString;
            JLabel wonMatch = GUIUtils.createLabel(wonMatchesLabelString, 30);
            String playedMatchesLabelString = "Played matches | " + " ".repeat(longestLength - playedMatchesString.length()) + playedMatchesString;
            JLabel playedMatch = GUIUtils.createLabel(playedMatchesLabelString, 30);
            String winRateLabelString = "Win rate       | " + " ".repeat(longestLength - winRateString.length()) + winRateString;
            JLabel winRateLabel = GUIUtils.createLabel(winRateLabelString, 30);
            String discoveredMinesLabelString = "Found mines    | " + " ".repeat(longestLength - discoveredMinesString.length()) + discoveredMinesString;
            JLabel discoveredMinesLabel = GUIUtils.createLabel(discoveredMinesLabelString, 30);
            String discoveredNonMineTilesLabelString = "Cleared tiles  | " + " ".repeat(longestLength - discoveredNonMineTilesString.length()) + discoveredNonMineTilesString;
            JLabel discoveredNonMineTilesLabel = GUIUtils.createLabel(discoveredNonMineTilesLabelString, 30);

            GUIUtils.addComponentsToPanel(panel, false, wonMatch, playedMatch, winRateLabel, discoveredMinesLabel, discoveredNonMineTilesLabel);

            GUIUtils.addBackButtonAtBottomOfPanel(panel, future);

        });

        return future.join();
    }
}
