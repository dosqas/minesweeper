import cz.cuni.mff.java.hp.minesweeper.service.Service;
import cz.cuni.mff.java.hp.minesweeper.utils.game.Board;
import cz.cuni.mff.java.hp.minesweeper.utils.game.IntPair;
import cz.cuni.mff.java.hp.minesweeper.utils.game.Score;
import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalGameDetails;
import cz.cuni.mff.java.hp.minesweeper.utils.stats.StatsTopScores;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Service class methods.
 */
@DisplayName("Service Class Tests")
class TestService {

    /**
     * Test the creation of the board with given height, width, and mine count.
     */
    @Test
    @DisplayName("Test createBoard method")
    public void testCreateBoard() {
        Service service = new Service();
        service.createBoard(10, 10, 20);

        Board board = Service.repository.getBoard();
        assertNotNull(board, "Board should not be null");
        assertEquals(20, board.getMines().size(), "Board should have 20 mines");
    }

    /**
     * Test getting the status of a tile (unrevealed, flagged, question-marked, revealed).
     */
    @Test
    @DisplayName("Test getTileStatus method")
    public void testGetTileStatus() {
        Service service = new Service();
        service.createBoard(5, 5, 5);

        Board board = Service.repository.getBoard();
        board.addFlag(0, 0);  // Add flag to (0,0)
        board.addRevealed(1, 1); // Reveal tile at (1,1)

        // Check the tile status
        assertEquals(2, service.getTileStatus(0, 0), "Tile at (0,0) should be flagged");
        assertEquals(3, service.getTileStatus(1, 1), "Tile at (1,1) should be revealed");
        assertEquals(0, service.getTileStatus(2, 2), "Tile at (2,2) should be unflagged and unrevealed");
    }

    /**
     * Test flagging a tile and toggling between flag, question mark, and no flag.
     */
    @Test
    @DisplayName("Test flagTile method")
    public void testFlagTile() {
        Service service = new Service();
        service.createBoard(5, 5, 5);

        JButton button = new JButton();
        service.flagTile(button, 0, 0); // Flag the tile
        Board board = Service.repository.getBoard();

        // Check if the tile is flagged
        assertTrue(board.getFlags().contains(new IntPair(0, 0)), "Tile at (0,0) should be flagged");

        service.flagTile(button, 0, 0); // Change flag to question mark
        assertTrue(board.getQuestionMarks().contains(new IntPair(0, 0)), "Tile at (0,0) should be question-marked");

        service.flagTile(button, 0, 0); // Change question mark to unflagged
        assertFalse(board.getFlags().contains(new IntPair(0, 0)), "Tile at (0,0) should not be flagged");
        assertFalse(board.getQuestionMarks().contains(new IntPair(0, 0)), "Tile at (0,0) should not be question-marked");
    }

    /**
     * Test the revealing of a tile, including when the tile is a mine and when it's not.
     */
    @Test
    @DisplayName("Test reveal method")
    public void testReveal() {
        Service service = new Service();
        service.createBoard(5, 5, 5);

        int safeTileX = 0;
        int safeTileY = 0;

        JButton[][] buttons = new JButton[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                buttons[i][j] = new JButton();

                if (!Service.repository.getBoard().getMines().contains(new IntPair(i, j))) {
                    safeTileX = i;
                    safeTileY = j;
                }
            }
        }

        // Reveal a safe tile
        boolean result = Service.reveal(buttons, safeTileX, safeTileY);
        assertTrue(result, "Revealing a safe tile should return true");

