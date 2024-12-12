package cz.cuni.mff.java.hp.minesweeper.gui.components.play;

import cz.cuni.mff.java.hp.minesweeper.service.Service;
import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalGameDetails;
import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.AudioPlayer;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.CustomOverlay;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.GUIUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * PlayGameScreen is a class that represents the screen where the game is actively played.
 */
public class PlayGameScreen {
    private static int heart_count;
    private static int shield_uses;
    private static boolean reveal_NotUsed;
    private static int safeStep_count;
    private static boolean safeStep_active;
    private static boolean flagModeIsOn;
    private static Timer timer;
    private static int uncovered_tiles;

    private static int shield_row;
    private static int shield_col;
    private static boolean paused;
    private static boolean lost;
    private static boolean won;
    private static boolean firstTileRevealSafety;
    private final Service service;
    ScheduledExecutorService scheduler;
    CompletableFuture<Integer> future;
    AtomicReference<Boolean> isMine;
    long[] seconds;
    JLabel powerUpsTextureLabel;
    private JButton[][] cellButtons;

    /**
     * Constructor of the PlayGameScreen class.
     *
     * @param service Service class that is used to interact with the game.
     */
    public PlayGameScreen(Service service) {
        this.service = service;
    }

    /**
     * Method that creates a scroll pane for the game board.
     *
     * @param boardPanel JPanel that represents the game board.
     * @param boardWidth Width of the game board.
     * @param boardHeight Height of the game board.
     * @return JScrollPane that contains the game board.
     */
    public static @NotNull JScrollPane getjScrollPane(JPanel boardPanel, int boardWidth, int boardHeight) {
        JScrollPane scrollPane = new JScrollPane(boardPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
        scrollPane.setPreferredSize(new Dimension(
                Math.min(boardWidth * 52 + 30, 1375),
                Math.min(boardHeight * 52 + 30, 575)
        ));
        scrollPane.setMaximumSize(new Dimension(
                Math.min(boardWidth * 52 + 30, 1375),
                Math.min(boardHeight * 52 + 30, 575)
        ));
        scrollPane.setMinimumSize(new Dimension(
                Math.min(boardWidth * 52 + 30, 1375),
                Math.min(boardHeight * 52 + 30, 575)
        ));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

    /**
     * Method that creates a GridBagConstraints object.
     *
     * @return GridBagConstraints object.
     */
    public static GridBagConstraints makeGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(1, 1, 1, 1);
        return gbc;
    }

    /**
     * Method that creates a JPanel with GridBagLayout.
     *
     * @return JPanel with GridBagLayout.
     */
    public static JPanel makeGbcPanelLayout() {
        JPanel boardPanel = new JPanel();
        boardPanel.setOpaque(false);

        boardPanel.setLayout(new GridBagLayout());
        return boardPanel;
    }

    /**
     * Method that runs the game.
     *
     * @return 0 if the game was exited to the main menu, 1 if the game was won, 2 if the game was lost.
     */
    public int run() {
        if (GlobalGameDetails.TIME_LIMIT != -1) {
            AudioPlayer.stopMusic();
            AudioPlayer.playMusic("/audio/music/timed.wav");
        }
        lost = false;
        won = false;
        paused = false;
        flagModeIsOn = false;
        firstTileRevealSafety = true;
        uncovered_tiles = 0;

        heart_count = 0;
        shield_uses = 0;
        reveal_NotUsed = false;
        safeStep_count = 0;
        seconds = new long[]{0};

        switch (GlobalGameDetails.POWER_UP) {
            case 1 -> heart_count = 3;
            case 2 -> shield_uses = 2;
            case 3 -> reveal_NotUsed = true;
            case 4 -> safeStep_count = 3;
        }

        isMine = new AtomicReference<>(false);

        future = new CompletableFuture<>();

        SwingUtilities.invokeLater(() -> {
            GlobalVariablesGUI.LAYERED_PANE.removeAll();
            GlobalVariablesGUI.LAYERED_PANE.revalidate();
            GlobalVariablesGUI.LAYERED_PANE.repaint();

            JPanel panel = GUIUtils.createPanel(true);

            JButton menuButton = GUIUtils.createButton("Menu", 150, 35, 20);
            menuButton.addActionListener(_ -> {
                paused = true;
                timer.stop();
                CompletableFuture<Integer> menuResult = CustomOverlay.showOverlayAsMenu(GlobalVariablesGUI.LAYERED_PANE);
                menuResult.thenAccept(result -> {
                    if (result == 0) {
                        paused = false;
                        timer.start();
                    } else if (result == 1) {
                        future.complete(0);
                    }
                });
            });

            JLabel powerUpsLabel = GUIUtils.createLabel("PowerUp:", 30);
            powerUpsLabel.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 0, Color.WHITE));

            int code = 0;
            switch (GlobalGameDetails.POWER_UP) {
                case 0 -> code = 100;
                case 1 -> code = 101;
                case 2 -> code = 105;
                case 3 -> code = 107;
                case 4 -> code = 109;
            }
            powerUpsTextureLabel = GUIUtils.createPowerUpLabel(code);

            JButton flagButton = GUIUtils.createButtonNoTexture("Flag: OFF", 150, 35, 15);
            flagButton.setBackground(new Color(255, 100, 100));
            flagButton.addActionListener(_ -> {
                if (flagModeIsOn) {
                    flagModeIsOn = false;
                    flagButton.setText("Flag: OFF");
                    flagButton.setBackground(new Color(255, 100, 100));
                } else {
                    flagModeIsOn = true;
                    flagButton.setText("Flag: ON ");
                    flagButton.setBackground(new Color(100, 255, 100));
                }
            });

            JLabel timeLabel = GUIUtils.createLabel("Time: " + seconds[0] + " ".repeat(6 - String.valueOf(seconds[0]).length()), 15);

            if (GlobalGameDetails.TIME_LIMIT != -1) {
                timeLabel.setText("Lose in: " + (GlobalGameDetails.TIME_LIMIT - seconds[0]) + " ".repeat(3 - String.valueOf(GlobalGameDetails.TIME_LIMIT - seconds[0]).length()));
            }

            JPanel panel1 = new JPanel();
            panel1.setOpaque(false);
            panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));

