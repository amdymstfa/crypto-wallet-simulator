package app;

import service.*;
import repository.*;

public class ApplicationContext {
    
    private static ApplicationContext instance;
    
    // Services
    private final WalletService walletService;
    private final TransactionService transactionService;
    private final MempoolService mempoolService;
    
    // Repositories
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final MempoolRepository mempoolRepository;
    
    private ApplicationContext() {
        // Initialize repositories
        this.walletRepository = new WalletRepository();
        this.transactionRepository = new TransactionRepository();
        this.mempoolRepository = new MempoolRepository();
        
        // Initialize services
        this.walletService = new WalletService();
        this.transactionService = new TransactionService(walletService);
        this.mempoolService = new MempoolService(transactionService);
    }
    

    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }
    
    // Getters
    public WalletService getWalletService() { return walletService; }
    public TransactionService getTransactionService() { return transactionService; }
    public MempoolService getMempoolService() { return mempoolService; }
    public WalletRepository getWalletRepository() { return walletRepository; }
    public TransactionRepository getTransactionRepository() { return transactionRepository; }
    public MempoolRepository getMempoolRepository() { return mempoolRepository; }
}