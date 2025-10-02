package util;

import model.Transaction;
import enums.FeeLevel;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Utility for elegant console display
 * ASCII tables, titles, formatted messages
 */
public class ConsolePrinter {
    
    // ASCII table characters
    private static final String HORIZONTAL = "─";
    private static final String VERTICAL = "│";
    private static final String TOP_LEFT = "┌";
    private static final String TOP_RIGHT = "┐";
    private static final String BOTTOM_LEFT = "└";
    private static final String BOTTOM_RIGHT = "┘";
    private static final String CROSS = "┼";
    private static final String T_DOWN = "┬";
    private static final String T_UP = "┴";
    private static final String T_RIGHT = "├";
    private static final String T_LEFT = "┤";
    
    /**
     * Repeats a string n times (Java 8 compatible)
     */
    private static String repeat(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
    
    /**
     * Displays main title with border
     */
    public static void printTitle(String title) {
        int width = 60;
        System.out.println("\n" + repeat("═", width));
        System.out.println(centerText(title, width));
        System.out.println(repeat("═", width) + "\n");
    }
    
    /**
     * Displays subtitle
     */
    public static void printSubTitle(String subtitle) {
        System.out.println("\n" + subtitle);
        System.out.println(repeat("─", subtitle.length()) + "\n");
    }
    
    /**
     * Displays success message
     */
    public static void printSuccess(String message) {
        System.out.println("✅ " + message);
    }
    
    /**
     * Displays error message
     */
    public static void printError(String message) {
        System.out.println("❌ " + message);
    }
    
    /**
     * Displays info message
     */
    public static void printInfo(String message) {
        System.out.println("ℹ️  " + message);
    }
    
    /**
     * Displays warning message
     */
    public static void printWarning(String message) {
        System.out.println("⚠️  " + message);
    }
    
    /**
     * Displays separator line
     */
    public static void printSeparator() {
        System.out.println(repeat("─", 60));
    }
    
    /**
     * Displays main menu
     */
    public static void printMainMenu() {
        printTitle("🪙 CRYPTO WALLET SIMULATOR 🪙");
        
        System.out.println("1️⃣  Create crypto wallet");
        System.out.println("2️⃣  Create new transaction");
        System.out.println("3️⃣  View position in mempool");
        System.out.println("4️⃣  Compare 3 fee levels");
        System.out.println("5️⃣  View mempool state");
        System.out.println("6️⃣  Display my wallets");
        System.out.println("7️⃣  Global statistics");
        System.out.println("0️⃣  Quit");
        
        printSeparator();
        System.out.print("👉 Your choice: ");
    }
    
    /**
     * Compares 3 fee levels in a table
     */
    public static void printFeeComparison(double amount, String cryptoSymbol, 
                                         Map<FeeLevel, Double> feesMap,
                                         Map<FeeLevel, Integer> positionsMap) {
        
        printSubTitle("💰 Fee Levels Comparison");
        
        System.out.println("Transaction amount: " + amount + " " + cryptoSymbol + "\n");
        
        // Table header
        System.out.println(TOP_LEFT + repeat(HORIZONTAL, 15) + T_DOWN + repeat(HORIZONTAL, 18) + T_DOWN + 
                          repeat(HORIZONTAL, 15) + T_DOWN + repeat(HORIZONTAL, 20) + TOP_RIGHT);
        System.out.println(VERTICAL + centerText("Level", 15) + VERTICAL + 
                          centerText("Fees (" + cryptoSymbol + ")", 18) + VERTICAL +
                          centerText("Position", 15) + VERTICAL +
                          centerText("Estimated time", 20) + VERTICAL);
        System.out.println(T_RIGHT + repeat(HORIZONTAL, 15) + CROSS + repeat(HORIZONTAL, 18) + CROSS + 
                          repeat(HORIZONTAL, 15) + CROSS + repeat(HORIZONTAL, 20) + T_LEFT);
        
        // Data rows
        for (FeeLevel level : FeeLevel.values()) {
            double fees = feesMap.get(level);
            int position = positionsMap.getOrDefault(level, 0);
            int minutes = position * 10;
            
            System.out.println(VERTICAL + 
                padRight(level.name(), 15) + VERTICAL +
                padRight(String.format("%.8f", fees), 18) + VERTICAL +
                padRight("~" + position, 15) + VERTICAL +
                padRight(minutes + " min", 20) + VERTICAL);
        }
        
        // Table footer
        System.out.println(BOTTOM_LEFT + repeat(HORIZONTAL, 15) + T_UP + repeat(HORIZONTAL, 18) + T_UP + 
                          repeat(HORIZONTAL, 15) + T_UP + repeat(HORIZONTAL, 20) + BOTTOM_RIGHT);
        
        System.out.println("\n💡 Higher fees = faster confirmation.");
    }
    
    /**
     * Displays mempool state with transactions
     */
    public static void printMempoolState(List<Transaction> transactions, String myTransactionId) {
        printSubTitle("📊 Current Mempool State");
        
        System.out.println("Pending transactions: " + transactions.size() + "\n");
        
        if (transactions.isEmpty()) {
            printInfo("Mempool is empty");
            return;
        }
        
        // Header
        System.out.println(TOP_LEFT + repeat(HORIZONTAL, 20) + T_DOWN + repeat(HORIZONTAL, 12) + T_DOWN + 
                          repeat(HORIZONTAL, 15) + T_DOWN + repeat(HORIZONTAL, 12) + TOP_RIGHT);
        System.out.println(VERTICAL + centerText("Transaction", 20) + VERTICAL + 
                          centerText("Type", 12) + VERTICAL +
                          centerText("Fees", 15) + VERTICAL +
                          centerText("Level", 12) + VERTICAL);
        System.out.println(T_RIGHT + repeat(HORIZONTAL, 20) + CROSS + repeat(HORIZONTAL, 12) + CROSS + 
                          repeat(HORIZONTAL, 15) + CROSS + repeat(HORIZONTAL, 12) + T_LEFT);
        
        // Display top 10 transactions
        int count = 0;
        for (Transaction tx : transactions) {
            if (count >= 10) break;
            
            String txDisplay = tx.getId().equals(myTransactionId) ? 
                              ">>> " + tx.getShortId() + " <<<" : tx.getShortId() + " (anonymous)";
            
            String marker = tx.getId().equals(myTransactionId) ? " ← YOU" : "";
            
            System.out.println(VERTICAL + 
                padRight(txDisplay, 20) + VERTICAL +
                padRight(tx.getType().getSymbol(), 12) + VERTICAL +
                padRight(String.format("%.8f", tx.getFees()), 15) + VERTICAL +
                padRight(tx.getFeeLevel().name(), 12) + VERTICAL + marker);
            
            count++;
        }
        
        // Footer
        System.out.println(BOTTOM_LEFT + repeat(HORIZONTAL, 20) + T_UP + repeat(HORIZONTAL, 12) + T_UP + 
                          repeat(HORIZONTAL, 15) + T_UP + repeat(HORIZONTAL, 12) + BOTTOM_RIGHT);
        
        if (transactions.size() > 10) {
            System.out.println("\n... and " + (transactions.size() - 10) + " other transactions");
        }
    }
    
    /**
     * Displays transaction details
     */
    public static void printTransactionDetails(Transaction tx, int position, Duration waitTime) {
        printSubTitle("📄 Transaction Details");
        
        System.out.println("ID: " + tx.getId());
        System.out.println("From: " + tx.getFromAddress());
        System.out.println("To: " + tx.getToAddress());
        System.out.println("Amount: " + tx.getAmount() + " " + tx.getType().getSymbol());
        System.out.println("Fees: " + tx.getFees() + " " + tx.getType().getSymbol());
        System.out.println("Total: " + tx.getTotalAmount() + " " + tx.getType().getSymbol());
        System.out.println("Fee level: " + tx.getFeeLevel() + " - " + tx.getFeeLevel().getDescription());
        System.out.println("Status: " + tx.getStatus().getDisplayName());
        System.out.println("Created: " + tx.getCreatedAt());
        System.out.println("\n📍 Position in mempool: " + position);
        System.out.println("⏱️  Estimated waiting time: " + waitTime.toMinutes() + " minutes");
    }
    
    /**
     * Displays loading message
     */
    public static void printLoading(String message) {
        System.out.print(message);
        try {
            for (int i = 0; i < 3; i++) {
                Thread.sleep(300);
                System.out.print(".");
            }
            System.out.println();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Clears console (simulation)
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    /**
     * Waits for Enter key press
     */
    public static void waitForEnter() {
        System.out.print("\n👉 Press Enter to continue...");
        try {
            System.in.read();
        } catch (Exception e) {
            // Ignore
        }
    }
    
    // ===== UTILITY METHODS =====
    
    private static String centerText(String text, int width) {
        if (text.length() >= width) return text.substring(0, width);
        int leftPadding = (width - text.length()) / 2;
        int rightPadding = width - text.length() - leftPadding;
        return repeat(" ", leftPadding) + text + repeat(" ", rightPadding);
    }
    
    private static String padRight(String text, int width) {
        if (text.length() >= width) return text.substring(0, width);
        return " " + text + repeat(" ", width - text.length() - 1);
    }
    
    private static String padLeft(String text, int width) {
        if (text.length() >= width) return text.substring(0, width);
        return repeat(" ", width - text.length() - 1) + text + " ";
    }
}