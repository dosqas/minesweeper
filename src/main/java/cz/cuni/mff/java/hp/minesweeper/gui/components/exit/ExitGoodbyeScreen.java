package cz.cuni.mff.java.hp.minesweeper.gui.components.exit;

import cz.cuni.mff.java.hp.minesweeper.utils.gui.AudioPlayer;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.GUIUtils;
import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;

import javax.swing.*;

/**
 * Screen that displays a goodbye message and exits the game.
 */
public class ExitGoodbyeScreen {
    /**
     * Runs the screen.
     */
    public static void run() {
        AudioPlayer.playSFX("/audio/sfx/goodbye.wav");
        GlobalVariablesGUI.LAYERED_PANE.removeAll();
        GlobalVariablesGUI.LAYERED_PANE.revalidate();
        GlobalVariablesGUI.LAYERED_PANE.repaint();

        JPanel panel = GUIUtils.createPanel(false);

        JLabel label = GUIUtils.createLabel("Goodbye!", 40);
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        panel.add(Box.createHorizontalGlue());
        panel.add(label);
        panel.add(Box.createHorizontalGlue());

        GUIUtils.addBackgroundToLayeredPane(GlobalVariablesGUI.LAYERED_PANE, 0);
        GlobalVariablesGUI.LAYERED_PANE.add(panel, JLayeredPane.PALETTE_LAYER);

        GlobalVariablesGUI.FRAME.revalidate();
        GlobalVariablesGUI.FRAME.repaint();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }
}
