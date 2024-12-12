package cz.cuni.mff.java.hp.minesweeper.repository;

import cz.cuni.mff.java.hp.minesweeper.utils.stats.StatsTxtReader;
import cz.cuni.mff.java.hp.minesweeper.utils.game.Board;

/**
 * The `Repository` class manages the game board and handles loading and saving game statistics.
 */
public class Repository {
    private Board board;

    /**
     * Creates a new game board with the specified dimensions and number of mines.
     *
     * @param height the height of the board
     * @param width the width of the board
     * @param mineCount the number of mines on the board
     */
    public void createBoard(int height, int width, int mineCount) {
        board = new Board(height, width, mineCount);
    }

    /**
     * Returns the current game board.
     *
     * @return the current game board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Loads the game statistics from a file.
     */
    public void loadStats() {
        try {
            StatsTxtReader.initialize();
        }
        catch (Exception e) {
            System.out.println("Error while loading stats");
        }
    }

    /**
     * Saves the game statistics to a file.
     */
    public static void saveStats() {
        try {
            StatsTxtReader.save();
        }
        catch (Exception e) {
            System.out.println("Error while saving stats");
            System.out.println(e.getMessage());
        }
    }
}