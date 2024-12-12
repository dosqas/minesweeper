package cz.cuni.mff.java.hp.minesweeper.gui;

import cz.cuni.mff.java.hp.minesweeper.gui.components.MainMenu;
import cz.cuni.mff.java.hp.minesweeper.gui.components.play.*;
import cz.cuni.mff.java.hp.minesweeper.gui.components.settings.*;
import cz.cuni.mff.java.hp.minesweeper.gui.components.exit.*;

import cz.cuni.mff.java.hp.minesweeper.gui.components.statistics.*;
import cz.cuni.mff.java.hp.minesweeper.gui.components.tutorial.*;
import cz.cuni.mff.java.hp.minesweeper.service.Service;
import cz.cuni.mff.java.hp.minesweeper.utils.globals.GlobalVariablesGUI;
import cz.cuni.mff.java.hp.minesweeper.utils.gui.AudioPlayer;

import javax.swing.*;

/**
 * The `GUI` class manages the graphical user interface of the Minesweeper game.
 * It handles the navigation between different screens and the initialization of the game.
 */
public class GUI {
    public final Service service;

    /**
     * Constructs a `GUI` object and initializes the service.
     */
    public GUI() {
        this.service = new Service();
    }

    /**
     * Starts the GUI by setting the icon image, loading stats, and running the main menu.
     */
    public void start() {
        new Thread(() -> {
            ImageIcon icon = new ImageIcon("/textures/misc/icon.png");
            GlobalVariablesGUI.FRAME.setIconImage(icon.getImage());
        }).start();
        service.loadStats();
        runMainMenu();
    }

    /**
     * Runs the main menu and handles the navigation based on the user's selection.
     */
    private void runMainMenu() {
        if (AudioPlayer.getCurrentTheme() != 0) {
            AudioPlayer.stopMusic();
            AudioPlayer.playMusic("/audio/music/mainMenu.wav");
        }
        MainMenu mainMenu = new MainMenu();

        int returnCode = mainMenu.run();
        switch (returnCode) {
            case 0 -> runPlay();
            case 1 -> runTutorial();
            case 2 -> runStatistics();
            case 3 -> runSettings();
            case 4 -> runExit();
        }
    }

    /**
     * Runs the play menu and handles the navigation based on the user's selection.
     */
    private void runPlay() {
        PlayMainScreen playMainScreen = new PlayMainScreen();

        int returnCode = playMainScreen.run();
        switch (returnCode) {
            case 0 -> runStandardGame();
            case 1 -> runTimeChallenge();
            case 2 -> runMainMenu();
        }
    }

    /**
     * Runs the time challenge menu and handles the navigation based on the user's selection.
     */
    private void runTimeChallenge() {
        PlayTimedChallengeScreen playTimedChallengeScreen = new PlayTimedChallengeScreen();

        int returnCode = playTimedChallengeScreen.run();
        if (returnCode == 1) {
            runStandardGame();
        }
        else runPlay();
    }

    /**
     * Runs the standard game menu and handles the navigation based on the user's selection.
     */
    private void runStandardGame() {
        PlayDifficultyScreen playDifficultyScreen = new PlayDifficultyScreen();

        int returnCode = playDifficultyScreen.run();
        switch (returnCode) {
            case 0 -> runPlay();
            case 1 -> runStartingGame();
            case 2 -> runTimeChallenge();
        }
    }

    /**
     * Runs the starting game screen and handles the navigation based on the user's selection.
     */
    private void runStartingGame() {
        PlayStartingGameScreen playStartingScreen = new PlayStartingGameScreen();

        int returnCode = playStartingScreen.run();
        if (returnCode == 0) {
            runGameScreen();
        }
    }

    /**
     * Runs the game screen and handles the navigation based on the user's selection.
     */
    private void runGameScreen() {
        PlayGameScreen playGameScreen = new PlayGameScreen(this.service);

        int returnCode = playGameScreen.run();
        switch (returnCode) {
            case 0 -> runMainMenu();
            case 1 -> runWinScreen();
            case 2 -> runLoseScreen();
        }
    }

    /**
     * Runs the win screen and handles the navigation based on the user's selection.
     */
    private void runWinScreen() {
        PlayWonMatchScreen playWinScreen = new PlayWonMatchScreen();

        int returnCode = playWinScreen.run();
        if (returnCode == 0) {
            runMainMenu();
        }
    }

    /**
     * Runs the loss screen and handles the navigation based on the user's selection.
     */
    private void runLoseScreen() {
        PlayLostMatchScreen playLoseScreen = new PlayLostMatchScreen();

        int returnCode = playLoseScreen.run();
        if (returnCode == 0) {
            runMainMenu();
        }
        else runStartingGame();
    }

