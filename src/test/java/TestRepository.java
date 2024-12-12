import cz.cuni.mff.java.hp.minesweeper.utils.game.Board;
import cz.cuni.mff.java.hp.minesweeper.repository.Repository;
import cz.cuni.mff.java.hp.minesweeper.utils.stats.StatsTxtReader;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Repository} class.
 */
@DisplayName("Repository Tests")
public class TestRepository {
    private Repository repository;

    /**
     * Set up the repository before each test.
     */
    @BeforeEach
    void setUp() {
        // Reset the repository before each test
        repository = new Repository();
    }

    /**
     * Test creating a new board with given dimensions and mines.
     */
    @Test
    @DisplayName("Should create a new board with given dimensions and mines")
    void shouldCreateBoard() {
        repository.createBoard(5, 5, 5);
        Board board = repository.getBoard();

        assertNotNull(board, "Board should not be null after creation");
        assertEquals(5, board.getMines().size(), "Board should have exactly 5 mines");
        assertEquals(25, board.getUnrevealed().size(), "Board should have all tiles initially unrevealed");
    }

    /**
     * Test that the repository returns null if the board is not created.
     */
    @Test
    @DisplayName("Should return null if board is not created")
    void shouldReturnNullForUninitializedBoard() {
        assertNull(repository.getBoard(), "Board should be null if not created");
    }

    /**
     * Test loading stats without throwing exceptions.
     */
    @Test
    @DisplayName("Should load stats without throwing exceptions")
    void shouldLoadStats() {
        // Simulate StatsTxtReader.initialize() without using Mockito
        boolean exceptionThrown = false;
        try {
            StatsTxtReader.initialize();
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown, "loadStats should not throw exceptions");
    }

    /**
     * Test saving stats without throwing exceptions.
     */
    @Test
    @DisplayName("Should save stats without throwing exceptions")
    void shouldSaveStats() {
        // Simulate StatsTxtReader.save() without using Mockito
        boolean exceptionThrown = false;
        try {
            StatsTxtReader.initialize();
            StatsTxtReader.save();
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown, "saveStats should not throw exceptions");
    }
}