import cz.cuni.mff.java.hp.minesweeper.utils.game.Score;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Score} class.
 */
@DisplayName("Score Tests")
public class TestScore {

    /**
     * Tests the creation of a new score.
     */
    @Test
    @DisplayName("Should create a new score with given name, time and score")
    public void testScoreCreation() {
        Score score = new Score("Player1", 120L, 500L);

        assertEquals("Player1", score.getName(), "Name should be set correctly");
        assertEquals(120L, score.getTime(), "Time should be set correctly");
        assertEquals(500L, score.getScore(), "Score should be set correctly");
    }
}
