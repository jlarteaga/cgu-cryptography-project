package main.util;

public class Logger {

    /**
     * Included to hide the implicit public constructor
     */
    private Logger() {
    }

    public static void info() {
        System.out.println();
    }

    public static void info(String message) {
        System.out.println(message);
    }
}
