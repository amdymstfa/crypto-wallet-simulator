package app;

import util.ConsolePrinter;
import util.LoggerUtil;

/**
 * Handles statistics display
 */
public class StatisticsHandler {
    
    private final ApplicationContext context;
    private final InputReader inputReader;
    
    public StatisticsHandler(ApplicationContext context, InputReader inputReader) {
        this.context = context;
        this.inputReader = inputReader;
    }

    private static String repeat(String str, int count) {
    StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
    }
        return sb.toString();
    }
    
    /**
     * Views global statistics
     */
    public void viewStatistics() {
        ConsolePrinter.printTitle("7️⃣  Global Statistics");
        
        try {
            System.out.println("💼 WALLETS");
           System.out.println(repeat("─", 40));
            System.out.println(context.getWalletService().getWalletStats());
            
            System.out.println("\n📊 TRANSACTIONS");
            System.out.println(repeat("─", 40));
            System.out.println(context.getTransactionService().getTransactionStats());
            
            System.out.println("\n🔄 MEMPOOL");
            System.out.println(repeat("─", 40));
            System.out.println(context.getMempoolService().getMempoolStats());
            
        } catch (Exception e) {
            ConsolePrinter.printError("Error: " + e.getMessage());
            LoggerUtil.logError("Statistics error", e);
        }
    }
}