            JPanel line1Panel = new JPanel();
            line1Panel.setOpaque(false);
            line1Panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
            line1Panel.add(menuButton);
            line1Panel.add(Box.createHorizontalStrut(20));

            panel1.add(Box.createVerticalStrut(10));
            panel1.add(line1Panel);

            JPanel line2Panel = new JPanel();
            line2Panel.setOpaque(false);
            line2Panel.setLayout(new BoxLayout(line2Panel, BoxLayout.X_AXIS));

            line2Panel.add(Box.createHorizontalGlue());

            line2Panel.add(Box.createHorizontalStrut(19));

            powerUpsLabel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
            powerUpsTextureLabel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
            line2Panel.add(powerUpsLabel);
            line2Panel.add(powerUpsTextureLabel);
            if (GlobalGameDetails.POWER_UP == 3 || GlobalGameDetails.POWER_UP == 4) {
                JButton powerUpButton = GUIUtils.createButton("Use", 75, 50, 15);
                powerUpButton.addActionListener(_ -> {
                    if (GlobalGameDetails.POWER_UP == 3) {
                        if (reveal_NotUsed) {
                            reveal_NotUsed = false;
                            service.revealRandomMine(cellButtons);
                            GUIUtils.addPowerUpTextureToLabel(powerUpsTextureLabel, 108);
                            CustomOverlay.usedPowerUpOverlay(GlobalVariablesGUI.LAYERED_PANE, 10, timer);
                        } else CustomOverlay.usedPowerUpOverlay(GlobalVariablesGUI.LAYERED_PANE, 11, timer);
                    } else if (GlobalGameDetails.POWER_UP == 4) {
                        if (safeStep_count > 0 && !safeStep_active) {
                            safeStep_count--;
                            safeStep_active = true;
                            GUIUtils.addPowerUpTextureToLabel(powerUpsTextureLabel, 112 - safeStep_count);
                            CustomOverlay.usedPowerUpOverlay(GlobalVariablesGUI.LAYERED_PANE, 22 - safeStep_count, timer);
                        } else if (safeStep_active) {
                            CustomOverlay.usedPowerUpOverlay(GlobalVariablesGUI.LAYERED_PANE, 28, timer);
                        } else if (safeStep_count == 0) {
                            CustomOverlay.usedPowerUpOverlay(GlobalVariablesGUI.LAYERED_PANE, 29, timer);
                        }
                    }
                });

                powerUpButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
                line2Panel.add(Box.createHorizontalStrut(20));
                line2Panel.add(powerUpButton);
                line2Panel.add(Box.createHorizontalGlue());
                line2Panel.add(Box.createHorizontalStrut(529));
            } else {
                line2Panel.add(Box.createHorizontalGlue());
                line2Panel.add(Box.createHorizontalStrut(673));
            }

            flagButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
            timeLabel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
            line2Panel.add(flagButton);
            line2Panel.add(Box.createHorizontalStrut(20));
            line2Panel.add(timeLabel);

            line2Panel.add(Box.createHorizontalGlue());
            if (GlobalGameDetails.TIME_LIMIT != -1) {
                if (GlobalGameDetails.POWER_UP != 3 && GlobalGameDetails.POWER_UP != 4)
                    line2Panel.add(Box.createHorizontalStrut(55));
                else line2Panel.add(Box.createHorizontalStrut(31));
            } else if (GlobalGameDetails.POWER_UP != 3 && GlobalGameDetails.POWER_UP != 4)
                line2Panel.add(Box.createHorizontalStrut(53));
            else line2Panel.add(Box.createHorizontalStrut(8));

            panel1.add(line2Panel);

            panel.add(panel1);

            panel.add(Box.createVerticalStrut(15));


            switch (GlobalGameDetails.DIFFICULTY) {
                case 0 -> {
                    GlobalGameDetails.HEIGHT = 9;
                    GlobalGameDetails.WIDTH = 9;
                    GlobalGameDetails.MINE_COUNT = 10;
                }
                case 1 -> {
                    GlobalGameDetails.HEIGHT = 16;
                    GlobalGameDetails.WIDTH = 16;
                    GlobalGameDetails.MINE_COUNT = 40;
                }
                case 2 -> {
                    GlobalGameDetails.HEIGHT = 16;
                    GlobalGameDetails.WIDTH = 30;
                    GlobalGameDetails.MINE_COUNT = 99;
                }
            }
            service.createBoard(GlobalGameDetails.HEIGHT, GlobalGameDetails.WIDTH, GlobalGameDetails.MINE_COUNT);
            Service.tileTextures = new int[GlobalGameDetails.HEIGHT][GlobalGameDetails.WIDTH];
            for (int i = 0; i < GlobalGameDetails.HEIGHT; i++) {
                for (int j = 0; j < GlobalGameDetails.WIDTH; j++) {
                    Service.tileTextures[i][j] = 9;
                }
            }


            int boardWidth = GlobalGameDetails.WIDTH;
            int boardHeight = GlobalGameDetails.HEIGHT;

            GridBagConstraints gbc = makeGbc();
            JPanel boardPanel = makeGbcPanelLayout();

            final AtomicLong lastClickTime = new AtomicLong(0);
            cellButtons = new JButton[boardHeight][boardWidth];
            for (int row = 0; row < boardHeight; row++) {
                for (int col = 0; col < boardWidth; col++) {
                    JButton cellButton = GUIUtils.createTileButton(50);
                    GUIUtils.addTileTextureToButton(cellButton, 9);
                    cellButtons[row][col] = cellButton;

                    int finalRow = row;
                    int finalCol = col;

                    cellButton.addActionListener(_ -> {
                        synchronized (lastClickTime) {
                            long currentTime = System.currentTimeMillis();

                            if (currentTime - lastClickTime.get() < 350) {
                                return;
                            }

                            lastClickTime.set(currentTime);
                        }

                        int status = service.getTileStatus(finalRow, finalCol);

                        if (flagModeIsOn && status != 3) {
                            AudioPlayer.playSFX("/audio/sfx/flag.wav");
                            service.flagTile(cellButtons[finalRow][finalCol], finalRow, finalCol);
                        } else if (!flagModeIsOn && status == 0) {
                            if (firstTileRevealSafety && Service.firstTileAMine(finalRow, finalCol)) {
                                firstTileRevealSafety = false;
                                AudioPlayer.playSFX("/audio/sfx/mineReveal.wav");
                                Service.moveMineFromSafeFirstTile(cellButtons, finalRow, finalCol);
                            } else {
                                firstTileRevealSafety = false;
                                boolean result = Service.reveal(cellButtons, finalRow, finalCol);
                                isMine.set(!result);

                                if (isMine.get()) {
                                    AudioPlayer.playSFX("/audio/sfx/tileReveal.wav");
                                    shield_col = finalCol;
                                    shield_row = finalRow;
                                } else {
                                    AudioPlayer.playSFX("/audio/sfx/mineReveal.wav");
                                    safeStep_active = false;
                                }
                            }

                            uncovered_tiles = Service.getRevealedTilesCount();
                        }
                    });

                    gbc.gridx = col;
                    gbc.gridy = row;
                    boardPanel.add(cellButton, gbc);
                }
            }

