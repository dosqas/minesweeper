package cz.cuni.mff.java.hp.minesweeper.utils.stats;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.java.hp.minesweeper.utils.game.Score;

/**
 * The `StatsTxtReader` class handles reading and writing game statistics and top scores to and from text files.
 */
public class StatsTxtReader {
    private static final String DATA_DIRECTORY = "data";
    private static final String STATS_FILE_NAME = "stats.txt";
    private static final String TOPSCORES_FILE_NAME = "topscores.txt";

    /**
     * Initializes the statistics and top scores by loading them from their respective files.
     *
     * @throws IOException if an I/O error occurs
     */
    public static void initialize() throws IOException {
        String statsPath = DATA_DIRECTORY + File.separator + STATS_FILE_NAME;
        String topScoresPath = DATA_DIRECTORY + File.separator + TOPSCORES_FILE_NAME;

        loadStats(statsPath);
        loadTopScores(topScoresPath);
    }

    /**
     * Saves the current statistics and top scores to their respective files.
     *
     * @throws IOException if an I/O error occurs
     */
    public static void save() throws IOException {
        String statsPath = DATA_DIRECTORY + File.separator + STATS_FILE_NAME;
        String topScoresPath = DATA_DIRECTORY + File.separator + TOPSCORES_FILE_NAME;

        saveStats(statsPath);
        saveTopScores(topScoresPath);
    }

    /**
     * Loads the game statistics from the specified file.
     *
     * @param path the path to the statistics file
     * @throws IOException if an I/O error occurs
     */
    public static void loadStats(String path) throws IOException {
        File statsFile = new File(path);

        if (!statsFile.getParentFile().exists()) {
            if (!statsFile.getParentFile().mkdirs()) {
                throw new IOException("Failed to create directory: " + statsFile.getParentFile());
            }
        }

        if (!statsFile.exists() || statsFile.length() == 0) {
            StatsGame.setWonMatches(0);
            StatsGame.setPlayedMatches(0);
            StatsGame.setDiscoveredMines(0);
            StatsGame.setDiscoveredNonMineTiles(0);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(statsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length < 2) continue;
                String key = parts[0].trim();
                long value = Long.parseLong(parts[1].trim());

                switch (key) {
                    case "wonMatches" -> StatsGame.setWonMatches(value);
                    case "playedMatches" -> StatsGame.setPlayedMatches(value);
                    case "discoveredMines" -> StatsGame.setDiscoveredMines(value);
                    case "discoveredNonMineTiles" -> StatsGame.setDiscoveredNonMineTiles(value);
                }
            }
        }
    }

    /**
     * Saves the current game statistics to the specified file.
     *
     * @param path the path to the statistics file
     * @throws IOException if an I/O error occurs
     */
    private static void saveStats(String path) throws IOException {
        File statsFile = createDir(path);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(statsFile))) {
            writer.write("wonMatches:" + StatsGame.getWonMatches());
            writer.newLine();
            writer.write("playedMatches:" + StatsGame.getPlayedMatches());
            writer.newLine();
            writer.write("discoveredMines:" + StatsGame.getDiscoveredMines());
            writer.newLine();
            writer.write("discoveredNonMineTiles:" + StatsGame.getDiscoveredNonMineTiles());
            writer.newLine();
        }
    }

    /**
     * Loads the top scores from the specified file.
     *
     * @param path the path to the top scores file
     * @throws IOException if an I/O error occurs
     */
    public static void loadTopScores(String path) throws IOException {
        File scoresFile = new File(path);
        if (!scoresFile.exists() || scoresFile.length() == 0) {
            StatsTopScores.setEasy(new ArrayList<>());
            StatsTopScores.setMedium(new ArrayList<>());
            StatsTopScores.setHard(new ArrayList<>());
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(scoresFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length < 2) continue;
                String key = parts[0].trim();
                String value = parts[1].trim();

                switch (key) {
                    case "easy" -> StatsTopScores.setEasy(parseScores(value));
                    case "medium" -> StatsTopScores.setMedium(parseScores(value));
                    case "hard" -> StatsTopScores.setHard(parseScores(value));
                }
            }
        }
    }

    /**
     * Saves the current top scores to the specified file.
     *
     * @param path the path to the top scores file
     * @throws IOException if an I/O error occurs
     */
    public static void saveTopScores(String path) throws IOException {
        File scoresFile = createDir(path);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(scoresFile))) {
            writer.write("easy:" + formatScores(StatsTopScores.getEasy()));
            writer.newLine();
            writer.write("medium:" + formatScores(StatsTopScores.getMedium()));
            writer.newLine();
            writer.write("hard:" + formatScores(StatsTopScores.getHard()));
            writer.newLine();
        }
    }

    /**
     * Parses a string of scores into a list of `Score` objects.
     *
     * @param value the string of scores
     * @return the list of `Score` objects
     */
    public static List<Score> parseScores(String value) {
        List<Score> scores = new ArrayList<>();
        String[] scoreEntries = value.split(";");
        for (String entry : scoreEntries) {
            String[] fields = entry.split(",");
            if (fields.length < 3) continue;
            String name = fields[0];
            long time = Long.parseLong(fields[1]);
            long score = Long.parseLong(fields[2]);
            scores.add(new Score(name, time, score));
        }
        return scores;
    }

    /**
     * Formats a list of `Score` objects into a string.
     *
     * @param scores the list of `Score` objects
     * @return the formatted string of scores
     */
    public static String formatScores(List<Score> scores) {
        StringBuilder sb = new StringBuilder();
        for (Score score : scores) {
            if (!sb.isEmpty()) sb.append(";");
            sb.append(score.getName()).append(",").append(score.getTime()).append(",").append(score.getScore());
        }
        return sb.toString();
    }

    /**
     * Creates a directory and file if they do not exist.
     *
     * @param path the path to the file
     * @return the created file
     * @throws IOException if an I/O error occurs
     */
    private static File createDir(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                throw new IOException("Failed to create directory: " + file.getParentFile());
            }
            if (!file.createNewFile()) {
                throw new IOException("Failed to create new file: " + file);
            }
        }

        return file;
    }
}