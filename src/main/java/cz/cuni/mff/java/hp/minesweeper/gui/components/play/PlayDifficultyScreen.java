package cz.cuni.mff.java.hp.minesweeper.gui.components.play;

import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;
import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalGameDetails;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.CustomOverlay;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.GUIUtils;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.InputField;

import javax.swing.*;
import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;

/**
 * Class representing the screen where the user can choose the difficulty of the game.
 */
public class PlayDifficultyScreen {
    /**
     * Method that runs the screen.
     * @return 0 if the user wants to go back, 1 if the user wants to start the game, 2 if the user wants to go back to the main menu.
     */
    public int run() {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        SwingUtilities.invokeLater(() -> {
            GlobalVariablesGUI.LAYERED_PANE.removeAll();
            GlobalVariablesGUI.LAYERED_PANE.revalidate();
            GlobalVariablesGUI.LAYERED_PANE.repaint();

            JPanel panel = GUIUtils.createPanel(true);

            JLabel title = GUIUtils.createLabel("Difficulty", 50);
            panel.add(Box.createVerticalStrut(10));
            title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            panel.add(title);

            JLabel askUser = GUIUtils.createLabel("Choose game difficulty:", 40);

            JButton easy = GUIUtils.createButton("Easy", 450, 55);
            easy.addActionListener(_ -> {
                GlobalGameDetails.DIFFICULTY = 0;
                future.complete(1);
            });

            JButton medium = GUIUtils.createButton("Medium", 450, 55);
            medium.addActionListener(_ -> {
                GlobalGameDetails.DIFFICULTY = 1;
                future.complete(1);
            });

            JButton hard = GUIUtils.createButton("Hard", 450, 55);
            hard.addActionListener(_ -> {
                GlobalGameDetails.DIFFICULTY = 2;
                future.complete(1);
            });

            JLabel customDifficulty = GUIUtils.createLabel("Custom difficulty", 30);

            JLabel customDifficultyLabel = GUIUtils.createLabel("Board width:", 15);
            InputField customDifficultyWidthField = new InputField(80, 40, 2, "", 25);
            JLabel customDifficultyLabel2 = GUIUtils.createLabel("Board height:", 15);
            InputField customDifficultyHeightField = new InputField(80, 40, 2, "", 25);
            JLabel customDifficultyLabel3 = GUIUtils.createLabel("Number of mines:", 15);
            InputField customDifficultyMinesField = new InputField(120, 40, 4, "", 25);

            JPanel customDifficultyPanel = GUIUtils.createPanel(false);
            JPanel widthPanel = GUIUtils.createPanel(false);
            JPanel heightPanel = GUIUtils.createPanel(false);
            JPanel minesPanel = GUIUtils.createPanel(false);

            GUIUtils.addComponentsToPanel(widthPanel, false, customDifficultyLabel, customDifficultyWidthField);
            GUIUtils.addComponentsToPanel(heightPanel, false, customDifficultyLabel2, customDifficultyHeightField);
            GUIUtils.addComponentsToPanel(minesPanel, false, customDifficultyLabel3, customDifficultyMinesField);
            GUIUtils.addComponentsToPanel(customDifficultyPanel, false, widthPanel, heightPanel, minesPanel);


            JButton startGame = GUIUtils.createButton("Start Custom", 600, 55);
            startGame.addActionListener(_ -> {
                try {
                    int width = Integer.parseInt(customDifficultyWidthField.getTextField().getText());
                    int height = Integer.parseInt(customDifficultyHeightField.getTextField().getText());
                    int mines = Integer.parseInt(customDifficultyMinesField.getTextField().getText());
                    if (width < 1) {
                        CustomOverlay.showOverlayWithMessage(
                                GlobalVariablesGUI.LAYERED_PANE,
                                "Width must be >0!",
                                2000,
                                40
                        );
                    }
                    else if (height < 1) {
                        CustomOverlay.showOverlayWithMessage(
                                GlobalVariablesGUI.LAYERED_PANE,
                                "Height must be >0!",
                                2000,
                                40
                        );
                    }
                    else if (width * height < 3) {
                        CustomOverlay.showOverlayWithMessage(
                                GlobalVariablesGUI.LAYERED_PANE,
                                "Board must have at least 3 squares!",
                                2000,
                                25
                        );
                    }
                    else if (mines <= 0) {
                        CustomOverlay.showOverlayWithMessage(
                                GlobalVariablesGUI.LAYERED_PANE,
                                "Mines must be >0!",
                                2000,
                                40
                        );
                    }
                    else if (mines >= width * height - 1) {
                        String message = MessageFormat.format("Mines must be < {0}!", width * height - 1);
                        CustomOverlay.showOverlayWithMessage(
                                GlobalVariablesGUI.LAYERED_PANE,
                                message,
                                2000,
                                40
                        );
                    }
                    else {
                        GlobalGameDetails.DIFFICULTY = 3;
                        GlobalGameDetails.WIDTH = width;
                        GlobalGameDetails.HEIGHT = height;
                        GlobalGameDetails.MINE_COUNT = mines;
                        future.complete(1);
                    }
                } catch (NumberFormatException ex) {
                    CustomOverlay.showOverlayWithMessage(
                            GlobalVariablesGUI.LAYERED_PANE,
                            "Please out fill all the fields with numbers!",
                            2000,
                            25
                    );
                }
            });

            panel.add(Box.createVerticalGlue());
            JButton backButton = GUIUtils.createButton("Back", 500, 80);

            if (GlobalGameDetails.TIME_LIMIT != -1) {
                backButton.addActionListener(_ -> future.complete(2));
            } else {
                backButton.addActionListener(_ -> future.complete(0));
            }

            GUIUtils.addComponentsToPanel(panel, false, askUser, easy, medium, hard , customDifficulty, customDifficultyPanel, startGame);

            panel.add(GUIUtils.createHorizontalBoxForComponent(backButton));

            panel.add(Box.createVerticalGlue());

            GUIUtils.addBackgroundToLayeredPane(GlobalVariablesGUI.LAYERED_PANE, 0);
            GlobalVariablesGUI.LAYERED_PANE.add(panel, JLayeredPane.PALETTE_LAYER);

            GlobalVariablesGUI.FRAME.revalidate();
            GlobalVariablesGUI.FRAME.repaint();
        });

        return future.join();
    }
}
