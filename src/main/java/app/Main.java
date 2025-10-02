package app;

import util.ConsolePrinter;
import util.DatabaseConnection;
import util.LoggerUtil;
import java.util.Scanner;

/**
 * Main application entry point
 * Clean and focused on initialization and main loop
 */
public class Main {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        try {
            // Initialize application
            initializeApplication();
            
            // Get application context
            ApplicationContext context = ApplicationContext.getInstance();
            InputReader inputReader = new InputReader(scanner);
            MenuHandler menuHandler = new MenuHandler(context, inputReader);
            
            // Main application loop
            boolean running = true;
            while (running) {
                running = menuHandler.handleMainMenu();
            }
            
        } catch (Exception e) {
            ConsolePrinter.printError("Fatal error: " + e.getMessage());
            LoggerUtil.logError("Application fatal error", e);
        } finally {
            // Cleanup
            scanner.close();
            DatabaseConnection.getInstance().closeConnection();
        }
    }
    
    /**
     * Initializes the application
     */
    private static void initializeApplication() {
        try {
            ConsolePrinter.printTitle("🚀 Application Initialization");
            
            // Initialize context
            ConsolePrinter.printLoading("Initializing services");
            ApplicationContext.getInstance();
            
            // Test database connection
            ConsolePrinter.printLoading("Connecting to database");
            if (DatabaseConnection.getInstance().testConnection()) {
                ConsolePrinter.printSuccess("PostgreSQL connection established");
            } else {
                ConsolePrinter.printWarning("Database unavailable - Memory mode only");
            }
            
            // Generate random transactions
            ConsolePrinter.printLoading("Simulating blockchain network");
            ApplicationContext.getInstance().getMempoolService().generateRandomTransactions(15);
            
            ConsolePrinter.printSuccess("Application initialized successfully!");
            Thread.sleep(1500);
            
        } catch (Exception e) {
            ConsolePrinter.printError("Initialization error: " + e.getMessage());
            LoggerUtil.logError("Initialization error", e);
            System.exit(1);
        }
    }
}