import cz.cuni.mff.java.hp.minesweeper.utils.game.Score;
import cz.cuni.mff.java.hp.minesweeper.utils.stats.StatsGame;
import cz.cuni.mff.java.hp.minesweeper.utils.stats.StatsTopScores;
import cz.cuni.mff.java.hp.minesweeper.utils.stats.StatsTxtReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the StatsTxtReader class, which handles loading and saving
 * game stats and top scores from text files.
 */
@DisplayName("StatsTxtReader Tests")
class TestStatsTxtReader {

    private File mockStatsFile;
    private File mockTopScoresFile;

    /**
     * Initializes the test environment before each test.
     */
    @BeforeEach
    @DisplayName("Set up the test environment")
    public void setUp() {
        mockStatsFile = new File("data/mockStatsFile.txt");
        mockTopScoresFile = new File("data/mockTopScoresFile.txt");
    }

    /**
     * Test the behavior of the initialize() method when the stats and top scores files don't exist.
     */
    @Test
    @DisplayName("Test initialize when stats file does not exist")
    public void testInitializeWhenStatsFileDoesNotExist() throws IOException {
        if (mockStatsFile.exists()) {
            boolean _ = mockStatsFile.delete();
        }
        if (mockTopScoresFile.exists()) {
            boolean _ = mockTopScoresFile.delete();
        }

        StatsTxtReader.initialize();

        assertFalse(mockStatsFile.exists(), "Stats file should not exist");
        assertFalse(mockTopScoresFile.exists(), "Top scores file should not exist");
    }

    /**
     * Test the save() method to ensure stats and top scores are saved correctly.
     */
    @Test
    @DisplayName("Test save method saves stats and top scores")
    public void testSave() throws IOException {
        if (!mockStatsFile.exists()) {
            boolean _ = mockStatsFile.createNewFile();
        }
        if (!mockTopScoresFile.exists()) {
            boolean _ = mockTopScoresFile.createNewFile();
        }

        StatsTxtReader.save();

        assertTrue(mockStatsFile.exists(), "Stats file should exist");
        assertTrue(mockTopScoresFile.exists(), "Top scores file should exist");
    }

    /**
     * Test the loadStats() method to check if stats are loaded correctly from the file.
     */
    @Test
    @DisplayName("Test loadStats loads stats from file correctly")
    public void testLoadStats() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(mockStatsFile))) {
            writer.write("wonMatches: 5");
            writer.newLine();
        }

        StatsTxtReader.loadStats(mockStatsFile.getPath());

        assertEquals(5, StatsGame.getWonMatches(), "Won matches should be 5");
    }

    /**
     * Test the parseScores() method to check if scores are parsed correctly from a string.
     */
    @Test
    @DisplayName("Test parseScores parses score string correctly")
    public void testParseScores() {
        String scoresString = "Player1,100,500;Player2,200,400";
        List<Score> scores = StatsTxtReader.parseScores(scoresString);

        assertEquals(2, scores.size(), "There should be 2 scores in the list");
        assertEquals("Player1", scores.getFirst().getName(), "First score should have name Player1");
        assertEquals(100L, scores.getFirst().getTime(), "First score should have time 100");
        assertEquals(500L, scores.getFirst().getScore(), "First score should have score 500");
    }

    /**
     * Test the behavior of the initialize() method when the stats file exists.
     */
    @Test
    @DisplayName("Test initialize when stats file exists and is valid")
    public void testInitializeWhenStatsFileExists() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(mockStatsFile))) {
            writer.write("wonMatches: 5");
            writer.newLine();
        }

        StatsTxtReader.initialize();
        StatsTxtReader.loadStats(mockStatsFile.getPath());

        assertEquals(5, StatsGame.getWonMatches(), "Won matches should be 5");
    }

    /**
     * Test the formatScores method to ensure it formats the score list correctly.
     */
    @Test
    @DisplayName("Test formatScores formats the score list correctly")
    public void testFormatScores() {
        List<Score> scores = List.of(
                new Score("Player1", 100L, 500L),
                new Score("Player2", 200L, 400L)
        );
        String formatted = StatsTxtReader.formatScores(scores);
        assertEquals("Player1,100,500;Player2,200,400", formatted, "The scores should be formatted correctly");
    }

    /**
     * Test the behavior of loadTopScores when the top scores file doesn't exist.
     */
    @Test
    @DisplayName("Test loadTopScores when top scores file does not exist")
    public void testLoadTopScoresWhenFileDoesNotExist() throws IOException {
        if (mockTopScoresFile.exists()) {
            boolean _ = mockTopScoresFile.delete();
        }

        StatsTxtReader.loadTopScores(mockTopScoresFile.getPath());

        assertEquals(0, StatsTopScores.getEasy().size(), "There should be no easy top scores");
        assertEquals(0, StatsTopScores.getMedium().size(), "There should be no medium top scores");
        assertEquals(0, StatsTopScores.getHard().size(), "There should be no hard top scores");
    }

    /**
     * Test the saveTopScores method to ensure it saves top scores to a file correctly.
     */
    @Test
    @DisplayName("Test saveTopScores saves top scores to file")
    public void testSaveTopScores() throws IOException {
        if (!mockTopScoresFile.exists()) {
            boolean _ = mockTopScoresFile.createNewFile();
        }

        StatsTxtReader.saveTopScores(mockTopScoresFile.getPath());

        assertTrue(mockTopScoresFile.exists(), "Top scores file should exist");
    }
}