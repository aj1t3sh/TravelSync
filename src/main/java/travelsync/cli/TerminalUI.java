package travelsync.cli;

import java.util.List;
import java.util.Scanner;

/**
 * Terminal UI Widget Renderer and Robust Input Handler for Eclipse Console.
 */
public class TerminalUI {

    private static final String HORIZONTAL_DOUBLE = "═";
    private static final String HORIZONTAL_SINGLE = "─";

    public static void printBanner() {
        System.out.println(ConsoleColors.brightCyan(
            "╔══════════════════════════════════════════════════════════════════════════════════╗\n" +
            "║                                  TRAVELSYNC                                      ║\n" +
            "║             Intelligent Travel Planning & Information Platform                   ║\n" +
            "║                 Data Structures & Algorithms - III (25CS2103E)                   ║\n" +
            "╚══════════════════════════════════════════════════════════════════════════════════╝"
        ));
    }

    public static void printSectionHeader(String title) {
        String line = HORIZONTAL_DOUBLE.repeat(82);
        System.out.println("\n" + ConsoleColors.cyan("╔" + line + "╗"));
        System.out.println(ConsoleColors.cyan(String.format("║  %-80s║", ConsoleColors.bold(title))));
        System.out.println(ConsoleColors.cyan("╚" + line + "╝"));
    }

    public static void printSubHeader(String title) {
        System.out.println("\n" + ConsoleColors.yellow("─── [ " + title + " ] " + "─".repeat(Math.max(5, 75 - title.length()))));
    }

    public static void printSuccess(String message) {
        System.out.println(ConsoleColors.green("  ✓ " + message));
    }

    public static void printInfo(String message) {
        System.out.println(ConsoleColors.cyan("  ℹ " + message));
    }

    public static void printWarning(String message) {
        System.out.println(ConsoleColors.yellow("  ⚠ " + message));
    }

    public static void printError(String message) {
        System.out.println(ConsoleColors.red("  ✗ " + message));
    }

    /**
     * Reads a valid integer choice within the specified range [min, max].
     */
    public static int readIntChoice(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(ConsoleColors.bold(prompt));
            if (!scanner.hasNextLine()) {
                return min; // EOF safety fallback
            }
            String line = scanner.nextLine().trim();
            try {
                int val = Integer.parseInt(line);
                if (val >= min && val <= max) {
                    return val;
                }
                printWarning(String.format("Please enter a valid choice between %d and %d.", min, max));
            } catch (NumberFormatException e) {
                printWarning("Invalid input. Please enter a valid number.");
            }
        }
    }

    /**
     * Reads a string input with a default fallback if empty.
     */
    public static String readString(Scanner scanner, String prompt, String defaultValue) {
        System.out.print(ConsoleColors.bold(prompt + (defaultValue != null ? " [Default: " + defaultValue + "]: " : ": ")));
        if (!scanner.hasNextLine()) {
            return defaultValue != null ? defaultValue : "";
        }
        String line = scanner.nextLine().trim();
        if (line.isEmpty() && defaultValue != null) {
            return defaultValue;
        }
        return line;
    }

    /**
     * Reads a positive integer with default fallback.
     */
    public static int readPositiveInt(Scanner scanner, String prompt, int defaultValue) {
        while (true) {
            System.out.print(ConsoleColors.bold(prompt + " [Default: " + defaultValue + "]: "));
            if (!scanner.hasNextLine()) {
                return defaultValue;
            }
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                return defaultValue;
            }
            try {
                int val = Integer.parseInt(line);
                if (val > 0) return val;
                printWarning("Please enter a positive integer greater than 0.");
            } catch (NumberFormatException e) {
                printWarning("Invalid input. Please enter a valid number.");
            }
        }
    }

    public static void pressEnterToContinue(Scanner scanner) {
        System.out.print(ConsoleColors.purple("\nPress [Enter] to return to menu..."));
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }
}
