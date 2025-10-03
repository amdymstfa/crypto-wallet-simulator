package service;

import model.*;
import enums.*;
import util.LoggerUtil;
import java.time.Duration;
import java.util.*;
import java.security.SecureRandom;

/**
 * Service to manage the mempool and simulate network activity
 */
public class MempoolService {
    
    private final Mempool mempool;
    private final TransactionService transactionService;
    private final SecureRandom random;
    
    public MempoolService(TransactionService transactionService) {
        this.mempool = new Mempool();
        this.transactionService = transactionService;
        this.random = new SecureRandom();
        LoggerUtil.logInfo("MempoolService initialized");
    }
    
    /**
     * Adds a transaction to the mempool
     */
    public void addTransaction(Transaction transaction) {
        mempool.addTransaction(transaction);
        LoggerUtil.logTransaction(transaction.getId(), "ADDED_TO_MEMPOOL", 
            String.format("Position: %d/%d", getPosition(transaction), getSize()));
    }
    
    /**
     * Generates random transactions to simulate network activity
     */
    public void generateRandomTransactions(int count) {
        LoggerUtil.logInfo(String.format("Generating %d random transactions", count));
        
        String[] btcAddresses = {
            "1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa", "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2",
            "1C4dKM9RkWz2UqRdN9o4KZcXF8GYqF7XzG", "1D5zA7ZuTjp1UPCnPJzqV4hEqFxq2V6jNN"
        };
        
        String[] ethAddresses = {
            "0x742d35Cc6634C0532925a3b8D9f4e676C2fC2f36", "0x8ba1f109551bD432803012645Hac136c69Ad217B",
            "0x267be1C1D684F78cb4F6a176C4911b741E4Ffdc0", "0xb47e3cd837dDF8e4c57F05d70Ab865de6e193BBB"
        };
        
        for (int i = 0; i < count; i++) {
            try {
                CryptoType type = random.nextBoolean() ? CryptoType.BITCOIN : CryptoType.ETHEREUM;
                String[] addresses = (type == CryptoType.BITCOIN) ? btcAddresses : ethAddresses;
                
                String fromAddr = addresses[random.nextInt(addresses.length)];
                String toAddr = addresses[random.nextInt(addresses.length)];
                while (toAddr.equals(fromAddr)) {
                    toAddr = addresses[random.nextInt(addresses.length)];
                }
                
                double amount = 0.001 + (random.nextDouble() * 0.999); // 0.001 to 1.0
                FeeLevel feeLevel = FeeLevel.values()[random.nextInt(FeeLevel.values().length)];
                
                Transaction tx = transactionService.createTransaction(fromAddr, toAddr, amount, type, feeLevel);
                mempool.addTransaction(tx);
                
            } catch (Exception e) {
                LoggerUtil.logError("Error generating random transaction", e);
            }
        }
    }
    
    /**
     * Get the position of a transaction in the mempool
     */
    public int getPosition(Transaction transaction) {
        return mempool.getPosition(transaction);
    }
    
    /**
     * Estimate waiting time for a transaction
     */
    public Duration estimateWaitingTime(Transaction transaction) {
        return mempool.estimateWaitingTime(transaction);
    }
    
    /**
     * Get the current state of the mempool sorted by fees
     */
    public List<Transaction> getMempoolState() {
        return mempool.getTransactionsByFees();
    }
    
    /**
     * Simulate confirming transactions (remove from mempool)
     */
    public List<Transaction> confirmTransactions(int count) {
        List<Transaction> txToConfirm = mempool.getTransactionsByFees();
        List<Transaction> confirmed = new ArrayList<>();
        
        for (int i = 0; i < Math.min(count, txToConfirm.size()); i++) {
            Transaction tx = txToConfirm.get(i);
            tx.setStatus(TransactionStatus.CONFIRMED);
            mempool.removeTransaction(tx);
            confirmed.add(tx);
            
            LoggerUtil.logTransaction(tx.getId(), "CONFIRMED", "Removed from mempool");
        }
        
        return confirmed;
    }
    
    /**
     * Get current mempool size
     */
    public int getSize() {
        return mempool.size();
    }
    
    /**
     * Check if mempool is empty
     */
    public boolean isEmpty() {
        return mempool.isEmpty();
    }
    
    /**
     * Get detailed statistics of the mempool
     */
    public String getMempoolStats() {
        List<Transaction> transactions = mempool.getTransactionsByFees();
        
        if (transactions.isEmpty()) {
            return "Mempool is empty";
        }
        
        double highestFees = transactions.get(0).getFees();
        double lowestFees = transactions.get(transactions.size() - 1).getFees();
        double avgFees = transactions.stream()
                .mapToDouble(Transaction::getFees)
                .average()
                .orElse(0.0);
        
        return String.format("Mempool: %d transactions | Fees: %.8f (min) - %.8f (max) - %.8f (avg)", 
            transactions.size(), lowestFees, highestFees, avgFees);
    }
}