        // Reveal a mine tile
        Service.repository.getBoard().addMine(0, 0);
        result = Service.reveal(buttons, 0, 0);
        assertFalse(result, "Revealing a mine should return false");
    }

    /**
     * Test the revealing of a random mine.
     */
    @Test
    @DisplayName("Test revealRandomMine method")
    public void testRevealRandomMine() {
        Service service = new Service();
        service.createBoard(5, 5, 5);

        JButton[][] buttons = new JButton[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                buttons[i][j] = new JButton();
            }
        }

        // Randomly reveal a mine
        service.revealRandomMine(buttons);
        assertFalse(Service.repository.getBoard().getRevealed().isEmpty(), "At least one tile should be revealed after revealing a random mine");
    }

    /**
     * Test the behavior when revealing unexploded mines at the end of the game.
     */
    @Test
    @DisplayName("Test revealUnexplodedMinesWhenGameEnds method")
    public void testRevealUnexplodedMinesWhenGameEnds() {
        Service service = new Service();
        service.createBoard(5, 5, 5);

        int mineX = 0;
        int mineY = 0;

        JButton[][] buttons = new JButton[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                buttons[i][j] = new JButton();

                if (Service.repository.getBoard().getMines().contains(new IntPair(i, j))) {
                    mineX = i;
                    mineY = j;
                }
            }
        }

        Service.revealUnexplodedMinesWhenGameEnds(buttons);
        // Verifying the behavior by checking tile textures
        assertEquals(14, Service.getTileTexture(mineX, mineY), "Mines should be revealed with correct tile texture at the end of the game");
    }

    /**
     * Test getting the count of revealed tiles that are not mines.
     */
    @Test
    @DisplayName("Test getRevealedTilesCount method")
    public void testGetRevealedTilesCount() {
        Service service = new Service();
        service.createBoard(5, 5, 5);

        Service.repository.getBoard().addRevealed(1, 1);
        int count = Service.getRevealedTilesCount();

        if (Service.repository.getBoard().getMines().contains(new IntPair(1, 1))) {
            assertEquals(0, count, "Revealed mine tiles count should be 0");
        } else {
            assertEquals(1, count, "Revealed non-mine tiles count should be 1");
        }
    }

    /**
     * Test if the first tile clicked is a mine.
     */
    @Test
    @DisplayName("Test firstTileAMine method")
    public void testFirstTileAMine() {
        Service service = new Service();
        service.createBoard(5, 5, 5);

        Service.repository.getBoard().addMine(0, 0);
        assertTrue(Service.firstTileAMine(0, 0), "Tile (0,0) should be a mine");
    }

    /**
     * Test moving a mine from a safe tile.
     */
    @Test
    @DisplayName("Test moveMineFromSafeFirstTile method")
    public void testMoveMineFromSafeFirstTile() {
        Service service = new Service();
        service.createBoard(5, 5, 1);

        int mineX = 0;
        int mineY = 0;

        JButton[][] buttons = new JButton[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                buttons[i][j] = new JButton();

                if (Service.repository.getBoard().getMines().contains(new IntPair(i, j))) {
                    mineX = i;
                    mineY = j;
                }
            }
        }

        GlobalGameDetails.HEIGHT = 5;
        GlobalGameDetails.WIDTH = 5;

        Service.moveMineFromSafeFirstTile(buttons, mineX, mineY);
        assertFalse(Service.repository.getBoard().getMines().contains(new IntPair(0, 0)), "Tile (0,0) should no longer be a mine");
    }

    /**
     * Test if the current score qualifies as a top score.
     */
    @Test
    @DisplayName("Test isATopScore method")
    public void testIsATopScore() {
        StatsTopScores.setTopScores(new ArrayList<>());

        assertTrue(Service.isATopScore(100), "Any score should be a top score if less than 5 scores are saved");

        StatsTopScores.getTopScores().add(new Score("Player1", 1000L, 50L));
        StatsTopScores.getTopScores().add(new Score("Player2", 500L, 40L));

        assertTrue(Service.isATopScore(60), "Score 60 should be a top score");
        assertTrue(Service.isATopScore(30), "Score 30 should be a top score");
    }

    /**
     * Test adding a top score to the leaderboard.
     */
    @Test
    @DisplayName("Test addTopScore method")
    public void testAddTopScore() {
        StatsTopScores.setTopScores(new ArrayList<>());

        Service.addTopScore("Player1", 100, 50);
        assertEquals(1, StatsTopScores.getTopScores().size(), "There should be 1 top score added");

        Service.addTopScore("Player2", 200, 60);
        assertEquals(2, StatsTopScores.getTopScores().size(), "There should be 2 top scores");
    }

    /**
     * Test getting the tile texture for a specific tile.
     */
    @Test
    @DisplayName("Test getTileTexture method")
    public void testGetTileTexture() {
        Service service = new Service();
        service.createBoard(5, 5, 5);

        assertEquals(0, Service.getTileTexture(0, 0), "Tile at (0,0) should have texture 0 initially");
    }
}