            JScrollPane scrollPane = getjScrollPane(boardPanel, boardWidth, boardHeight);

            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            scrollPane.getHorizontalScrollBar().setUnitIncrement(16);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.add(scrollPane, BorderLayout.CENTER);

            if (GlobalGameDetails.HEIGHT * 52 + 30 > 575) {
                GUIUtils.addComponentsToPanel(panel, false, (JComponent) Box.createVerticalStrut(35), scrollPane, (JComponent) Box.createVerticalStrut(15));
            } else {
                int diff = 575 - (GlobalGameDetails.HEIGHT * 52 + 30);
                GUIUtils.addComponentsToPanel(panel, false, (JComponent) Box.createVerticalStrut(diff / 2), scrollPane, (JComponent) Box.createVerticalStrut(diff / 2 + 40));
            }

            GUIUtils.addBackgroundToLayeredPane(GlobalVariablesGUI.LAYERED_PANE, 0);
            GlobalVariablesGUI.LAYERED_PANE.add(panel, JLayeredPane.PALETTE_LAYER);

            GlobalVariablesGUI.FRAME.revalidate();
            GlobalVariablesGUI.FRAME.repaint();

            ReentrantLock lock = new ReentrantLock();

            scheduler = Executors.newSingleThreadScheduledExecutor();

            Runnable monitorTask = () -> {
                lock.lock();
                try {
                    if (won || lost) {
                        return;
                    }

                    double revealedMineCount = (Service.getRevealedTilesCount() /
                            (double) ((GlobalGameDetails.WIDTH * GlobalGameDetails.HEIGHT - GlobalGameDetails.MINE_COUNT)));

                    handleMusicChange(revealedMineCount);

                    if (GlobalGameDetails.TIME_LIMIT != -1 && seconds[0] >= GlobalGameDetails.TIME_LIMIT) {
                        handleTimeLimitExceeded();
                        return;
                    }

                    if (isMine.get()) {
                        handleMineHit();
                    }

                    checkWinCondition();
                } finally {
                    lock.unlock();
                }
            };


            scheduler.scheduleAtFixedRate(monitorTask, 0, 100, TimeUnit.MILLISECONDS);

            timer = new Timer(1000, _ -> {
                seconds[0]++;
                if (GlobalGameDetails.TIME_LIMIT != -1) {
                    timeLabel.setText("Lose in: " + (GlobalGameDetails.TIME_LIMIT - seconds[0]) + " ".repeat(3 - String.valueOf(GlobalGameDetails.TIME_LIMIT - seconds[0]).length()));
                } else timeLabel.setText("Time: " + seconds[0] + " ".repeat(6 - String.valueOf(seconds[0]).length()));
            });

