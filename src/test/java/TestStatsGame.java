import cz.cuni.mff.java.hp.minesweeper.utils.stats.StatsGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
  * Unit tests for the {@link StatsGame} class.
  */
public class TestStatsGame {

    /**
     * Set up the stats before each test.
     */
    @BeforeEach
    public void setUp() {
        // Reset the stats before each test
        StatsGame.setWonMatches(0);
        StatsGame.setPlayedMatches(0);
        StatsGame.setDiscoveredMines(0);
        StatsGame.setDiscoveredNonMineTiles(0);
    }

    /**
     * Test the default values of the stats.
     */
    @Test
    @DisplayName("Test the default values of the stats")
    public void testWinRate() {
        StatsGame.setPlayedMatches(100);
        StatsGame.setWonMatches(70);

        assertEquals(70, StatsGame.getWinRate(), "The win rate should be 70%");
    }

    /**
     * Test the default values of the stats.
     */
    @Test
    @DisplayName("Test the default values of the stats")
    public void testGettersAndSetters() {
        StatsGame.setDiscoveredMines(50);
        StatsGame.setDiscoveredNonMineTiles(200);
        StatsGame.setPlayedMatches(100);
        StatsGame.setWonMatches(70);

        assertEquals(50, StatsGame.getDiscoveredMines(), "The number of discovered mines should be 50");
        assertEquals(200, StatsGame.getDiscoveredNonMineTiles(), "The number of discovered non-mine tiles should be 200");
        assertEquals(100, StatsGame.getPlayedMatches(), "The number of played matches should be 100");
        assertEquals(70, StatsGame.getWonMatches(), "The number of won matches should be 70");
    }
}
