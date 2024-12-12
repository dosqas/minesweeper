package cz.cuni.mff.java.hp.minesweeper.gui.components.tutorial;

import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.GUIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CompletableFuture;

/**
 * Tutorial screen for the power-ups in the game.
 */
public class TutorialPowerUpsScreen {
    /**
     * Runs the tutorial screen for the power-ups.
     *
     * @return 0 if the back button was pressed
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

            JLabel label1 = GUIUtils.createLabel("In this version of the game there are 4 available power-ups:     ", 18, 15);
            label1.setBorder(BorderFactory.createMatteBorder(5, 5, 1, 5, Color.WHITE));
            panel.add(GUIUtils.createHorizontalBoxForComponent(label1));

            JLabel label2 = GUIUtils.createLabel("[1] 2 Extra-Lives - If you reveal a mine accidentally, you will  ", 18, 15);
            label2.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 5, Color.WHITE));
            panel.add(GUIUtils.createHorizontalBoxForComponent(label2));

            JLabel label2_1 = GUIUtils.createLabel("not lose the game. You only get 2, try to use them mindfully!    ", 18, 15);
            label2_1.setBorder(BorderFactory.createMatteBorder(0, 5, 1, 5, Color.WHITE));
            panel.add(GUIUtils.createHorizontalBoxForComponent(label2_1));

            JLabel label3 = GUIUtils.createLabel("[2] Shield - If you reveal a mine accidentally, you get a second ", 18, 15);
            label3.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 5, Color.WHITE));
            panel.add(GUIUtils.createHorizontalBoxForComponent(label3));

            JLabel label3_1 = GUIUtils.createLabel("chance and you also reveal the tiles around the exploded mine.   ", 18, 15);
            label3_1.setBorder(BorderFactory.createMatteBorder(0, 5, 1, 5, Color.WHITE));
            panel.add(GUIUtils.createHorizontalBoxForComponent(label3_1));

            JLabel label4 = GUIUtils.createLabel("[3] Reveal - Reveals a random mine on the board.                 ", 18, 15);
            label4.setBorder(BorderFactory.createMatteBorder(0, 5, 1, 5, Color.WHITE));
            panel.add(GUIUtils.createHorizontalBoxForComponent(label4));

            JLabel label5 = GUIUtils.createLabel("[4] 3 Safe-Steps - You get graced for your next tile reveal: if  ", 18, 15);
            label5.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 5, Color.WHITE));
            panel.add(GUIUtils.createHorizontalBoxForComponent(label5));

            JLabel label5_1 = GUIUtils.createLabel("you reveal a mine, you will not lose the game. Otherwise, you    ", 18, 15);
            label5_1.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 5, Color.WHITE));
            panel.add(GUIUtils.createHorizontalBoxForComponent(label5_1));

            JLabel label5_2 = GUIUtils.createLabel("will waste it. You only get 3, be smart with their use!          ", 18, 15);
            label5_2.setBorder(BorderFactory.createMatteBorder(0, 5, 1, 5, Color.WHITE));
            panel.add(GUIUtils.createHorizontalBoxForComponent(label5_2));

            JLabel label6 = GUIUtils.createLabel("You can, of course, try to play without any power-ups :).        ", 18, 15);
            label6.setBorder(BorderFactory.createMatteBorder(0, 5, 1, 5, Color.WHITE));
            panel.add(GUIUtils.createHorizontalBoxForComponent(label6));

            JLabel label7 = GUIUtils.createLabel("You can activate a power-up in the settings tab in the main menu.", 18, 15);
            label7.setBorder(BorderFactory.createMatteBorder(0, 5, 5, 5, Color.WHITE));
            panel.add(GUIUtils.createHorizontalBoxForComponent(label7));

            GUIUtils.addBackButtonAtBottomOfPanel(panel, future);
        });

        return future.join();
    }
}
