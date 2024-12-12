import cz.cuni.mff.java.hp.minesweeper.utils.game.IntPair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link IntPair} class.
 */
@DisplayName("IntPair Tests")
public class TestIntPair {

    /**
     * Should correctly create an IntPair and retrieve its values.
     */
    @Test
    @DisplayName("Should correctly create an IntPair and retrieve its values")
    void shouldCreateIntPairCorrectly() {
        IntPair pair = new IntPair(1, 2);
        assertEquals(1, pair.first(), "First value should be 1");
        assertEquals(2, pair.second(), "Second value should be 2");
    }

    /**
     * Should correctly set the values of an IntPair.
     */
    @Test
    @DisplayName("toString() should return the correct representation")
    void shouldReturnCorrectStringRepresentation() {
        IntPair pair = new IntPair(3, 4);
        assertEquals("(3, 4)", pair.toString(), "toString should return '(3, 4)'");
    }

    /**
     * Should correctly set the values of an IntPair.
     */
    @Test
    @DisplayName("IntPairs with the same values should be equal")
    void shouldCheckEquality() {
        IntPair pair1 = new IntPair(5, 6);
        IntPair pair2 = new IntPair(5, 6);
        assertEquals(pair1, pair2, "IntPairs with the same values should be equal");
    }

    /**
     * Should correctly set the values of an IntPair.
     */
    @Test
    @DisplayName("IntPairs with the same values should have the same hashCode")
    void shouldHaveConsistentHashCode() {
        IntPair pair1 = new IntPair(7, 8);
        IntPair pair2 = new IntPair(7, 8);
        assertEquals(pair1.hashCode(), pair2.hashCode(), "IntPairs with the same values should have the same hashCode");
    }
}
