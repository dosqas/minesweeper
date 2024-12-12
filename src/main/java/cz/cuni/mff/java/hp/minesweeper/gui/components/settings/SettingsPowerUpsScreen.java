package cz.cuni.mff.java.hp.minesweeper.gui.components.settings;

import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;
import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalGameDetails;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.GUIUtils;

import javax.swing.*;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents the screen where the user can select the power-ups option.
 */
public class SettingsPowerUpsScreen {
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

            JPanel panel = GUIUtils.createPanel(true);

            JLabel title = GUIUtils.createLabel("PowerUps", 50);
            GUIUtils.addLabelToTopOfPanel(panel, title);

            JLabel powerUpsLabel = GUIUtils.createLabel("Select a power-up option:", 30);

            JButton noPowerUpsButton = GUIUtils.createButton("No power-ups", 750, 50);
            noPowerUpsButton.addActionListener(_ -> {
                GlobalGameDetails.POWER_UP = 0;
                future.complete(1);
            });

            JButton twoExtraLivesButton = GUIUtils.createButton("2 Extra-Lives", 750, 50);
            twoExtraLivesButton.addActionListener(_ -> {
                GlobalGameDetails.POWER_UP = 1;
                future.complete(1);
            });

            JButton shieldButton = GUIUtils.createButton("Shield", 750, 50);
            shieldButton.addActionListener(_ -> {
                GlobalGameDetails.POWER_UP = 2;
                future.complete(1);
            });

            JButton revealButton = GUIUtils.createButton("Reveal", 750, 50);
            revealButton.addActionListener(_ -> {
                GlobalGameDetails.POWER_UP = 3;
                future.complete(1);
            });

            JButton threeSafeStepsButton = GUIUtils.createButton("3 Safe-Steps", 750, 50);
            threeSafeStepsButton.addActionListener(_ -> {
                GlobalGameDetails.POWER_UP = 4;
                future.complete(1);
            });

            switch (GlobalGameDetails.POWER_UP) {
                case 0 -> noPowerUpsButton.setText("[X] No power-ups");
                case 1 -> twoExtraLivesButton.setText("[X] 2 Extra-Lives");
                case 2 -> shieldButton.setText("[X] Shield");
                case 3 -> revealButton.setText("[X] Reveal");
                case 4 -> threeSafeStepsButton.setText("[X] 3 Safe-Steps");
            }

            GUIUtils.addComponentsToPanel(panel, false, powerUpsLabel, noPowerUpsButton, twoExtraLivesButton, shieldButton, revealButton, threeSafeStepsButton);


            GUIUtils.addBackButtonAtBottomOfPanel(panel, future);
        });

        return future.join();
    }
}
