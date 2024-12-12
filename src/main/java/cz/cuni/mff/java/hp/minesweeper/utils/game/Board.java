package cz.cuni.mff.java.hp.minesweeper.utils.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * The `Board` class represents the game board for Minesweeper.
 * It manages the positions of mines, flags, question marks, revealed, and unrevealed tiles.
 */
public class Board {
    private final int width;
    private final int height;
    private final int mineCount;
    private final HashSet<IntPair> mines;
    private final HashSet<IntPair> questionMarks;
    private final HashSet<IntPair> flags;
    private final HashSet<IntPair> revealed;
    private final HashSet<IntPair> unrevealed;

    /**
     * Constructs a `Board` with the specified dimensions and number of mines.
     *
     * @param height the height of the board
     * @param width the width of the board
     * @param mineCount the number of mines on the board
     */
    public Board(int height, int width, int mineCount) {
        this.width = width;
        this.height = height;
        this.mineCount = mineCount;
        this.mines = initMines();
        this.questionMarks = new HashSet<>();
        this.flags = new HashSet<>();
        this.revealed = new HashSet<>();
        this.unrevealed = initUnrevealed();
    }

    /**
     * Initializes the positions of the mines on the board.
     *
     * @return a `HashSet` containing the positions of the mines
     */
    private HashSet<IntPair> initMines() {
        ArrayList<IntPair> allPositions = new ArrayList<>();
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                allPositions.add(new IntPair(x, y));
            }
        }

        Collections.shuffle(allPositions);

        return new HashSet<>(allPositions.subList(0, Math.min(mineCount, allPositions.size())));
    }

    /**
     * Initializes the positions of the unrevealed tiles on the board.
     *
     * @return a `HashSet` containing the positions of the unrevealed tiles
     */
    private HashSet<IntPair> initUnrevealed() {
        HashSet<IntPair> unrevealedSet = new HashSet<>();
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                unrevealedSet.add(new IntPair(x, y));
            }
        }
        return unrevealedSet;
    }

    /**
     * Returns the positions of the mines on the board.
     *
     * @return a `HashSet` containing the positions of the mines
     */
    public HashSet<IntPair> getMines() { return mines; }

    /**
     * Removes a mine from the specified position.
     *
     * @param x the x-coordinate of the mine
     * @param y the y-coordinate of the mine
     */
    public void removeMine(int x, int y) { mines.remove(new IntPair(x, y)); }

    /**
     * Adds a mine to the specified position.
     *
     * @param x the x-coordinate of the mine
     * @param y the y-coordinate of the mine
     */
    public void addMine(int x, int y) { mines.add(new IntPair(x, y)); }

    /**
     * Returns the positions of the question marks on the board.
     *
     * @return a `HashSet` containing the positions of the question marks
     */
    public HashSet<IntPair> getQuestionMarks() { return questionMarks; }

    /**
     * Returns the positions of the flags on the board.
     *
     * @return a `HashSet` containing the positions of the flags
     */
    public HashSet<IntPair> getFlags() {
        return flags;
    }

    /**
     * Returns the positions of the revealed tiles on the board.
     *
     * @return a `HashSet` containing the positions of the revealed tiles
     */
    public HashSet<IntPair> getRevealed() { return revealed; }

    /**
     * Returns the positions of the unrevealed tiles on the board.
     *
     * @return a `HashSet` containing the positions of the unrevealed tiles
     */
    public HashSet<IntPair> getUnrevealed() {
        return unrevealed;
    }

    /**
     * Adds a question mark to the specified position.
     *
     * @param x the x-coordinate of the question mark
     * @param y the y-coordinate of the question mark
     */
    public void addQuestionMark(int x, int y) {
        questionMarks.add(new IntPair(x, y));
    }

    /**
     * Removes a question mark from the specified position.
     *
     * @param x the x-coordinate of the question mark
     * @param y the y-coordinate of the question mark
     */
    public void removeQuestionMark(int x, int y) {
        questionMarks.remove(new IntPair(x, y));
    }

    /**
     * Adds a flag to the specified position.
     *
     * @param x the x-coordinate of the flag
     * @param y the y-coordinate of the flag
     */
    public void addFlag(int x, int y) {
        flags.add(new IntPair(x, y));
    }

    /**
     * Removes a flag from the specified position.
     *
     * @param x the x-coordinate of the flag
     * @param y the y-coordinate of the flag
     */
    public void removeFlag(int x, int y) {
        flags.remove(new IntPair(x, y));
    }

    /**
     * Adds a revealed tile to the specified position.
     *
     * @param x the x-coordinate of the revealed tile
     * @param y the y-coordinate of the revealed tile
     */
    public void addRevealed(int x, int y) {
        revealed.add(new IntPair(x, y));
    }

    /**
     * Removes an unrevealed tile from the specified position.
     *
     * @param x the x-coordinate of the unrevealed tile
     * @param y the y-coordinate of the unrevealed tile
     */
    public void removeUnrevealed(int x, int y) {
        unrevealed.remove(new IntPair(x, y));
    }
}