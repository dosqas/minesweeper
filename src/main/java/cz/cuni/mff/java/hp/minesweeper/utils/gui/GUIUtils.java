package cz.cuni.mff.java.hp.minesweeper.utils.gui;

import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * The `GUIUtils` class provides utility methods for creating GUI components.
 */
public class GUIUtils {
    /**
     * Creates a new frame with the specified title and layered pane.
     *
     * @param windowTitle the title of the window
     * @param layeredPane the layered pane to add to the frame
     * @return the created frame
     */
    public static JFrame createFrame(String windowTitle, JLayeredPane layeredPane) {
        JFrame frame = new JFrame(windowTitle);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(1440, 810);
        frame.add(layeredPane);
        frame.setVisible(true);

        return frame;
    }

    /**
     * Creates a new layered pane with the default preferred size.
     *
     * @return the created layered pane
     */
    public static JLayeredPane createLayeredPane() {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1440, 810));

        return layeredPane;
    }

    /**
     * Creates a new button with the specified text, width, and height.
     *
     * @param text   the text to display on the button
     * @param width  the width of the button
     * @param height the height of the button
     * @return the created button
     */
    public static JButton createButton(String text, int width, int height) {
        JButton button = new JButton(text);
        button.setFont(GlobalVariablesGUI.FONT);

        addHoverTextureToButton(button);

        button.addActionListener(_ -> {
            if (GlobalVariablesGUI.MUTE_SFX == 0) {
                AudioPlayer.playSFX("/audio/sfx/buttonClick.wav");
            }
        });

        return getjButton(width, height, button);
    }

    /**
     * A helper method to configure a button with the specified width and height.
     */
    @NotNull
    private static JButton getjButton(int width, int height, JButton button) {
        configButton(width, height, button);

        addTextureToButton(button);

        return button;
    }

    /**
     * Configures the specified button with the specified width and height.
     */
    private static void configButton(int width, int height, JButton button) {
        button.setPreferredSize(new Dimension(width, height));
        button.setMaximumSize(new Dimension(width, height));
        Border border = BorderFactory.createLineBorder(Color.WHITE, 3);
        button.setBorder(border);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
    }

    /**
     * Creates a new button with the specified text, width, height, and font size.
     *
     * @param text     the text to display on the button
     * @param width    the width of the button
     * @param height   the height of the button
     * @param fontSize the font size of the button text
     * @return the created button
     */
    public static JButton createButton(String text, int width, int height, float fontSize) {
        JButton button = new JButton(text);
        button.setFont(GlobalVariablesGUI.FONT.deriveFont(fontSize));
        getjButton(width, height, button);

        addHoverTextureToButton(button);

        button.addActionListener(_ -> {
            if (GlobalVariablesGUI.MUTE_SFX == 0) {
                AudioPlayer.playSFX("/audio/sfx/buttonClick.wav");
            }
        });

        return button;
    }

    /**
     * Creates a new button with the specified text, width, height, and font size, without a texture.
     *
     * @param text     the text to display on the button
     * @param width    the width of the button
     * @param height   the height of the button
     * @param fontSize the font size of the button text
     * @return the created button
     */
    public static JButton createButtonNoTexture(String text, int width, int height, float fontSize) {
        JButton button = new JButton(text);
        button.setFont(GlobalVariablesGUI.FONT.deriveFont(fontSize));
        configButton(width, height, button);

        button.setFocusable(false);
        button.setFocusPainted(false);

        button.setRolloverEnabled(false);
        button.setOpaque(true);
        button.setBackground(button.getBackground());

        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                super.paint(g, c);
            }
        });

        return button;
    }

    /**
     * Creates a new button to act as a tile on the game board.
     *
     * @param code the code of the tile to display
     * @return the created button
     */
    public static JButton createTileButton(int code) {
        JButton button = new JButton("");
        button.setPreferredSize(new Dimension(50, 50));
        button.setMaximumSize(new Dimension(50, 50));
        button.setMinimumSize(new Dimension(50, 50));

        addTileTextureToButton(button, code);

        return button;
    }

    /**
     * Creates a horizontal box with the specified component in the center.
     *
     * @param component the component to add to the box
     * @return the created box
     */
    public static Box createHorizontalBoxForComponent(JComponent component) {
        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalGlue());
        box.add(component);
        box.add(Box.createHorizontalGlue());
        return box;
    }

    /**
     * Creates a new label with the specified text and font size.
     *
     * @param text the text to display on the label
     * @param size the font size of the label text
     * @return the created label
     */
    public static JLabel createLabel(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(GlobalVariablesGUI.FONT.deriveFont((float) size));
        label.setForeground(Color.WHITE);
        Border border = BorderFactory.createLineBorder(Color.WHITE, 5);
        label.setBorder(border);

        addTextureToLabel(label);

        label.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
        label.setOpaque(true);

        return label;
    }

    /**
     * Creates a new label with the specified text, font size, and border size.
     *
     * @param text      the text to display on the label
     * @param size      the font size of the label text
     * @param borderSize the size of the label border
     * @return the created label
     */
    public static JLabel createLabel(String text, int size, int borderSize) {
        JLabel label = new JLabel(text);
        label.setBorder(BorderFactory.createEmptyBorder(borderSize, 0, borderSize, 0));
        label.setFont(GlobalVariablesGUI.FONT.deriveFont((float) size));
        label.setForeground(Color.WHITE);

        addTextureToLabel(label, borderSize);

        return label;
    }

    /**
     * Creates a new label to display a power-up with the specified code.
     *
     * @param code the code of the power-up to display
     * @return the created label
     */
    public static JLabel createPowerUpLabel(int code) {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(100, 50));
        label.setMaximumSize(new Dimension(100, 50));
        label.setMinimumSize(new Dimension(100, 50));
        label.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
        label.setOpaque(true);
        label.setBackground(new Color(200, 200, 200, 40));

        addPowerUpTextureToLabel(label, code);

        return label;
    }

    /**
     * Creates a new panel with the specified orientation.
     *
     * @param orientationIsVertical whether the panel should be vertical
     * @return the created panel
     */
    public static JPanel createPanel(boolean orientationIsVertical) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBounds(0, 0, 1440, 810);

        if (orientationIsVertical) panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        else panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        return panel;
    }

    /**
     * Creates a new slider with the specified minimum, maximum, value, width, height, and type.
     *
     * @param min    the minimum value of the slider
     * @param max    the maximum value of the slider
     * @param value  the initial value of the slider
     * @param width  the width of the slider
     * @param height the height of the slider
     * @param type   the type of the slider (0 for SFX, 1 for music)
     * @return the created slider
     */
    public static JPanel createSlider(int min, int max, int value, int width, int height, int type) {
        JPanel panel = GUIUtils.createPanel(true);
        panel.setPreferredSize(new Dimension(width + 20, height + 40));
        panel.setMaximumSize(new Dimension(width + 20, height + 40));

        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, value);
        slider.setPreferredSize(new Dimension(width - 10, height - 10));
        slider.setMaximumSize(new Dimension(width - 10, height - 10));
        slider.setFont(GlobalVariablesGUI.FONT);
        slider.setOpaque(false);
        slider.setPaintTicks(true);
        slider.setPaintTrack(true);
        slider.setMajorTickSpacing(25);

        slider.setUI(new javax.swing.plaf.basic.BasicSliderUI(slider) {
            @Override
            public void paintTrack(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

                int trackHeight = 6;
                int trackY = (trackRect.height - trackHeight) / 2 + trackRect.y;

                g2.setColor(Color.GRAY);
                g2.fillRect(trackRect.x, trackY, trackRect.width, trackHeight);

                int innerTrackHeight = 4;
                int innerTrackY = trackY + 1;
                g2.setColor(Color.WHITE);
                g2.fillRect(trackRect.x + 1, innerTrackY, trackRect.width - 2, innerTrackHeight);
            }

            @Override
            public void paintThumb(Graphics g) {
                if (thumbRect.isEmpty() || !slider.isEnabled()) {
                    return;
                }
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

                int thumbWidth = 10;
                int thumbHeight = 20;
                int thumbX = thumbRect.x + (thumbRect.width - thumbWidth) / 2;
                int thumbY = thumbRect.y + (thumbRect.height - thumbHeight) / 2;

                g2.setColor(slider.getBackground());
                g2.fillRect(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);

                g2.setColor(Color.WHITE);
                g2.fillRect(thumbX, thumbY, thumbWidth, thumbHeight);

                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(1));
            }
        });

        slider.setFocusable(false);

        String volumeLabelText = "Volume: " + value + "%";
        JLabel volumeLabel = GUIUtils.createLabel(volumeLabelText, 13);
        slider.addChangeListener(_ -> {
            int sliderValue = slider.getValue();

            Thread updatingThread = new Thread(() -> {
                if (type == 0) {
                    GlobalVariablesGUI.SFX_VOLUME = sliderValue;
                    AudioPlayer.setSFXVolume(sliderValue);
                } else {
                    GlobalVariablesGUI.MUSIC_VOLUME = sliderValue;
                    AudioPlayer.setMusicVolume(sliderValue);
                }
            });
            updatingThread.start();

            volumeLabel.setText("Volume: " + sliderValue + "%");
        });

        GUIUtils.addComponentsToPanel(panel, true, slider, (JComponent) Box.createVerticalStrut(5), volumeLabel);

        return panel;
    }

    /**
     * Creates a new text field with the specified width, height, and font size.
     *
     * @param width    the width of the text field
     * @param height   the height of the text field
     * @param fontSize the font size of the text field
     * @return the created text field
     */
    public static JTextField createTextField(int width, int height, float fontSize) {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(width, height));
        textField.setMaximumSize(new Dimension(width, height));
        textField.setFont(GlobalVariablesGUI.FONT.deriveFont(fontSize));
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
        textField.setForeground(Color.WHITE);
        textField.setOpaque(false);

        return textField;
    }

    /**
     * Adds a background image to the specified layered pane.
     *
     * @param layeredPane the layered pane to add the background to
     * @param menuType    the type of menu to display
     */
    public static void addBackgroundToLayeredPane(JLayeredPane layeredPane, int menuType) {
        JLabel background = new JLabel();

        String firstPartOfPath = "/textures/backgrounds/";
        String middlePartOfPath = GUIUtils.getMenuTypeFileName(menuType);
        String lastPartOfPath = GUIUtils.getThemeFileName();

        ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(AudioPlayer.class.getResource(firstPartOfPath + middlePartOfPath + lastPartOfPath)));
        Image scaledImage = originalIcon.getImage().getScaledInstance(
                layeredPane.getWidth(), layeredPane.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        background.setIcon(scaledIcon);
        background.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());

        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);
    }

    /**
     * Adds a texture to the specified button.
     */
    public static void addTextureToButton(JButton button) {
        String firstPartOfPath = "/textures/buttons/";
        String lastPartOfPath = GUIUtils.getThemeFileName();

        ImageIcon icon = new ImageIcon(Objects.requireNonNull(AudioPlayer.class.getResource(firstPartOfPath + lastPartOfPath)));
        Image image = icon.getImage();

        resizeImage(button, image);
    }

    /**
     * Resizes the specified image to fit the specified button.
     *
     * @param button the button to resize the image for
     * @param image the image to resize
     */
    public static void resizeImage(JButton button, Image image) {
        button.setIcon(new ImageIcon(image));
        button.revalidate();
        button.repaint();

        button.dispatchEvent(new java.awt.event.ComponentEvent(button, java.awt.event.ComponentEvent.COMPONENT_RESIZED));

        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setContentAreaFilled(false);
        button.setBorderPainted(true);
        button.setFocusPainted(false);
    }

    /**
     * Adds a texture to the specified button.
     */
    public static void addTileTextureToButton(JButton button, int code) {
        Image image = getImageForBoard(code);

        resizeImage(button, image);
    }

    /**
     * Returns the image for the specified board code.
     *
     * @param code the code of the board image to return
     * @return the image for the specified board code
     */
    public static Image getImageForBoard(int code) {
        String firstPartOfPath = "/textures/board/";

        switch (GlobalVariablesGUI.THEME) {
            case 0 -> firstPartOfPath += "classic/";
            case 1 -> firstPartOfPath += "dark/";
            case 2 -> firstPartOfPath += "desert/";
            case 3 -> firstPartOfPath += "winter/";
            case 4 -> firstPartOfPath += "pink/";
            case 5 -> firstPartOfPath += "cats/";
        }

        String fullPath = getString(code, firstPartOfPath);
        URL imageUrl = AudioPlayer.class.getResource(fullPath);

        if (imageUrl != null) {
            ImageIcon icon = new ImageIcon(imageUrl);
            return icon.getImage();
        } else {
            System.err.println("Image not found: " + fullPath);
            return null;
        }
    }

    /**
     * Returns the string for the specified code and first part of the path.
     *
     * @param code the code of the image to return
     * @param firstPartOfPath the first part of the path to use
     * @return the string for the specified code and first part of the path
     */
    private static @NotNull String getString(int code, String firstPartOfPath) {
        String lastPartOfPath = "";
        switch (code) {
            case 0 -> lastPartOfPath = "0.png";
            case 1 -> lastPartOfPath = "1.png";
            case 2 -> lastPartOfPath = "2.png";
            case 3 -> lastPartOfPath = "3.png";
            case 4 -> lastPartOfPath = "4.png";
            case 5 -> lastPartOfPath = "5.png";
            case 6 -> lastPartOfPath = "6.png";
            case 7 -> lastPartOfPath = "7.png";
            case 8 -> lastPartOfPath = "8.png";
            case 9 -> lastPartOfPath = "unrevealed.png";
            case 10 -> lastPartOfPath = "questionMark.png";
            case 11 -> lastPartOfPath = "flag.png";
            case 12 -> lastPartOfPath = "incorrectFlag.png";
            case 13 -> lastPartOfPath = "explodedMine.png";
            case 14 -> lastPartOfPath = "unexplodedMine.png";
            case 15 -> lastPartOfPath = "badQuestionMark.png";
            case 16 -> lastPartOfPath = "badFlag.png";
        }

        return firstPartOfPath + lastPartOfPath;
    }

    /**
     * Adds a texture to the specified label.
     *
     * @param label the label to add the texture to
     * @param code the code of the texture to add
     */
    public static void addPowerUpTextureToLabel(JLabel label, int code) {
        String firstPartOfPath = "/textures/powerUps/";

        Image image = getImageForPowerup(code, firstPartOfPath);

        if (image != null) {
            label.setIcon(new ImageIcon(image));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);

            label.setHorizontalTextPosition(SwingConstants.CENTER);
            label.setVerticalTextPosition(SwingConstants.CENTER);
        } else {
            System.err.println("Powerup image not found for code: " + code);
        }
    }

    /**
     * Returns the image for the specified power-up code.
     *
     * @param code the code of the power-up image to return
     * @param firstPartOfPath the first part of the path to use
     * @return the image for the specified power-up code
     */
    private static Image getImageForPowerup(int code, String firstPartOfPath) {
        String lastPartOfPath = "";
        switch (code) {
            case 100 -> lastPartOfPath = "nopowerup.png";
            case 101 -> lastPartOfPath = "3hearts.png";
            case 102 -> lastPartOfPath = "2hearts.png";
            case 103 -> lastPartOfPath = "1heart.png";
            case 104 -> lastPartOfPath = "0hearts.png";
            case 105 -> lastPartOfPath = "shield.png";
            case 106 -> lastPartOfPath = "cracked_shield.png";
            case 107 -> lastPartOfPath = "reveal.png";
            case 108 -> lastPartOfPath = "reveal_used.png";
            case 109 -> lastPartOfPath = "3safeSteps.png";
            case 110 -> lastPartOfPath = "2safeSteps.png";
            case 111 -> lastPartOfPath = "1safeStep.png";
            case 112 -> lastPartOfPath = "0safeSteps.png";
        }

        String fullPath = firstPartOfPath + lastPartOfPath;
        URL imageUrl = GUIUtils.class.getResource(fullPath);

        if (imageUrl != null) {
            ImageIcon icon = new ImageIcon(imageUrl);
            return icon.getImage();
        } else {
            System.err.println("Image not found: " + fullPath);
            return null;
        }
    }

    /**
     * Adds a texture to the specified label.
     *
     * @param label the label to add the texture to
     */
    public static void addTextureToLabel(JLabel label) {
        String firstPartOfPath = "/textures/labels/";
        String lastPartOfPath = GUIUtils.getThemeFileName();

        URL imageURL = GUIUtils.class.getResource(firstPartOfPath + lastPartOfPath);
        if (imageURL != null) {
            ImageIcon icon = new ImageIcon(imageURL);
            Image image = icon.getImage();

            GUIUtils.resizeImage(label, image);

            label.setHorizontalTextPosition(SwingConstants.CENTER);
            label.setVerticalTextPosition(SwingConstants.CENTER);
        } else {
            System.err.println("Resource not found: " + firstPartOfPath + lastPartOfPath);
        }
    }

    /**
     * Adds a texture to the specified label.
     *
     * @param label the label to add the texture to
     * @param borderSize the size of the label border
     */
    private static void addTextureToLabel(JLabel label, int borderSize) {
        String firstPartOfPath = "/textures/labels/";
        String lastPartOfPath = GUIUtils.getThemeFileName();

        URL imageURL = GUIUtils.class.getResource(firstPartOfPath + lastPartOfPath);
        if (imageURL != null) {
            ImageIcon icon = new ImageIcon(imageURL);
            Image image = icon.getImage();

            GUIUtils.resizeImage(label, image, borderSize);

            label.setHorizontalTextPosition(SwingConstants.CENTER);
            label.setVerticalTextPosition(SwingConstants.CENTER);
        } else {
            System.err.println("Resource not found: " + firstPartOfPath + lastPartOfPath);
        }
    }

    /**
     * Adds components to the specified panel according to the specified orientation.
     *
     * @param panel the panel to add the components to
     * @param orientationIsHorizontal whether the components should be added horizontally
     * @param components the components to add to the panel
     */
    public static void addComponentsToPanel(JPanel panel, boolean orientationIsHorizontal, JComponent... components) {
        if (orientationIsHorizontal) {
            Box horizontalBox = Box.createHorizontalBox();
            horizontalBox.add(Box.createHorizontalGlue());

            for (JComponent component : components) {
                horizontalBox.add(component);
                horizontalBox.add(Box.createHorizontalStrut(10));
            }

            horizontalBox.add(Box.createHorizontalGlue());

            Box verticalBox = Box.createVerticalBox();
            verticalBox.add(Box.createVerticalGlue());
            verticalBox.add(horizontalBox);
            verticalBox.add(Box.createVerticalGlue());

            panel.add(verticalBox);
        }
        else {
            panel.add(Box.createVerticalGlue());

            for (JComponent component : components) {
                panel.add(GUIUtils.createHorizontalBoxForComponent(component));
                panel.add(Box.createVerticalStrut(10));
            }
            panel.add(Box.createVerticalGlue());
        }
    }

    /**
     * Adds a label to the top of the specified panel.
     *
     * @param panel the panel to add the label to
     * @param label the label to add to the panel
     */
    public static void addLabelToTopOfPanel(JPanel panel, JLabel label) {
        panel.add(Box.createVerticalStrut(10));
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(50));
    }

    /**
     * Adds a back button to the bottom of the specified panel.
     *
     * @param panel the panel to add the back button to
     * @param future the future to complete when the back button is clicked
     */
    public static void addBackButtonAtBottomOfPanel(JPanel panel, CompletableFuture<Integer> future) {
        panel.add(Box.createVerticalGlue());
        JButton backButton = GUIUtils.createButton("Back", 500, 80);

        backButton.addActionListener(_ -> future.complete(0));

        panel.add(GUIUtils.createHorizontalBoxForComponent(backButton));

        panel.add(Box.createVerticalGlue());

        GUIUtils.addBackgroundToLayeredPane(GlobalVariablesGUI.LAYERED_PANE, 0);
        GlobalVariablesGUI.LAYERED_PANE.add(panel, JLayeredPane.PALETTE_LAYER);

        GlobalVariablesGUI.FRAME.revalidate();
        GlobalVariablesGUI.FRAME.repaint();
    }

    /**
     * Loads a custom font from the specified path with the specified size.
     *
     * @param path the path to the font file
     * @param size the size of the font
     * @return the loaded font
     */
    public static Font loadCustomFont(String path, float size) {
        try {
            InputStream fontStream = AudioPlayer.class.getResourceAsStream(path);

            if (fontStream == null) {
                System.err.println("Font not found at path: " + path);
                return new Font("Arial", Font.PLAIN, 40);
            }

            // Create the font from the input stream
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            return font.deriveFont(size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font("Arial", Font.PLAIN, 40);
        }
    }

    /**
     * Returns the file name for the specified menu type.
     *
     * @param menuType the type of menu to get the file name for
     * @return the file name for the specified menu type
     */
    private static String getMenuTypeFileName(int menuType) {
        String name = null;
        switch (menuType) {
            case 0 -> name = "main-menu/";
            case 1 -> name = "inner-menu/";
            case 2 -> name = "win/";
            case 3 -> name = "lose/";
        }
        return name;
    }

    /**
     * Returns the file name for the current theme.
     *
     * @return the file name for the current theme
     */
    public static String getThemeFileName() {
        String name = null;
        switch (GlobalVariablesGUI.THEME) {
            case 0 -> name = "classic.png";
            case 1 -> name = "dark.png";
            case 2 -> name = "desert.png";
            case 3 -> name = "winter.png";
            case 4 -> name = "pink.png";
            case 5 -> name = "cats.png";
        }
        return name;
    }

    /**
     * Resizes the specified image to fit the specified component.
     *
     * @param component the component to resize the image for
     * @param image the image to resize
     */
    private static void resizeImage(JComponent component, Image image) {
        final boolean[] resized = {false};
        component.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                if (!resized[0]) {
                    int width = component.getWidth();
                    int height = component.getHeight();
                    rescaleImage(width, height, image, component, resized);
                }
            }
        });
    }

    /**
     * Resizes the specified image to fit the specified component with the specified border size.
     *
     * @param width the width of the component
     * @param height the height of the component
     * @param image the image to resize
     * @param component the component to resize the image for
     * @param resized whether the image has been resized
     */
    private static void rescaleImage(int width, int height, Image image, JComponent component, boolean[] resized) {
        if (width > 0 && height > 0) {
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            if (component instanceof JButton) {
                ((JButton) component).setIcon(scaledIcon);
            } else if (component instanceof JLabel) {
                ((JLabel) component).setIcon(scaledIcon);
            }
            resized[0] = true;
        }
    }

    /**
     * Resizes the specified image to fit the specified component with the specified border size.
     *
     * @param component the component to resize the image for
     * @param image the image to resize
     * @param borderSize the size of the border
     */
    private static void resizeImage(JComponent component, Image image, int borderSize) {
        final boolean[] resized = {false};
        component.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                if (!resized[0]) {
                    int width = component.getWidth() + borderSize;
                    int height = component.getHeight() + borderSize;
                    rescaleImage(width, height, image, component, resized);
                }
            }
        });
    }

    /**
     * Changes the icon of the specified button to the specified new icon code.
     *
     * @param button the button to change the icon of
     * @param newIconCode the code of the new icon to display
     */
    public static void changeTileIcon(JButton button, int newIconCode) {
        Image icon = GUIUtils.getImageForBoard(newIconCode);
        assert icon != null;
        Image scaledImage = icon.getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImage));

        button.revalidate();
        button.repaint();
    }

    /**
     * Adds a hover texture to the specified button.
     *
     * @param button the button to add the hover texture to
     */
    private static void addHoverTextureToButton(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ImageIcon hoverIcon = null;
                switch (GlobalVariablesGUI.THEME) {
                    case 0 -> hoverIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/textures/buttonsHover/classic.png")));
                    case 1 -> hoverIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/textures/buttonsHover/dark.png")));
                    case 2 -> hoverIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/textures/buttonsHover/desert.png")));
                    case 3 -> hoverIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/textures/buttonsHover/winter.png")));
                    case 4 -> hoverIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/textures/buttonsHover/pink.png")));
                    case 5 -> hoverIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/textures/buttonsHover/cats.png")));                }

                button.setIcon(hoverIcon);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ImageIcon originalIcon = null;
                switch (GlobalVariablesGUI.THEME) {
                    case 0 -> originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/textures/buttons/classic.png")));
                    case 1 -> originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/textures/buttons/dark.png")));
                    case 2 -> originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/textures/buttons/desert.png")));
                    case 3 -> originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/textures/buttons/winter.png")));
                    case 4 -> originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/textures/buttons/pink.png")));
                    case 5 -> originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/textures/buttons/cats.png")));
                }
                button.setIcon(originalIcon);
            }
        });
    }
}
