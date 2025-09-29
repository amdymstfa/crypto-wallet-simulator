package util;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.io.IOException;

public class LoggerUtil {

    private final static Logger logger = Logger.getLogger("CryptoWalletSimulator");

    static {
        try {
            // Allow all log levels (INFO, WARNING, SEVERE, DEBUG...)
            logger.setLevel(Level.ALL);

            // Disable default configuration (prevents duplicate logs)
            logger.setUseParentHandlers(false);

            // Console handler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL); 
            consoleHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(consoleHandler);

            // File handler
            FileHandler fileHandler = new FileHandler("CryptoWallet.log", true); 
            fileHandler.setLevel(Level.ALL); 
            fileHandler.setFormatter(new SimpleFormatter()); 
            logger.addHandler(fileHandler);

        } catch (IOException e) {
            logger.severe("Error initializing logger: " + e.getMessage());
        }
    }

    // Utility methods
    public static void logInfo(String message) {
        logger.info(message); 
    }

    public static void logError(String message) {
        logger.severe(message); 
    }

    public static void logError(String message, Throwable throwable) {
        logger.log(Level.SEVERE, message, throwable); 
    }

    public static void logDebug(String message) {
        logger.fine(message); 
    }

    public static void logWarning(String message) {
        logger.warning(message); 
    }

    public static void logTransaction(String transactionId, String operation, String details) {
        // Log transaction-specific messages
        logInfo(String.format("Transaction [%s] - %s : %s", transactionId, operation, details));
    }

    public static void logFeeCalculation(String transactionId, double fees, String method) {
        // Log fee calculation details
        logInfo(String.format("FEES [%s] - Calculated: %.8f via %s", transactionId, fees, method));
    }
}
