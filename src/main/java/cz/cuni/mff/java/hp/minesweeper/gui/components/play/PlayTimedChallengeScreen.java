package cz.cuni.mff.java.hp.minesweeper.gui.components.play;

import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;
import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalGameDetails;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.CustomOverlay;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.GUIUtils;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.InputField;

import javax.swing.*;
import java.util.concurrent.CompletableFuture;

/**
 * Class representing the screen where the user can choose the time limit for the time challenge.
 */
public class PlayTimedChallengeScreen {
    /**
     * Method that runs the screen.
     *
     * @return 0 if the user chooses to go back, 1 if the user chooses a time limit
     */
    public int run() {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        SwingUtilities.invokeLater(() -> {
            GlobalVariablesGUI.LAYERED_PANE.removeAll();
            GlobalVariablesGUI.LAYERED_PANE.revalidate();
            GlobalVariablesGUI.LAYERED_PANE.repaint();

            JPanel panel = GUIUtils.createPanel(true);

            JLabel title = GUIUtils.createLabel("Time Challenge", 50);
            GUIUtils.addLabelToTopOfPanel(panel, title);

            JLabel askUser = GUIUtils.createLabel("Choose time challenge length", 30);

            JButton time1 = GUIUtils.createButton("30 seconds", 450, 60);
            time1.addActionListener(_ -> {
                GlobalGameDetails.TIME_LIMIT = 30;
                future.complete(1);
            });
            JButton time2 = GUIUtils.createButton("1 minute", 450, 60);
            time2.addActionListener(_ -> {
                GlobalGameDetails.TIME_LIMIT = 60;
                future.complete(1);
            });
            JButton time3 = GUIUtils.createButton("2 minutes", 450, 60);
            time3.addActionListener(_ -> {
                GlobalGameDetails.TIME_LIMIT = 120;
                future.complete(1);
            });

            JLabel customTime = GUIUtils.createLabel("Custom time (seconds):", 30);
            InputField customTimeField = new InputField(
                    130,
                    50,
                    3,
                    GlobalGameDetails.TIME_LIMIT == -1 ? "" : String.valueOf(GlobalGameDetails.TIME_LIMIT),
                    35
            );

            JButton customTimeButton = GUIUtils.createButton("Ok", 200, 80);

            customTimeButton.addActionListener(_ -> {
                try {
                    // Retrieve text from the InputField's JTextField
                    String text = customTimeField.getTextField().getText();

                    GlobalGameDetails.TIME_LIMIT = Integer.parseInt(text);

                    if (GlobalGameDetails.TIME_LIMIT < 1) {
                        CustomOverlay.showOverlayWithMessage(
                                GlobalVariablesGUI.LAYERED_PANE,
                                "Number must be >0!",
                                2000,
                                40
                        );
                    } else {
                        future.complete(1);
                    }
                } catch (NumberFormatException ex) {
                    CustomOverlay.showOverlayWithMessage(
                            GlobalVariablesGUI.LAYERED_PANE,
                            "You must input a number!",
                            2000,
                            40
                    );
                }
            });


            JPanel customTimePanel = GUIUtils.createPanel(false);
            GUIUtils.addComponentsToPanel(customTimePanel, false, customTime, customTimeField, customTimeButton);

            GUIUtils.addComponentsToPanel(panel, false, askUser, time1, time2, time3, customTimePanel);

            GUIUtils.addBackButtonAtBottomOfPanel(panel, future);
        });

        return future.join();
    }
}
