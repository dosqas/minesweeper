import cz.cuni.mff.java.hp.minesweeper.utils.game.Board;
import cz.cuni.mff.java.hp.minesweeper.utils.game.IntPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Board} class.
 */
@DisplayName("Board Tests")
public class TestBoard {

    private Board board;

    /**
     * Sets up the board before each test.
     */
    @BeforeEach
    void setUp() {
        // Initialize a Board with 5x5 grid and 5 mines
        board = new Board(5, 5, 5);
    }

    /**
     * Test the behavior of the {@link Board} constructor.
     */
    @Test
    @DisplayName("Should initialize mines correctly")
    void shouldInitializeMinesCorrectly() {
        HashSet<IntPair> mines = board.getMines();
        assertEquals(5, mines.size(), "There should be exactly 5 mines");
        assertTrue(mines.stream().allMatch(pair -> pair.first() >= 0 && pair.first() < 5 && pair.second() >= 0 && pair.second() < 5),
                "Mines should be within the board boundaries");
    }

    /**
     * Test the behavior of the {@link Board} constructor.
     */
    @Test
    @DisplayName("Should initialize unrevealed tiles correctly")
    void shouldInitializeUnrevealedTilesCorrectly() {
        HashSet<IntPair> unrevealed = board.getUnrevealed();
        assertEquals(25, unrevealed.size(), "All 25 tiles should initially be unrevealed");
    }

    /**
     * Test the behavior of the {@link Board} constructor.
     */
    @Test
    @DisplayName("Should add and remove mines")
    void shouldAddAndRemoveMines() {
        board.addMine(0, 0);
        assertTrue(board.getMines().contains(new IntPair(0, 0)), "Mine should be added at (0, 0)");

        board.removeMine(0, 0);
        assertFalse(board.getMines().contains(new IntPair(0, 0)), "Mine should be removed from (0, 0)");
    }

    /**
     * Test the behavior of the {@link Board} constructor.
     */
    @Test
    @DisplayName("Should add and remove question marks")
    void shouldAddAndRemoveQuestionMarks() {
        board.addQuestionMark(1, 1);
        assertTrue(board.getQuestionMarks().contains(new IntPair(1, 1)), "Question mark should be added at (1, 1)");

        board.removeQuestionMark(1, 1);
        assertFalse(board.getQuestionMarks().contains(new IntPair(1, 1)), "Question mark should be removed from (1, 1)");
    }

    /**
     * Test the behavior of the {@link Board} constructor.
     */
    @Test
    @DisplayName("Should add and remove flags")
    void shouldAddAndRemoveFlags() {
        board.addFlag(2, 2);
        assertTrue(board.getFlags().contains(new IntPair(2, 2)), "Flag should be added at (2, 2)");

        board.removeFlag(2, 2);
        assertFalse(board.getFlags().contains(new IntPair(2, 2)), "Flag should be removed from (2, 2)");
    }

    /**
     * Test the behavior of the {@link Board} constructor.
     */
    @Test
    @DisplayName("Should add revealed tiles")
    void shouldAddRevealedTiles() {
        board.addRevealed(3, 3);
        assertTrue(board.getRevealed().contains(new IntPair(3, 3)), "Tile (3, 3) should be added to revealed tiles");
    }

    /**
     * Test the behavior of the {@link Board} constructor.
     */
    @Test
    @DisplayName("Should remove unrevealed tiles")
    void shouldRemoveUnrevealedTiles() {
        board.removeUnrevealed(4, 4);
        assertFalse(board.getUnrevealed().contains(new IntPair(4, 4)), "Tile (4, 4) should be removed from unrevealed tiles");
    }

    /**
     * Test the behavior of the {@link Board} constructor.
     */
    @Test
    @DisplayName("Should handle mine count smaller than board size")
    void shouldHandleSmallMineCount() {
        Board smallMineBoard = new Board(5, 5, 3);
        assertEquals(3, smallMineBoard.getMines().size(), "Board with fewer mines should initialize exactly 3 mines");
    }

    /**
     * Test the behavior of the {@link Board} constructor.
     */
    @Test
    @DisplayName("Should handle mine count larger than board size")
    void shouldHandleExcessiveMineCount() {
        Board excessiveMineBoard = new Board(2, 2, 10);
        assertEquals(4, excessiveMineBoard.getMines().size(), "Mine count should be capped at the total number of tiles (4)");
    }

    /**
     * Test the behavior of the {@link Board} constructor.
     */
    @Test
    @DisplayName("Mines, flags, and question marks should be distinct sets")
    void shouldEnsureDistinctSets() {
        board.addMine(0, 0);
        board.addFlag(0, 0);
        board.addQuestionMark(0, 0);

        assertTrue(board.getMines().contains(new IntPair(0, 0)), "Mine set should contain (0, 0)");
        assertTrue(board.getFlags().contains(new IntPair(0, 0)), "Flag set should contain (0, 0)");
        assertTrue(board.getQuestionMarks().contains(new IntPair(0, 0)), "Question mark set should contain (0, 0)");
    }
}
