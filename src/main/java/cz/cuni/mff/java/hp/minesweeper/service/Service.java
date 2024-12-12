package cz.cuni.mff.java.hp.minesweeper.service;

import cz.cuni.mff.java.hp.minesweeper.repository.Repository;
import cz.cuni.mff.java.hp.minesweeper.utils.game.Board;
import cz.cuni.mff.java.hp.minesweeper.utils.game.IntPair;
import cz.cuni.mff.java.hp.minesweeper.utils.game.Score;
import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalGameDetails;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.GUIUtils;
import cz.cuni.mff.java.hp.minesweeper.utils.stats.StatsGame;
import cz.cuni.mff.java.hp.minesweeper.utils.stats.StatsTopScores;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * The `Service` class is responsible for the game logic.
 * It initializes the repository and provides methods for creating the board, revealing tiles, flagging tiles, etc.
 */
public class Service {
    public static int[][] tileTextures;

    public static Repository repository;

    /**
     * The constructor initializes the repository and the tile textures.
     */
    public Service() {
        repository = new Repository();
        tileTextures = new int[10][10]; // Initialize with default size
    }

    /**
     * Creates a new board with the specified height, width, and mine count.
     *
     * @param height    the height of the board
     * @param width     the width of the board
     * @param mineCount the number of mines on the board
     */
    public void createBoard(int height, int width, int mineCount) {
        repository.createBoard(height, width, mineCount);
        tileTextures = new int[height][width]; // Reinitialize with board size
    }

    /**
     * Returns the status of the tile at the specified coordinates.
     *
     * @param x the x-coordinate of the tile
     * @param y the y-coordinate of the tile
     * @return the status of the tile (0 = hidden, 1 = question mark, 2 = flag, 3 = revealed)
     */
    public int getTileStatus(int x, int y) {
        Board board = repository.getBoard();

        if (board.getRevealed().contains(new IntPair(x, y))) {
            return 3;
        } else if (board.getFlags().contains(new IntPair(x, y))) {
            return 2;
        } else if (board.getQuestionMarks().contains(new IntPair(x, y))) {
            return 1;
        }
        return 0;
    }

    /**
     * Cycles through flag statuses for the tile at the specified coordinates.
     *
     * @param button the button representing the tile
     * @param x      the x-coordinate of the tile
     * @param y      the y-coordinate of the tile
     */
    public void flagTile(JButton button, int x, int y) {
        Board board = repository.getBoard();
        if (board.getRevealed().contains(new IntPair(x, y))) {
            return;
        }

        if (board.getFlags().contains(new IntPair(x, y))) {
            board.removeFlag(x, y);
            board.addQuestionMark(x, y);
            if (button.getWidth() > 0 && button.getHeight() > 0) GUIUtils.changeTileIcon(button, 10);
            tileTextures[x][y] = 10;
        } else if (board.getQuestionMarks().contains(new IntPair(x, y))) {
            board.removeQuestionMark(x, y);
            if (button.getWidth() > 0 && button.getHeight() > 0) GUIUtils.changeTileIcon(button, 9);
            tileTextures[x][y] = 9;
        } else {
            board.addFlag(x, y);
            if (button.getWidth() > 0 && button.getHeight() > 0) GUIUtils.changeTileIcon(button, 11);
            tileTextures[x][y] = 11;
        }
    }