            timer.start();
        });

        return future.join();
    }

    /**
     * Method that handles the change of music based on the number of revealed mines.
     *
     * @param revealedMineCount Number of revealed mines.
     */
    private void handleMusicChange(double revealedMineCount) {
        if (!lost && !paused && GlobalGameDetails.TIME_LIMIT == -1) {
            if (revealedMineCount >= 0 && revealedMineCount < 0.66 && AudioPlayer.getCurrentTheme() != 1) {
                AudioPlayer.stopMusic();
                AudioPlayer.playMusic("/audio/music/game1.wav");
            } else if (revealedMineCount >= 0.66 && revealedMineCount < 0.88 && AudioPlayer.getCurrentTheme() != 2) {
                AudioPlayer.stopMusic();
                AudioPlayer.playMusic("/audio/music/game2.wav");
            } else if (revealedMineCount >= 0.88 && revealedMineCount < 1 && AudioPlayer.getCurrentTheme() != 3) {
                AudioPlayer.stopMusic();
                AudioPlayer.playMusic("/audio/music/game3.wav");
            }
        }
    }

    /**
     * Method that handles the situation when the time limit is exceeded.
     */
    private void handleTimeLimitExceeded() {
        if (won || lost) {
            return;
        }
        timer.stop();
        Service.revealUnexplodedMinesWhenGameEnds(cellButtons);
        CustomOverlay.loseOverlay(GlobalVariablesGUI.LAYERED_PANE, timer, 1);
        AudioPlayer.stopMusic();
        AudioPlayer.playSFX("/audio/sfx/lostMatch.wav");
        try {
            Thread.sleep(3500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        future.complete(2);

        stopMonitoring();
    }

    /**
     * Method that handles the situation when a mine is hit.
     */
    private void handleMineHit() {
        if (heart_count > 0) {
            heart_count--;
            updatePowerUpTexture();

            if (heart_count == 0) {
                endGameWithLoss();
            }
            isMine.set(false);
        } else if (shield_uses > 0) {
            shield_uses--;
            activateShield();
            if (shield_uses == 0) {
                endGameWithLoss();
            }
            isMine.set(false);
        } else if (safeStep_active) {
            safeStep_active = false;
            isMine.set(false);
        } else {
            endGameWithLoss();
            isMine.set(false);
        }
    }

    /**
     * Method that updates the power-up texture.
     */
    private void updatePowerUpTexture() {
        if (heart_count == 2) GUIUtils.addPowerUpTextureToLabel(powerUpsTextureLabel, 102);
        else if (heart_count == 1) GUIUtils.addPowerUpTextureToLabel(powerUpsTextureLabel, 103);
        else if (heart_count == 0) GUIUtils.addPowerUpTextureToLabel(powerUpsTextureLabel, 104);
        CustomOverlay.clickedOnMineOverlay(GlobalVariablesGUI.LAYERED_PANE, 2 - heart_count, timer);
    }

    /**
     * Method that activates the shield power-up.
     */
    private void activateShield() {
        if (shield_uses == 1) {
            GUIUtils.addPowerUpTextureToLabel(powerUpsTextureLabel, 106);
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (shield_row + i >= 0 && shield_row + i < GlobalGameDetails.WIDTH &&
                            shield_col + j >= 0 && shield_col + j < GlobalGameDetails.HEIGHT) {
                        Service.reveal(cellButtons, shield_row + i, shield_col + j);
                    }
                }
            }
            uncovered_tiles = Service.getRevealedTilesCount();
        }
        CustomOverlay.clickedOnMineOverlay(GlobalVariablesGUI.LAYERED_PANE, 4 - shield_uses, timer);
    }

    /**
     * Method that ends the game with a loss.
     */
    private void endGameWithLoss() {
        if (won || lost) {
            return;
        }
        AudioPlayer.stopMusic();
        timer.stop();
        Service.revealUnexplodedMinesWhenGameEnds(cellButtons);
        CustomOverlay.loseOverlay(GlobalVariablesGUI.LAYERED_PANE, timer, 0);
        AudioPlayer.playSFX("/audio/sfx/lostMatch.wav");
        try {
            Thread.sleep(3500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        lost = true;
        future.complete(2);

        stopMonitoring();

    }

    /**
     * Method that checks the win condition.
     */
    private void checkWinCondition() {
        if (won || lost) {
            return;
        }
        if (GlobalGameDetails.WIDTH * GlobalGameDetails.HEIGHT - GlobalGameDetails.MINE_COUNT <= uncovered_tiles) {
            won = true;
            timer.stop();
            Service.revealUnexplodedMinesWhenGameEnds(cellButtons);
            GlobalGameDetails.FINAL_TIME = (int) seconds[0];
            CustomOverlay.winOverlay(GlobalVariablesGUI.LAYERED_PANE, timer);
            AudioPlayer.stopMusic();
            AudioPlayer.playSFX("/audio/sfx/winMatch.wav");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
            future.complete(1);
            uncovered_tiles = 0;

            stopMonitoring();
        }
    }

    /**
     * Method that stops the monitoring of the game.
     */
    public void stopMonitoring() {
        scheduler.shutdownNow();
        int retryCount = 3;
        while (retryCount > 0) {
            try {
                if (scheduler.awaitTermination(1, TimeUnit.SECONDS)) break;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            retryCount--;
        }
    }
}
