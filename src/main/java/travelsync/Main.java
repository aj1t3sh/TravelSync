package travelsync;

import travelsync.api.ApiServer;
import travelsync.cli.ConsoleColors;
import travelsync.cli.MenuHandler;
import travelsync.cli.TerminalUI;
import travelsync.planner.TravelSyncEngine;

import java.util.Scanner;

/**
 * Main Application Entry Point for TravelSync.
 * Defaults to starting the high-performance Java REST API Server for the UI,
 * while seamlessly supporting CLI mode with --cli or automated demo with --demo.
 */
public class Main {

    public static void main(String[] args) {
        boolean isCli = args.length > 0 && ("--cli".equalsIgnoreCase(args[0]) || "-c".equalsIgnoreCase(args[0]));
        boolean isDemo = args.length > 0 && ("--demo".equalsIgnoreCase(args[0]) || "-d".equalsIgnoreCase(args[0]));

        if (isDemo || isCli) {
            runCliMode(isDemo);
            return;
        }

        // Default: Start TravelSync REST API Server
        runServerMode(args);
    }

    private static void runServerMode(String[] args) {
        int port = 8080;
        String envPort = System.getenv("PORT");
        if (envPort != null && !envPort.isEmpty()) {
            try {
                port = Integer.parseInt(envPort);
            } catch (NumberFormatException ignored) {}
        }
        if (args.length > 1 && ("--port".equalsIgnoreCase(args[0]) || "-p".equalsIgnoreCase(args[0]))) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {}
        }

        TerminalUI.printBanner();
        System.out.println(ConsoleColors.brightGreen("Starting TravelSync High-Performance REST API Server on port " + port + "..."));
        System.out.println(ConsoleColors.yellow("Note: Run with '--cli' for interactive Eclipse console, or '--demo' for automated test."));

        try {
            ApiServer server = new ApiServer(port);
            server.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nShutting down TravelSync REST Server...");
                server.stop();
            }));

            // Keep main thread alive
            Thread.currentThread().join();
        } catch (Exception e) {
            System.err.println("Fatal Error starting TravelSync REST API Server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void runCliMode(boolean autoDemo) {
        TerminalUI.printBanner();

        try {
            System.out.println(ConsoleColors.cyan("Initializing TravelSync Core Engine & Ingesting Datasets..."));
            TravelSyncEngine engine = new TravelSyncEngine();
            System.out.println(ConsoleColors.brightGreen("✓ Ingestion Completed: ") +
                    engine.getRepository().getAllDestinations().size() + " Destinations, " +
                    engine.getRepository().getAllTransportConnections().size() + " Transport Routes Loaded.");

            if (autoDemo) {
                System.out.println(ConsoleColors.yellow("\nRunning automated TravelSync full pipeline demonstration...\n"));
                Scanner scanner = new Scanner(System.in);
                MenuHandler handler = new MenuHandler(engine, scanner);
                handler.handleOption(10);
                return;
            }

            Scanner scanner = new Scanner(System.in);
            MenuHandler handler = new MenuHandler(engine, scanner);

            boolean running = true;
            while (running) {
                handler.displayMainMenu();
                int choice = TerminalUI.readIntChoice(scanner, "Enter your choice (0-12): ", 0, 12);
                if (choice == 0) {
                    handler.handleOption(0);
                    running = false;
                } else {
                    handler.handleOption(choice);
                }
            }
        } catch (Exception e) {
            System.err.println("Fatal Error during TravelSync execution: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
