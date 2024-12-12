import cz.cuni.mff.java.hp.minesweeper.utils.game.Score;
import cz.cuni.mff.java.hp.minesweeper.utils.stats.StatsTopScores;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for the {@link StatsTopScores} class.
 */
public class TestStatsTopScores {

    /**
     * Set up the test environment.
     */
    @BeforeEach
    public void setUp() {
        // Reset the static lists before each test
        StatsTopScores.setEasy(new ArrayList<>());
        StatsTopScores.setMedium(new ArrayList<>());
        StatsTopScores.setHard(new ArrayList<>());
    }

    /**
     * Test setting and getting top scores.
     */
    @Test
    @DisplayName("Test setting and getting top scores")
    public void testSetAndGetTopScores() {
        List<Score> easyScores = Arrays.asList(
                new Score("Player1", 100L, 500L),
                new Score("Player2", 200L, 400L)
        );

        StatsTopScores.setHard(easyScores);

        List<Score> retrievedScores = StatsTopScores.getHard();
        assertEquals(2, retrievedScores.size(), "The number of scores should be 2");
        assertEquals("Player1", retrievedScores.getFirst().getName(), "The first player should be Player1");
    }

    /**
     * Test setting and getting top scores for medium difficulty.
     */
    @Test
    @DisplayName("Test setting and getting top scores for medium difficulty")
    public void testSortingLogic() {
        List<Score> scores = Arrays.asList(
                new Score("Player1", 100L, 500L),
                new Score("Player2", 100L, 600L),
                new Score("Player3", 150L, 500L)
        );

        StatsTopScores.setMedium(scores);

        List<Score> sortedScores = StatsTopScores.getMedium();
        assertEquals("Player2", sortedScores.get(0).getName(), "Player2 should be first");
        assertEquals("Player1", sortedScores.get(1).getName(), "Player1 should be second");
        assertEquals("Player3", sortedScores.get(2).getName(), "Player3 should be third");
    }

    /**
     * Test getting top scores for a specific difficulty.
     */
    @Test
    @DisplayName("Test getting top scores for a specific difficulty")
    public void testGetTopScoresForDifficulty() {
        List<Score> easyScores = Arrays.asList(
                new Score("Player1", 100L, 500L),
                new Score("Player2", 200L, 400L)
        );

        StatsTopScores.setEasy(easyScores);

        // Simulating that the difficulty is set to easy (0)
        List<Score> topScores = StatsTopScores.getTopScores();
        assertEquals("Player1", topScores.getFirst().getName(), "Player1 should be first");
    }
}
