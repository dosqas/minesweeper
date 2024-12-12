package cz.cuni.mff.java.hp.minesweeper;

import cz.cuni.mff.java.hp.minesweeper.gui.GUI;
import cz.cuni.mff.java.hp.minesweeper.service.Service;

/**
 * The `Main` class is the entry point for the Minesweeper application.
 * It initializes the GUI and sets up a shutdown hook to save game statistics.
 */
public class Main {

    /**
     * The main method that starts the Minesweeper application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("            \u001B[30;47m[MINESWEEPER]\u001B[0m");
        System.out.println("\u001B[30;47m*Created by Soptelea Sebastian @dosqas, 2024*\u001B[0m\n");

        GUI gui = new GUI();
        System.out.println("\u001B[31;40mStarting GUI...\u001B[0m");
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\u001B[31;40mGUI started.\u001B[0m");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n\u001B[31;40mSaving stats...\u001B[0m");
            Service.saveStats();

            try {
                Thread.sleep(750);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("\u001B[31;40mStats saved.\u001B[0m");
        }));

        gui.start();
    }
}