    /**
     * Runs the tutorial menu and handles the navigation based on the user's selection.
     */
    private void runTutorial() {
        TutorialMainScreen tutorialMainScreen = new TutorialMainScreen();

        int returnCode = tutorialMainScreen.run();
        switch (returnCode) {
            case 0 -> runHowToPlay();
            case 1 -> runPowerUpsTutorial();
            case 2 -> runMainMenu();
        }
    }

    /**
     * Runs the how-to-play tutorial screen and handles the navigation based on the user's selection.
     */
    private void runHowToPlay() {
        TutorialHowToPlayScreen tutorialHowToPlayScreen = new TutorialHowToPlayScreen();

        int returnCode = tutorialHowToPlayScreen.run();
        if (returnCode == 0) {
            runTutorial();
        }
    }

    /**
     * Runs the power-ups tutorial screen and handles the navigation based on the user's selection.
     */
    private void runPowerUpsTutorial() {
        TutorialPowerUpsScreen tutorialPowerUpsScreen = new TutorialPowerUpsScreen();

        int returnCode = tutorialPowerUpsScreen.run();
        if (returnCode == 0) {
            runTutorial();
        }
    }

    /**
     * Runs the statistics menu and handles the navigation based on the user's selection.
     */
    private void runStatistics() {
        StatisticsMainScreen statisticsMainScreen = new StatisticsMainScreen();

        int returnCode = statisticsMainScreen.run();
        switch (returnCode) {
            case 0 -> runTopScores();
            case 1 -> runGameStats();
            case 2 -> runMainMenu();
        }
    }

    /**
     * Runs the top scores screen and handles the navigation based on the user's selection.
     */
    private void runTopScores() {
        StatisticsTopScoresScreen statisticsTopScoresScreen = new StatisticsTopScoresScreen();

        int returnCode = statisticsTopScoresScreen.run();

        if (returnCode == 0) {
            runStatistics();
        }
    }

    /**
     * Runs the game stats screen and handles the navigation based on the user's selection.
     */
    private void runGameStats() {
        StatisticsGameStatsScreen statisticsGameStatsScreen = new StatisticsGameStatsScreen();

        int returnCode = statisticsGameStatsScreen.run();

        if (returnCode == 0) {
            runStatistics();
        }
    }

    /**
     * Runs the settings menu and handles the navigation based on the user's selection.
     */
    private void runSettings() {
        SettingsMainScreen settingsMainScreen = new SettingsMainScreen();

        int returnCode = settingsMainScreen.run();
        switch (returnCode) {
            case 0 -> runPowerUps();
            case 1 -> runTheme();
            case 2 -> runControls();
            case 3 -> runAudio();
            case 4 -> runMainMenu();
        }
    }

    /**
     * Runs the power-ups settings screen and handles the navigation based on the user's selection.
     */
    private void runPowerUps() {
        SettingsPowerUpsScreen settingsPowerUpsScreen = new SettingsPowerUpsScreen();

        int returnCode = settingsPowerUpsScreen.run();
        if (returnCode == 0) {
            runSettings();
        }
        else runPowerUps();
    }

    /**
     * Runs the theme settings screen and handles the navigation based on the user's selection.
     */
    private void runTheme() {
        SettingsThemesScreen settingsThemesScreen = new SettingsThemesScreen();

        int returnCode = settingsThemesScreen.run();
        if (returnCode == 0) {
            runSettings();
        }
        else runTheme();
    }

    /**
     * Runs the controls settings screen and handles the navigation based on the user's selection.
     */
    private void runControls() {
        SettingsControlsScreen settingsControlsScreen = new SettingsControlsScreen();

        int returnCode = settingsControlsScreen.run();
        if (returnCode == 0) {
            runSettings();
        }
    }

    /**
     * Runs the audio settings screen and handles the navigation based on the user's selection.
     */
    private void runAudio() {
        SettingsAudioScreen settingsAudioScreen = new SettingsAudioScreen();

        int returnCode = settingsAudioScreen.run();
        if (returnCode == 0) {
            runSettings();
        }
    }

    /**
     * Runs the exit confirmation screen and handles the navigation based on the user's selection.
     */
    private void runExit() {
        ExitConfirmScreen exitConfirmScreen = new ExitConfirmScreen();

        int returnCode = exitConfirmScreen.run();
        if (returnCode == 0) {
            runExitScreen();
        } else {
            AudioPlayer.playMusic("/audio/music/mainMenu.wav");
            runMainMenu();
        }
    }

    /**
     * Runs the exit screen.
     */
    private void runExitScreen() {
        ExitGoodbyeScreen.run();
    }
}