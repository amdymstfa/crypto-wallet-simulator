package app;

import util.ConsolePrinter;
import util.LoggerUtil;

/**
 * Handles menu display and navigation
 */
public class MenuHandler {
    
    private final ApplicationContext context;
    private final InputReader inputReader;
    private final WalletHandler walletHandler;
    private final TransactionHandler transactionHandler;
    private final MempoolHandler mempoolHandler;
    private final StatisticsHandler statisticsHandler;
    
    public MenuHandler(ApplicationContext context, InputReader inputReader) {
        this.context = context;
        this.inputReader = inputReader;
        this.walletHandler = new WalletHandler(context, inputReader);
        this.transactionHandler = new TransactionHandler(context, inputReader);
        this.mempoolHandler = new MempoolHandler(context, inputReader);
        this.statisticsHandler = new StatisticsHandler(context, inputReader);
    }
    
    /**
     * Displays and handles the main menu
     */
    public boolean handleMainMenu() {
        ConsolePrinter.clearScreen();
        ConsolePrinter.printMainMenu();
        
        // Validate menu choice (0-7)
        int choice = inputReader.readIntInRange("", 0, 7);
        
        try {
            switch (choice) {
                case 1:
                    walletHandler.createWallet();
                    break;
                case 2:
                    transactionHandler.createTransaction();
                    break;
                case 3:
                    mempoolHandler.viewMempoolPosition();
                    break;
                case 4:
                    transactionHandler.compareFees();
                    break;
                case 5:
                    mempoolHandler.viewMempoolState();
                    break;
                case 6:
                    walletHandler.viewMyWallets();
                    break;
                case 7:
                    statisticsHandler.viewStatistics();
                    break;
                case 0:
                    ConsolePrinter.printInfo("Goodbye! 👋");
                    return false;
                default:
                    ConsolePrinter.printError("Invalid choice");
                    break;
            }
            
            if (choice != 0) {
                inputReader.waitForEnter();
            }
            
        } catch (Exception e) {
            ConsolePrinter.printError("Error: " + e.getMessage());
            LoggerUtil.logError("Menu error", e);
            inputReader.waitForEnter();
        }
        
        return true;
    }
}