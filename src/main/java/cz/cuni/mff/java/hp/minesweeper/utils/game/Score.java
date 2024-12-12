package cz.cuni.mff.java.hp.minesweeper.utils.game;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The `Score` class represents a single score entry in the Minesweeper game.
 * It contains the player's name, the time it took to finish the game, and the score.
 */
public class Score {
    @JsonProperty("name")
    private String name;
    @JsonProperty("time")
    private Long time;
    @JsonProperty("score")
    private Long score;

    /**
     * Creates a new score entry with the given name, time, and score.
     *
     * @param name  the player's name
     * @param time  the time it took to finish the game
     * @param score the score
     */
    public Score(String name, Long time, Long score) {
        this.name = name;
        this.time = time;
        this.score = score;
    }

    /**
     * Gets the player's name.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the time it took to finish the game.
     *
     * @return the time in milliseconds
     */
    public Long getTime() {
        return time;
    }

    /**
     * Gets the score.
     *
     * @return the score
     */
    public Long getScore() {
        return score;
    }
}