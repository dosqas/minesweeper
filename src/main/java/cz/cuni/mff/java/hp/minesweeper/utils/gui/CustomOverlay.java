package cz.cuni.mff.java.hp.minesweeper.utils.gui;

import cz.cuni.mff.java.hp.minesweeper.gui.components.play.PlayGameScreen;
import cz.cuni.mff.java.hp.minesweeper.service.Service;
import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalGameDetails;
import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.concurrent.*;

import static cz.cuni.mff.java.hp.minesweeper.gui.components.play.PlayGameScreen.makeGbc;
import static cz.cuni.mff.java.hp.minesweeper.gui.components.play.PlayGameScreen.makeGbcPanelLayout;

/**
 * The `CustomOverlay` class provides methods for creating custom overlays in the Minesweeper game.
 */
public class CustomOverlay {
    private static final BlockingQueue<Runnable> overlayQueue = new LinkedBlockingQueue<>();
    private static volatile boolean isOverlayActive = false;

    /**
     * Processes the overlay queue.
     */
    private static void processQueue() {
        if (isOverlayActive) return;

        try (ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor()) {
            executorService.scheduleAtFixedRate(() -> {
                try {
                    Runnable task = overlayQueue.take();
                    isOverlayActive = true;
                    task.run();
                    isOverlayActive = false;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, 0, 500, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /// Static block to initialize the overlay queue processing.
    static {
        processQueue();
    }


    /**
     * Shows an overlay with a message on the given layered pane.
     *
     * @param layeredPane the layered pane to show the overlay on
     * @param message     the message to display
     * @param delay       the delay in milliseconds before the overlay disappears
     * @param fontSize    the font size of the message
     */
    public static void showOverlayWithMessage(JLayeredPane layeredPane, String message, int delay, float fontSize) {
        JLabel messageLabel = getjLabel(layeredPane, message, fontSize);

        layeredPane.add(messageLabel, JLayeredPane.DRAG_LAYER);
        layeredPane.revalidate();
        layeredPane.repaint();

        Timer timer = new Timer(delay, _ -> {
            layeredPane.remove(messageLabel);
            layeredPane.revalidate();
            layeredPane.repaint();
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Creates a JLabel with a message to display on the overlay.
     *
     * @param layeredPane the layered pane to show the overlay on
     * @param message     the message to display
     * @param fontSize    the font size of the message
     */
    private static @NotNull JLabel getjLabel(JLayeredPane layeredPane, String message, float fontSize) {
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(GlobalVariablesGUI.FONT.deriveFont(fontSize));
        messageLabel.setOpaque(true);
        messageLabel.setBackground(new Color(255, 100, 100, 250));
        int width = layeredPane.getWidth() / 2 + 400;
        return getjLabel(layeredPane, messageLabel, width);
    }

    /**
     * Creates a JLabel with a message to display on the overlay.
     *
     * @param layeredPane the layered pane to show the overlay on
     * @param messageLabel the message label to display
     * @param width the width of the message label
     */
    private static @NotNull JLabel getjLabel(JLayeredPane layeredPane, JLabel messageLabel, int width) {
        int height = layeredPane.getHeight() / 2;
        int x = (layeredPane.getWidth() - width) / 2;
        int y = (layeredPane.getHeight() - height) / 2;
        messageLabel.setBounds(x, y, width, height);
        return messageLabel;
    }

    /**
     * Shows an overlay when the player clicks on a mine.
     *
     * @param layeredPane the layered pane to show the overlay on
     * @param powerUpCase the power-up case to display
     * @param mainTimer   the main timer to stop incrementing while the overlay is displayed
     */
    public static void clickedOnMineOverlay(JLayeredPane layeredPane, int powerUpCase, Timer mainTimer) {
        JPanel glassPane = getGlassPane(0, 0, layeredPane);

        layeredPane.add(glassPane, JLayeredPane.POPUP_LAYER);

        JLabel messageLabel = getLabel(layeredPane, powerUpCase);

        removeOverlay(layeredPane, mainTimer, glassPane, messageLabel);
    }

    /**
     * Removes the overlay from the layered pane.
     *
     * @param layeredPane  the layered pane to remove the overlay from
     * @param mainTimer    the main timer to start after the overlay is removed
     * @param glassPane    the glass pane to remove
     * @param messageLabel the message label to remove
     */
    private static void removeOverlay(JLayeredPane layeredPane, Timer mainTimer, JPanel glassPane, JLabel messageLabel) {
        layeredPane.add(messageLabel, JLayeredPane.DRAG_LAYER);
        layeredPane.revalidate();
        layeredPane.repaint();

        mainTimer.stop();

        Timer timer = new Timer(1500, _ -> {
            layeredPane.remove(messageLabel);
            layeredPane.remove(glassPane);
            layeredPane.revalidate();
            layeredPane.repaint();

            mainTimer.start();
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Gets the label for the overlay when the player clicks on a mine, but has a passive power-up protecting them.
     *
     * @param layeredPane  the layered pane to show the overlay on
     * @param powerUpCase  the power-up case to display
     */
    private static @NotNull JLabel getLabel(JLayeredPane layeredPane, int powerUpCase) {
        String message = "You clicked on a mine!";
        switch (powerUpCase) {
            case 0 -> message += " You have 1 extra heart left!";
            case 1 -> message += " You have no extra hearts left! Careful...";
            case 2 -> message += " That was your last life...";
            case 3 -> message += " Your protective shield's broken! Careful...";
            case 4 -> message += " No shield protecting you anymore...";
        }

        return getMessageLabel(layeredPane, message);
    }

    /**
     * Shows an overlay when the player consumes a power-up.
     *
     * @param layeredPane the layered pane to show the overlay on
     * @param powerUpCase the power-up case to display
     * @param mainTimer   the main timer to stop incrementing while the overlay is displayed
     */
    public static void usedPowerUpOverlay(JLayeredPane layeredPane, int powerUpCase, Timer mainTimer) {
        JPanel glassPane = getGlassPane(0, 0, layeredPane);

        layeredPane.add(glassPane, JLayeredPane.POPUP_LAYER);

        JLabel messageLabel = getjLabel(layeredPane, powerUpCase);

        removeOverlay(layeredPane, mainTimer, glassPane, messageLabel);
    }

    /**
     * Gets the label for the overlay when the player uses a power-up. (Reveal or Safe-step)
     *
     * @param layeredPane the layered pane to show the overlay on
     * @param powerUpCase the power-up case to display
     */
    private static @NotNull JLabel getjLabel(JLayeredPane layeredPane, int powerUpCase) {
        String message = "";
        if (powerUpCase / 10 == 1) {
            if (powerUpCase == 10) {
                message = "Reveal used! A random mine has been revealed!";
            }
            else {
                message = "No reveals left!";
            }
        }
        else {
            switch (powerUpCase) {
                case 20 -> message = "Safe-step used! 2 remaining!";
                case 21 -> message = "Safe-step used! 1 remaining!";
                case 22 -> message = "Safe-step used! No remaining safe-steps!";
                case 28 -> message = "Safe-step is active! Next reveal is safe!";
                case 29 -> message = "No safe-steps left!";
                }
        }

        return getMessageLabel(layeredPane, message);
    }

    /**
     * Creates a JLabel with a message to display on the overlay.
     *
     * @param layeredPane the layered pane to show the overlay on
     * @param message     the message to display
     */
    private static @NotNull JLabel getMessageLabel(JLayeredPane layeredPane, String message) {
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(GlobalVariablesGUI.FONT.deriveFont(20f));
        messageLabel.setOpaque(true);
        messageLabel.setBackground(new Color(255, 100, 100, 50));
        int width = layeredPane.getWidth() / 2 + 700;
        return getjLabel(layeredPane, messageLabel, width);
    }

    /**
     * Shows an overlay when the player loses the game.
     *
     * @param layeredPane the layered pane to show the overlay on
     * @param mainTimer   the main timer to stop incrementing while the overlay is displayed
     * @param loseType    the type of loss (0 - clicked on mine, 1 - time's up)
     */
    public static void loseOverlay(JLayeredPane layeredPane, Timer mainTimer, int loseType) {
        JPanel glassPane = getGlassPane(0, 0, layeredPane);

        layeredPane.add(glassPane, JLayeredPane.POPUP_LAYER);

        JLabel messageLabel = getMessageLabel(layeredPane, loseType);

        setTimerToOverlays(layeredPane, mainTimer, glassPane, messageLabel);
    }

    /**
     * Sets the timer for the overlays to disappear.
     *
     * @param layeredPane  the layered pane to show the overlay on
     * @param mainTimer    the main timer to stop incrementing while the overlay is displayed
     * @param glassPane    the glass pane to remove
     * @param messageLabel the message label to remove
     */
    private static void setTimerToOverlays(JLayeredPane layeredPane, Timer mainTimer, JPanel glassPane, JLabel messageLabel) {
        layeredPane.add(messageLabel, JLayeredPane.DRAG_LAYER);
        layeredPane.revalidate();
        layeredPane.repaint();

        mainTimer.stop();

        Timer timer = new Timer(3000, _ -> {
            layeredPane.remove(messageLabel);
            layeredPane.revalidate();
            layeredPane.repaint();
        });

        Timer timer2 = new Timer(3500, _ -> {
            layeredPane.remove(glassPane);
            mainTimer.start();
        });

        timer.setRepeats(false);
        timer.start();
        timer2.start();
    }

    /**
     * Creates a JPanel with a glass pane effect, to block user input while the main overlay is displayed.
     *
     * @param r           the red color value
     * @param r1          the alpha value
     * @param layeredPane the layered pane to show the overlay on
     */
    private static @NotNull JPanel getGlassPane(int r, int r1, JLayeredPane layeredPane) {
        JPanel glassPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(r, r, r, r1));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        glassPane.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
        glassPane.setOpaque(false);
        glassPane.addMouseListener(new MouseAdapter() {
        });
        return glassPane;
    }

    /**
     * Gets the label for the overlay when the player loses the game.
     *
     * @param layeredPane the layered pane to show the overlay on
     * @param loseType    the type of loss (0 - clicked on mine, 1 - time's up)
     */
    private static @NotNull JLabel getMessageLabel(JLayeredPane layeredPane, int loseType) {
        JLabel messageLabel;
        if (loseType == 0) messageLabel = new JLabel("Uh oh... That's a mine!", SwingConstants.CENTER);
        else messageLabel = new JLabel("Time's up! Challenge lost...", SwingConstants.CENTER);

        messageLabel.setFont(GlobalVariablesGUI.FONT.deriveFont(20f));
        messageLabel.setOpaque(true);
        messageLabel.setBackground(new Color(255, 100, 100, 50));
        int width = layeredPane.getWidth() / 2 + 700;
        return getjLabel(layeredPane, messageLabel, width);
    }

    /**
     * Shows an overlay when the player wins the game.
     *
     * @param layeredPane the layered pane to show the overlay on
     * @param mainTimer   the main timer to stop incrementing while the overlay is displayed
     */
    public static void winOverlay(JLayeredPane layeredPane, Timer mainTimer) {
        JPanel glassPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 0));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        glassPane.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
        glassPane.setOpaque(false);

        layeredPane.add(glassPane, JLayeredPane.POPUP_LAYER);

        JLabel messageLabel = getjLabel(layeredPane);

        setTimerToOverlays(layeredPane, mainTimer, glassPane, messageLabel);
    }

    /**
     * Gets the label for the overlay when the player wins the game.
     *
     * @param layeredPane the layered pane to show the overlay on
     */
    private static @NotNull JLabel getjLabel(JLayeredPane layeredPane) {
        JLabel messageLabel = new JLabel("Great job! All correct tiles uncovered!", SwingConstants.CENTER);
        messageLabel.setFont(GlobalVariablesGUI.FONT.deriveFont(20f));
        messageLabel.setOpaque(true);
        messageLabel.setBackground(new Color(100, 255, 100, 50));
        int width = layeredPane.getWidth() / 2 + 700;
        return getjLabel(layeredPane, messageLabel, width);
    }

    /**
     * Shows an overlay when the player pauses the game.
     *
     * @param layeredPane the layered pane to show the overlay on
     * @return a completable future with the result of the overlay (0 - continue, 1 - exit)
     */
    public static CompletableFuture<Integer> showOverlayAsMenu(JLayeredPane layeredPane) {
        if (AudioPlayer.getCurrentTheme() != 6) {
            AudioPlayer.stopMusic();
            AudioPlayer.playMusic("/audio/music/paused.wav");
        }
        CompletableFuture<Integer> result = new CompletableFuture<>();

        JPanel glassPane = getjPanel(layeredPane);
        layeredPane.add(glassPane, JLayeredPane.POPUP_LAYER);

        JPanel panel = GUIUtils.createPanel(true);
        int panelWidth = 800;
        int panelHeight = 600;
        panel.setBounds(
                (layeredPane.getWidth() - panelWidth) / 2,
                (layeredPane.getHeight() - panelHeight) / 2,
                panelWidth,
                panelHeight
        );

        JLabel pauseLabel = GUIUtils.createLabel("Game Paused", 25);

        JButton returnButton = GUIUtils.createButton("Continue", 500, 80, 50);
        returnButton.addActionListener(_ -> {
            result.complete(0);
            layeredPane.remove(panel);
            layeredPane.remove(glassPane);
            layeredPane.revalidate();
            layeredPane.repaint();
        });

        JButton exitButton = GUIUtils.createButton("Main menu", 500, 80, 50);
        exitButton.addActionListener(_ -> {
            result.complete(1);
            layeredPane.remove(panel);
            layeredPane.remove(glassPane);
            layeredPane.revalidate();
            layeredPane.repaint();
        });

        GUIUtils.addComponentsToPanel(panel, false, pauseLabel, returnButton, exitButton);

        glassPane.add(panel);

        layeredPane.revalidate();
        layeredPane.repaint();

        return result;
    }

    /**
     * Shows the board as an overlay when the match ends and the user wishes to check the final state of the board.
     *
     * @param layeredPane the layered pane to show the overlay on
     */
    public static void showBoardOverlayWhenMatchEnds(JLayeredPane layeredPane) {
        JPanel glassPane = getjPanel(layeredPane);
        layeredPane.add(glassPane, JLayeredPane.POPUP_LAYER);

        JPanel panel = GUIUtils.createPanel(true);
        int panelWidth = 1440;
        int panelHeight = 810;
        panel.setBounds(
                (layeredPane.getWidth() - panelWidth) / 2,
                (layeredPane.getHeight() - panelHeight) / 2,
                panelWidth,
                panelHeight
        );

        JButton returnButton = GUIUtils.createButton("Return", 450, 80, 50);
        returnButton.addActionListener(_ -> {
            layeredPane.remove(panel);
            layeredPane.remove(glassPane);
            layeredPane.revalidate();
            layeredPane.repaint();
        });

        int boardWidth = GlobalGameDetails.WIDTH;
        int boardHeight = GlobalGameDetails.HEIGHT;

        GridBagConstraints gbc = makeGbc();
        JPanel boardPanel = makeGbcPanelLayout();

        SwingUtilities.invokeLater(() -> {
            for (int row = 0; row < boardHeight; row++) {
                for (int col = 0; col < boardWidth; col++) {
                    JButton cellButton = GUIUtils.createTileButton(50);
                    GUIUtils.addTileTextureToButton(cellButton, 9);

                    gbc.gridx = col;
                    gbc.gridy = row;
                    boardPanel.add(cellButton, gbc);

                    int finalRow = row;
                    int finalCol = col;
                    SwingUtilities.invokeLater(() -> GUIUtils.changeTileIcon(cellButton, Service.getTileTexture(finalRow, finalCol)));
                }
            }
            boardPanel.revalidate();
            boardPanel.repaint();
        });


        JScrollPane scrollPane = PlayGameScreen.getjScrollPane(boardPanel, boardWidth, boardHeight);

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        if (GlobalGameDetails.HEIGHT * 52 + 30 > 575) {
            GUIUtils.addComponentsToPanel(panel, false, (JComponent) Box.createVerticalStrut(15), scrollPane, (JComponent) Box.createVerticalStrut(35), returnButton);
        } else {
            int diff = 575 - (GlobalGameDetails.HEIGHT * 52 + 30);
            GUIUtils.addComponentsToPanel(panel, false, (JComponent) Box.createVerticalStrut(diff / 2), scrollPane, (JComponent) Box.createVerticalStrut(diff), returnButton);
        }

        glassPane.add(panel);

        layeredPane.revalidate();
        layeredPane.repaint();
    }

    /**
     * Sets up the glass pane for the overlay.
     *
     * @param layeredPane the layered pane to show the overlay on
     */
    private static @NotNull JPanel getjPanel(JLayeredPane layeredPane) {
        JPanel glassPane = getGlassPane(211, 150, layeredPane);
        glassPane.setFocusable(true);
        glassPane.setLayout(null);
        glassPane.setOpaque(false);
        return glassPane;
    }
}