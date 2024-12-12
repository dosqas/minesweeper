package cz.cuni.mff.java.hp.minesweeper.utils.stats;

import cz.cuni.mff.java.hp.minesweeper.utils.game.Score;
import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalGameDetails;

import java.util.List;
import java.util.Objects;

/**
 * The `StatsTopScores` class manages the top scores for different difficulty levels in the Minesweeper game.
 */
public class StatsTopScores {
    private static List<Score> easy;
    private static List<Score> medium;
    private static List<Score> hard;

    /**
     * Gets the list of top scores for the easy difficulty level.
     *
     * @return the list of top scores for the easy difficulty level
     */
    public static List<Score> getEasy() { return easy; }

    /**
     * Gets the list of top scores for the medium difficulty level.
     *
     * @return the list of top scores for the medium difficulty level
     */
    public static List<Score> getMedium() {
        return medium;
    }

    /**
     * Gets the list of top scores for the hard difficulty level.
     *
     * @return the list of top scores for the hard difficulty level
     */
    public static List<Score> getHard() {
        return hard;
    }

    /**
     * Sets the list of top scores for the easy difficulty level.
     *
     * @param easy the list of top scores for the easy difficulty level
     */
    public static void setEasy(List<Score> easy) {
        sortList(easy);
        StatsTopScores.easy = easy;
    }

    /**
     * Sets the list of top scores for the medium difficulty level.
     *
     * @param medium the list of top scores for the medium difficulty level
     */
    public static void setMedium(List<Score> medium) {
        sortList(medium);
        StatsTopScores.medium = medium;
    }

    /**
     * Sets the list of top scores for the hard difficulty level.
     *
     * @param hard the list of top scores for the hard difficulty level
     */
    public static void setHard(List<Score> hard) {
        sortList(hard);
        StatsTopScores.hard = hard;
    }

    /**
     * Gets the list of top scores based on the current game difficulty.
     *
     * @return the list of top scores for the current game difficulty
     */
    public static List<Score> getTopScores() {
        switch (GlobalGameDetails.DIFFICULTY) {
            case 0 -> { return StatsTopScores.getEasy(); }
            case 1 -> { return StatsTopScores.getMedium(); }
            case 2 -> { return StatsTopScores.getHard(); }
            default -> throw new IllegalStateException("Unexpected difficulty: " + GlobalGameDetails.DIFFICULTY);}
    }

    /**
     * Sets the list of top scores based on the current game difficulty.
     *
     * @param topScores the list of top scores for the current game difficulty
     */
    public static void setTopScores(List<Score> topScores) {
        sortList(topScores);
        switch (GlobalGameDetails.DIFFICULTY) {
            case 0 -> StatsTopScores.setEasy(topScores);
            case 1 -> StatsTopScores.setMedium(topScores);
            case 2 -> StatsTopScores.setHard(topScores);
            default -> throw new IllegalStateException("Unexpected difficulty: " + GlobalGameDetails.DIFFICULTY);}
    }

    /**
     * Sorts the list of scores in descending order of score, then by ascending order of time, and finally by name.
     *
     * @param scores the list of scores to sort
     */
    private static void sortList(List<Score> scores) {
        scores.sort((o1, o2) -> {
            if (Objects.equals(o1.getScore(), o2.getScore())) {
                if (Objects.equals(o1.getTime(), o2.getTime())) {
                    return o1.getName().compareTo(o2.getName());
                }
                return o1.getTime() < o2.getTime() ? -1 : 1;
            }
            return o1.getScore() < o2.getScore() ? 1 : -1;
        });
    }
}