package cz.cuni.mff.java.hp.minesweeper.utils.game;

/**
 * The `IntPair` class represents a pair of integers.
 * It is used to store the coordinates of tiles on the game board.
 */
public record IntPair(int first, int second) {

    /**
     * Returns a string representation of the `IntPair`.
     *
     * @return a string representation of the `IntPair`
     */
    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
