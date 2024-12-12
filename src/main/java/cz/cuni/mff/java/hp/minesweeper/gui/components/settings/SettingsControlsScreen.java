package cz.cuni.mff.java.hp.minesweeper.gui.components.settings;

import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.GUIUtils;

import javax.swing.*;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Class representing the controls screen in the settings menu.
 */
public class SettingsControlsScreen {
    /**
     * Method that creates the controls screen.
     * @return 1
     */
    public int run() {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        SwingUtilities.invokeLater(() -> {
            GlobalVariablesGUI.LAYERED_PANE.removeAll();
            GlobalVariablesGUI.LAYERED_PANE.revalidate();
            GlobalVariablesGUI.LAYERED_PANE.repaint();

            JPanel panel = GUIUtils.createPanel(true);

            JLabel title = GUIUtils.createLabel("Controls", 50);
            GUIUtils.addLabelToTopOfPanel(panel, title);

            JLabel instructions = GUIUtils.createLabel("Use your left-click to play the game.", 20);
            JLabel instructions2 = GUIUtils.createLabel("  This is the only needed input :).  ", 20);

            ImageIcon leftClick = new ImageIcon(Objects.requireNonNull(getClass().getResource("/textures/misc/controls.png")));
            JLabel leftClickLabel = new JLabel(leftClick);

            GUIUtils.addComponentsToPanel(panel, false, instructions, instructions2, leftClickLabel);



            GUIUtils.addBackButtonAtBottomOfPanel(panel, future);
        });

        return future.join();
    }
}
