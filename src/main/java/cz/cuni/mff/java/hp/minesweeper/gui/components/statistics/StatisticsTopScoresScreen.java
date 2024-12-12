package cz.cuni.mff.java.hp.minesweeper.gui.components.statistics;

import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;
import cz.cuni.mff.java.hp.minesweeper.utils.game.Score;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.GUIUtils;
import cz.cuni.mff.java.hp.minesweeper.utils.stats.StatsTopScores;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Class representing the screen with the top scores.
 */
public class StatisticsTopScoresScreen {
    private static int currentDifficulty = 0;
    private static JPanel scoresPanel;
    private static JPanel mainPanel;
    private static JLabel[] scoreLabels;
    private static JLabel difficultyLabel;

    /**
     * Creates the panel with the top scores.
     */
    private static void initializeScoresPanel() {
        makeScoresPanel();
        scoresPanel.setLayout(new BoxLayout(scoresPanel, BoxLayout.Y_AXIS));

        JLabel mainDifficultyLabel = GUIUtils.createLabel("Difficulty:", 25);
        difficultyLabel = GUIUtils.createLabel("      ", 25);
        JPanel difficultyPanel = GUIUtils.createPanel(false);
        difficultyPanel.add(mainDifficultyLabel);
        difficultyPanel.add(Box.createHorizontalStrut(10));
        difficultyPanel.add(difficultyLabel);
        mainPanel.add(GUIUtils.createHorizontalBoxForComponent(difficultyPanel));

        JLabel infoLabel = GUIUtils.createLabel("    Name    |  Time  |  Score  ", 30);
        infoLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
        scoresPanel.add(GUIUtils.createHorizontalBoxForComponent(infoLabel));
        scoresPanel.add(Box.createVerticalStrut(3));

        for (int i = 0; i < scoreLabels.length; i++) {
            JLabel label = GUIUtils.createLabel("                               ", 30);
            label.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
            scoreLabels[i] = label;
            scoresPanel.add(GUIUtils.createHorizontalBoxForComponent(label));
        }
    }

    /**
     * Creates the panel with the top scores.
     */
    private static void makeScoresPanel() {
        new SwingWorker<List<Score>, Void>() {
            @Override
            protected List<Score> doInBackground() {
                switch (currentDifficulty) {
                    case 0 -> { return StatsTopScores.getEasy(); }
                    case 1 -> { return StatsTopScores.getMedium(); }
                    case 2 -> { return StatsTopScores.getHard(); }
                    default -> throw new IllegalStateException("Unexpected difficulty: " + currentDifficulty);
                }
            }

            @Override
            protected void done() {
                try {
                    switch (currentDifficulty) {
                        case 0 -> difficultyLabel.setText("Easy");
                        case 1 -> difficultyLabel.setText("Medium");
                        case 2 -> difficultyLabel.setText("Hard");
                        default -> throw new IllegalStateException("Unexpected difficulty: " + currentDifficulty);
                    }
                    difficultyLabel.revalidate();
                    difficultyLabel.repaint();

                    List<Score> scores = get();
                    SwingUtilities.invokeLater(() -> {
                        for (int i = 0; i < scoreLabels.length; i++) {
                            Score score = i < scores.size() ? scores.get(i) : new Score("N/A", 0L, 0L); // Fallback if fewer scores

                            String name = score.getName().substring(0, Math.min(score.getName().length(), 8));
                            name += " ".repeat(8 - name.length());

                            String entry = makeEntries(score, name);
                            scoreLabels[i].setText(entry);
                        }
                        scoresPanel.revalidate();
                        scoresPanel.repaint();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    /**
     * Creates the entries for the top scores.
     *
     * @param score The score to create the entry for.
     * @param name The name of the player.
     * @return The entry for the top scores.
     */
    private static @NotNull String makeEntries(Score score, String name) {
        String time = score.getTime().toString();
        if (time.length() > 3) {
            time = ">999";
        }
        else time = " ".repeat(4 - time.length()) + time;

        String scoreValue = score.getScore().toString();
        if (scoreValue.length() > 3) {
            scoreValue = ">999";
        }
        else scoreValue = " ".repeat(4 - scoreValue.length()) + scoreValue;

        return MessageFormat.format("  {0}  |  {1}  |   {2}  ",
                name, time, scoreValue);
    }

    /**
     * Runs the screen with the top scores.
     *
     * @return The result of the screen.
     */
    public int run() {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        SwingUtilities.invokeLater(() -> {
            GlobalVariablesGUI.LAYERED_PANE.removeAll();
            GlobalVariablesGUI.LAYERED_PANE.revalidate();
            GlobalVariablesGUI.LAYERED_PANE.repaint();
            scoresPanel = GUIUtils.createPanel(true);
            scoreLabels = new JLabel[5];
            if (mainPanel != null)
                mainPanel.removeAll();

            mainPanel = GUIUtils.createPanel(true);

            JLabel topScores = GUIUtils.createLabel("Top Scores", 50);
            GUIUtils.addLabelToTopOfPanel(mainPanel, topScores);

            initializeScoresPanel();

            mainPanel.add(GUIUtils.createHorizontalBoxForComponent(scoresPanel));

            JButton rightButton = GUIUtils.createButton(">", 120, 120);
            rightButton.addActionListener(_ -> {
                if (currentDifficulty < 2) {
                    currentDifficulty++;
                    makeScoresPanel();
                }
            });
            JButton leftButton = GUIUtils.createButton("<", 120, 120);
            leftButton.addActionListener(_ -> {
                if (currentDifficulty > 0) {
                    currentDifficulty--;
                    makeScoresPanel();
                }
            });

            JPanel buttonPanel = GUIUtils.createPanel(false);
            buttonPanel.add(leftButton);
            buttonPanel.add(Box.createHorizontalGlue());
            buttonPanel.add(rightButton);
            mainPanel.add(Box.createVerticalStrut(10));
            mainPanel.add(GUIUtils.createHorizontalBoxForComponent(buttonPanel));

            GUIUtils.addBackButtonAtBottomOfPanel(mainPanel, future);
        });

        return future.join();
    }

}