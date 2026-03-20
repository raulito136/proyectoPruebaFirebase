package com.example.moviecollection;

/**
 * Launcher class to start the application.
 * This is a workaround for a common issue with JavaFX applications where the main class
 * extending javafx.application.Application is not correctly launched by some IDEs or tools.
 */
public class Launcher {
    public static void main(String[] args) {
        Main.main(args);
    }
}
