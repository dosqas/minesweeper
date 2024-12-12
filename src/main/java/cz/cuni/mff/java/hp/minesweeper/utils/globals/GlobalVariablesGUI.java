package cz.cuni.mff.java.hp.minesweeper.utils.globals;

import cz.cuni.mff.java.hp.minesweeper.utils.gui.GUIUtils;

import javax.swing.*;
import java.awt.*;

/**
 * The `GlobalVariablesGUI` class is a utility class that stores global GUI variables.
 * It contains the theme, SFX and music settings, and the layered pane and frame of the GUI.
 */
public class GlobalVariablesGUI {
    public static int THEME = 0;
    public static int MUTE_SFX = 0;
    public static int SFX_VOLUME = 15;
    public static int MUTE_MUSIC = 0;
    public static int MUSIC_VOLUME = 15;
    public static final JLayeredPane LAYERED_PANE = GUIUtils.createLayeredPane();
    public static final JFrame FRAME = GUIUtils.createFrame("Minesweeper", LAYERED_PANE);
    public final static Font FONT = GUIUtils.loadCustomFont("/textures/fonts/press-start-2p-font/PressStart2P-vaV7.ttf", 40);
}

