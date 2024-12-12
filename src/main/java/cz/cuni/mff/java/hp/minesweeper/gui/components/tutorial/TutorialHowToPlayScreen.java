package cz.cuni.mff.java.hp.minesweeper.gui.components.tutorial;

import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.GUIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CompletableFuture;

/**
 * Class representing the screen with the tutorial on how to play the game.
 */
public class TutorialHowToPlayScreen {
    /**
     * Method that runs the screen with the tutorial on how to play the game.
     * @return int representing the next screen to be shown
     */
    public int run() {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        SwingUtilities.invokeLater(() -> {
            GlobalVariablesGUI.LAYERED_PANE.removeAll();
            GlobalVariablesGUI.LAYERED_PANE.revalidate();
            GlobalVariablesGUI.LAYERED_PANE.repaint();

            JPanel panel = GUIUtils.createPanel(true);

            JLabel title = GUIUtils.createLabel("How to play", 50);
            GUIUtils.addLabelToTopOfPanel(panel, title);

            JLabel label1 = GUIUtils.createLabel("The goal of the game is to uncover all the tiles that do not     ", 18, 15);
            label1.setBorder(BorderFactory.createMatteBorder(5, 5, 0, 5, Color.WHITE));
            panel.add(GUIUtils.createHorizontalBoxForComponent(label1));

            JLabel label1_1 = GUIUtils.createLabel("contain mines.                                                   ", 18, 15);
            label1_1.setBorder(BorderFactory.createMatteBorder(0, 5, 1, 5, Color.WHITE));
            panel.add(GUIUtils.createHorizontalBoxForComponent(label1_1));

            JLabel label2 = GUIUtils.createLabel("!Lose the game if you uncover a tile with a mine.                ", 18, 15);
            label2.setBorder(BorderFactory.createMatteBorder(0, 5, 1, 5, Color.WHITE));
            panel.add(GUIUtils.createHorizontalBoxForComponent(label2));

            JLabel label3 = GUIUtils.createLabel("!Numbers on tiles indicate how many mines are in adjacent tiles  ", 18, 15);
            label3.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 5, Color.WHITE));
            panel.add(GUIUtils.createHorizontalBoxForComponent(label3));

            JLabel label3_1 = GUIUtils.createLabel("(a blank tile means there are no mines in adjacent tiles).       ", 18, 15);
            label3_1.setBorder(BorderFactory.createMatteBorder(0, 5, 1, 5, Color.WHITE));
            panel.add(GUIUtils.createHorizontalBoxForComponent(label3_1));

            JLabel label4 = GUIUtils.createLabel("!Left-click to uncover a tile.                                   ", 18, 15);
            label4.setBorder(BorderFactory.createMatteBorder(0, 5, 1, 5, Color.WHITE));
            panel.add(GUIUtils.createHorizontalBoxForComponent(label4));

            JLabel label5 = GUIUtils.createLabel("!Flag suspected mines by selecting the flag option before        ", 18, 15);
            label5.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 5, Color.WHITE));
            panel.add(GUIUtils.createHorizontalBoxForComponent(label5));

            JLabel label5_1 = GUIUtils.createLabel("revealing them.                                                  ", 18, 15);
            label5_1.setBorder(BorderFactory.createMatteBorder(0, 5, 1, 5, Color.WHITE));
            panel.add(GUIUtils.createHorizontalBoxForComponent(label5_1));

            JLabel label6 = GUIUtils.createLabel("!Uncovering a numberless tile will automatically uncover all     ", 18, 15);
            label6.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 5, Color.WHITE));
            panel.add(GUIUtils.createHorizontalBoxForComponent(label6));

            JLabel label6_1 = GUIUtils.createLabel("neighboring tiles that do not contain mines.                     ", 18, 15);
            label6_1.setBorder(BorderFactory.createMatteBorder(0, 5, 1, 5, Color.WHITE));
            panel.add(GUIUtils.createHorizontalBoxForComponent(label6_1));

            JLabel label7 = GUIUtils.createLabel("!Tiles with numbers only uncover themselves.                     ", 18, 15);
            label7.setBorder(BorderFactory.createMatteBorder(0, 5, 1, 5, Color.WHITE));
            panel.add(GUIUtils.createHorizontalBoxForComponent(label7));

            JLabel label8 = GUIUtils.createLabel("***Good luck!***                                                 ", 18, 15);
            label8.setBorder(BorderFactory.createMatteBorder(0, 5, 5, 5, Color.WHITE));
            panel.add(GUIUtils.createHorizontalBoxForComponent(label8));

            GUIUtils.addBackButtonAtBottomOfPanel(panel, future);
        });

        return future.join();
    }
}
