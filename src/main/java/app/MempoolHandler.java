package app;

import model.Transaction;
import util.ConsolePrinter;
import util.LoggerUtil;
import java.time.Duration;
import java.util.List;

/**
 * Handles mempool-related operations
 */
public class MempoolHandler {
    
    private final ApplicationContext context;
    private final InputReader inputReader;
    
    public MempoolHandler(ApplicationContext context, InputReader inputReader) {
        this.context = context;
        this.inputReader = inputReader;
    }
    
    /**
     * Views position in mempool
     */
    public void viewMempoolPosition() {
        ConsolePrinter.printTitle("3️⃣  Position in Mempool");
        
        // Note: Need reference to lastTransaction from TransactionHandler
        ConsolePrinter.printWarning("Please create a transaction first (option 2)");
    }
    
    /**
     * Views mempool state
     */
    public void viewMempoolState() {
        ConsolePrinter.printTitle("5️⃣  Current Mempool State");
        
        try {
            List<Transaction> mempoolTx = context.getMempoolService().getMempoolState();
            ConsolePrinter.printMempoolState(mempoolTx, "");
            
            System.out.println("\n📊 " + context.getMempoolService().getMempoolStats());
            
        } catch (Exception e) {
            ConsolePrinter.printError("Error: " + e.getMessage());
            LoggerUtil.logError("Mempool state error", e);
        }
    }
}

