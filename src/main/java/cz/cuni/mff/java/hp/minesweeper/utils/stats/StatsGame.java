package cz.cuni.mff.java.hp.minesweeper.utils.stats;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The `StatsGame` class manages the game statistics such as won matches, played matches,
 * discovered mines, and discovered non-mine tiles.
 */
public class StatsGame {
    @JsonProperty("wonMatches")
    private static long wonMatches;
    @JsonProperty("playedMatches")
    private static long playedMatches;
    @JsonProperty("discoveredMines")
    private static long discoveredMines;
    @JsonProperty("discoveredNonMineTiles")
    private static long discoveredNonMineTiles;

    /**
     * Gets the number of won matches.
     *
     * @return the number of won matches
     */
    public static long getWonMatches() {
        return wonMatches;
    }

    /**
     * Gets the number of played matches.
     *
     * @return the number of played matches
     */
    public static long getPlayedMatches() {
        return playedMatches;
    }

    /**
     * Gets the win rate as a percentage.
     *
     * @return the win rate percentage
     */
    public static long getWinRate() {
        return (int) ((double) wonMatches / playedMatches * 100);
    }

    /**
     * Gets the number of discovered mines.
     *
     * @return the number of discovered mines
     */
    public static long getDiscoveredMines() {
        return discoveredMines;
    }

    /**
     * Gets the number of discovered non-mine tiles.
     *
     * @return the number of discovered non-mine tiles
     */
    public static long getDiscoveredNonMineTiles() {
        return discoveredNonMineTiles;
    }

    /**
     * Sets the number of won matches.
     *
     * @param wonMatches the number of won matches
     */
    public static void setWonMatches(long wonMatches) {
        StatsGame.wonMatches = wonMatches;
    }

    /**
     * Sets the number of played matches.
     *
     * @param playedMatches the number of played matches
     */
    public static void setPlayedMatches(long playedMatches) {
        StatsGame.playedMatches = playedMatches;
    }

    /**
     * Sets the number of discovered mines.
     *
     * @param discoveredMines the number of discovered mines
     */
    public static void setDiscoveredMines(long discoveredMines) {
        StatsGame.discoveredMines = discoveredMines;
    }

    /**
     * Sets the number of discovered non-mine tiles.
     *
     * @param discoveredNonMineTiles the number of discovered non-mine tiles
     */
    public static void setDiscoveredNonMineTiles(long discoveredNonMineTiles) {
        StatsGame.discoveredNonMineTiles = discoveredNonMineTiles;
    }
}