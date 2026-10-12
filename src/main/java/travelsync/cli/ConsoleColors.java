package travelsync.cli;

/**
 * ANSI Escape Codes for Terminal and Eclipse Console Styling.
 * Provides graceful degradation for consoles without ANSI color support.
 */
public class ConsoleColors {

    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";

    public static final String BRIGHT_RED = "\u001B[91m";
    public static final String BRIGHT_GREEN = "\u001B[92m";
    public static final String BRIGHT_YELLOW = "\u001B[93m";
    public static final String BRIGHT_BLUE = "\u001B[94m";
    public static final String BRIGHT_PURPLE = "\u001B[95m";
    public static final String BRIGHT_CYAN = "\u001B[96m";

    private static boolean enabled = true;

    public static void setEnabled(boolean state) {
        enabled = state;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static String color(String colorCode, String text) {
        if (!enabled) return text;
        return colorCode + text + RESET;
    }

    public static String cyan(String text) { return color(CYAN, text); }
    public static String green(String text) { return color(GREEN, text); }
    public static String yellow(String text) { return color(YELLOW, text); }
    public static String red(String text) { return color(RED, text); }
    public static String purple(String text) { return color(PURPLE, text); }
    public static String bold(String text) { return color(BOLD, text); }
    public static String brightCyan(String text) { return color(BRIGHT_CYAN, text); }
    public static String brightGreen(String text) { return color(BRIGHT_GREEN, text); }
}