    /**
     * Reveals the tile at the specified coordinates, recursively revealing neighboring tiles if the tile is empty.
     *
     * @param cellButtons the buttons representing the tiles
     * @param x           the x-coordinate of the tile
     * @param y           the y-coordinate of the tile
     * @return true if the tile is revealed, false if the tile is a mine
     */
    public static boolean reveal(JButton[][] cellButtons, int x, int y) {
        Board board = repository.getBoard();

        if (board.getRevealed().contains(new IntPair(x, y))) {
            return true;
        }

        if (board.getMines().contains(new IntPair(x, y))) {
            board.addRevealed(x, y);
            board.removeUnrevealed(x, y);
            if (cellButtons[x][y].getWidth() > 0 && cellButtons[x][y].getHeight() > 0) {
                GUIUtils.changeTileIcon(cellButtons[x][y], 13);
                tileTextures[x][y] = 13;
            }
            StatsGame.setDiscoveredMines(StatsGame.getDiscoveredMines() + 1);
            return false;
        }

        if (board.getFlags().contains(new IntPair(x, y))) {
            return true;
        }

        if (board.getQuestionMarks().contains(new IntPair(x, y))) {
            return true;
        }

        board.addRevealed(x, y);
        board.removeUnrevealed(x, y);
        StatsGame.setDiscoveredNonMineTiles(StatsGame.getDiscoveredNonMineTiles() + 1);

        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0)) {
                    if (board.getMines().contains(new IntPair(x + i, y + j))) {
                        count++;
                    }
                }
            }
        }

        if (cellButtons[x][y].getWidth() > 0 && cellButtons[x][y].getHeight() > 0) {
            GUIUtils.changeTileIcon(cellButtons[x][y], count);
            tileTextures[x][y] = count;
        }

        if (count == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    IntPair neighbor = new IntPair(x + i, y + j);

                    // Process only unrevealed and unflagged tiles
                    if (board.getUnrevealed().contains(neighbor) && !board.getFlags().contains(neighbor)) {
                        reveal(cellButtons, x + i, y + j);
                    }
                }
            }
        }

        return true;
    }

    /**
     * Reveals a random mine on the board.
     *
     * @param cellButtons the buttons representing the tiles
     */
    public void revealRandomMine(JButton[][] cellButtons) {
        Board board = repository.getBoard();

        HashSet<IntPair> mines = board.getMines();

        int randomMine = (int) (Math.random() * mines.size());

        reveal(cellButtons, mines.toArray(new IntPair[0])[randomMine].first(), mines.toArray(new IntPair[0])[randomMine].second());
    }

    /**
     * Reveals all mines on the board when the game ends.
     *
     * @param cellButtons the buttons representing the tiles
     */
    public static void revealUnexplodedMinesWhenGameEnds(JButton[][] cellButtons) {
        Board board = repository.getBoard();

        HashSet<IntPair> mines = board.getMines();

        for (IntPair mine : mines) {
            if (!board.getFlags().contains(mine) && !board.getRevealed().contains(mine) && !board.getQuestionMarks().contains(mine)) {
                if (cellButtons[mine.first()][mine.second()].getWidth() > 0 && cellButtons[mine.first()][mine.second()].getHeight() > 0) {
                    GUIUtils.changeTileIcon(cellButtons[mine.first()][mine.second()], 14);
                }
                tileTextures[mine.first()][mine.second()] = 14;
            }
        }

        for (IntPair questionMark : board.getQuestionMarks()) {
            if (!mines.contains(questionMark)) {
                if (cellButtons[questionMark.first()][questionMark.second()].getWidth() > 0 && cellButtons[questionMark.first()][questionMark.second()].getHeight() > 0) {
                    GUIUtils.changeTileIcon(cellButtons[questionMark.first()][questionMark.second()], 15);
                }
                tileTextures[questionMark.first()][questionMark.second()] = 15;
            }
        }

        for (IntPair flag : board.getFlags()) {
            if (!mines.contains(flag)) {
                if (cellButtons[flag.first()][flag.second()].getWidth() > 0 && cellButtons[flag.first()][flag.second()].getHeight() > 0) {
                    GUIUtils.changeTileIcon(cellButtons[flag.first()][flag.second()], 16);
                }
                tileTextures[flag.first()][flag.second()] = 16;
            }
        }
    }

    /**
     * Returns the number of revealed tiles that are not mines.
     *
     * @return the number of revealed tiles that are not mines
     */
    public static int getRevealedTilesCount() {
        Board board = repository.getBoard();

        synchronized (board) {
            HashSet<IntPair> revealed = board.getRevealed();
            HashSet<IntPair> mines = board.getMines();

            int count = 0;
            HashSet<IntPair> revealedCopy = new HashSet<>(revealed);
            for (var tile : revealedCopy) {
                if (!mines.contains(tile)) {
                    count++;
                }
            }

            return count;
        }
    }

    /**
     * Returns whether the first tile is a mine or not.
     *
     * @param x the x-coordinate of the first tile
     * @param y the y-coordinate of the first tile
     * @return true if the first tile is a mine, false otherwise
     */
    public static boolean firstTileAMine(int x, int y) {
        Board board = repository.getBoard();

        return board.getMines().contains(new IntPair(x, y));
    }

    /**
     * Moves the mine from the first tile to a random safe tile if the first tile is a mine.
     *
     * @param cellButtons the buttons representing the tiles
     * @param x           the x-coordinate of the first tile
     * @param y           the y-coordinate of the first tile
     */
    public static void moveMineFromSafeFirstTile(JButton[][] cellButtons, int x, int y) {
        Board board = repository.getBoard();

        ArrayList<IntPair> allPositions = new ArrayList<>();
        for (int i = 0; i < GlobalGameDetails.HEIGHT; i++) {
            for (int j = 0; j < GlobalGameDetails.WIDTH; j++) {
                if (!board.getMines().contains(new IntPair(i, j))) {
                    allPositions.add(new IntPair(i, j));
                }
            }
        }

        if (!allPositions.isEmpty()) {
            IntPair newMine = allPositions.get((int) (Math.random() * allPositions.size()));

            board.removeMine(x, y);
            board.addMine(newMine.first(), newMine.second());
            Service.reveal(cellButtons, x, y);
        } else {
            throw new IndexOutOfBoundsException("No available positions to move the mine");
        }
    }

    /**
     * Returns whether a score is a top score.
     *
     * @param score the score to check
     * @return true if the score is a top score, false otherwise
     */
    public static boolean isATopScore(int score) {
        List<Score> topScores = StatsTopScores.getTopScores();

        if (topScores.size() < 5) {
            return true;
        }

        for (Score topScore : topScores) {
            if (score > topScore.getScore()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Adds a top score to the list of top scores.
     *
     * @param name  the name of the player
     * @param time  the time taken to complete the game
     * @param score the score achieved
     */
    public static void addTopScore(String name, long time, long score) {
        List<Score> topScores = StatsTopScores.getTopScores();

        topScores.add(new Score(name, time, score));
        topScores.sort((o1, o2) -> Long.compare(o2.getScore(), o1.getScore()));

        if (topScores.size() > 5) {
            topScores.remove(5);
        }

        StatsTopScores.setTopScores(topScores);
    }

    /**
     * Returns the tile texture code at the specified coordinates.
     *
     * @param x the x-coordinate of the tile
     * @param y the y-coordinate of the tile
     * @return the tile texture code
     */
    public static int getTileTexture(int x, int y) {
        if (tileTextures == null) {
            throw new NullPointerException("tileTextures is not initialized");
        }
        return tileTextures[x][y];
    }

    /**
     * Loads the game statistics from the repository.
     */
    public void loadStats() {
        repository.loadStats();
    }

    /**
     * Saves the game statistics to the repository.
     */
    public static void saveStats() {
        Repository.saveStats();
    }
}