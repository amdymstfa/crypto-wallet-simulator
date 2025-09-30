package model;

import enums.CryptoType;
import util.UUIDGenerator;
import util.LoggerUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class representing a crypto wallet
 * Template Method Pattern
 */
public abstract class Wallet {
    
    // Protected attributes for subclasses
    protected final String id;
    protected final String address;
    protected final CryptoType type;
    protected double balance;
    protected final LocalDateTime createdAt;
    protected final List<Transaction> transactions;
    
    /**
     * Protected constructor for subclasses
     * @param type The cryptocurrency type
     */
    protected Wallet(CryptoType type) {
        this.id = UUIDGenerator.generate();
        this.type = type;
        this.address = generateAddress();
        this.balance = 0.0;
        this.createdAt = LocalDateTime.now();
        this.transactions = new ArrayList<>();
        
        LoggerUtil.logInfo(String.format("Wallet created: %s [%s] - Address: %s", 
            id.substring(0, 8), type, address));
    }
    
    // ===== GETTERS =====
    
    /**
     * Gets the wallet ID
     * @return The wallet unique identifier
     */
    public String getId() { 
        return id; 
    }
    
    /**
     * Gets the wallet address
     * @return The crypto address
     */
    public String getAddress() { 
        return address; 
    }
    
    /**
     * Gets the cryptocurrency type
     * @return The type (BITCOIN or ETHEREUM)
     */
    public CryptoType getType() { 
        return type; 
    }
    
    /**
     * Gets the current balance
     * @return The balance in crypto units
     */
    public double getBalance() { 
        return balance; 
    }
    
    /**
     * Gets the creation timestamp
     * @return The creation date and time
     */
    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    
    /**
     * Gets all transactions associated with this wallet
     * @return A copy of the transactions list
     */
    public List<Transaction> getTransactions() { 
        return new ArrayList<>(transactions); 
    }
    
    // ===== BUSINESS METHODS =====
    
    /**
     * Adds a transaction to the wallet
     * @param transaction The transaction to add
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        LoggerUtil.logTransaction(transaction.getId(), "ADDED_TO_WALLET", 
            String.format("Wallet: %s", id.substring(0, 8)));
    }
    
    /**
     * Updates the wallet balance
     * @param newBalance The new balance value
     */
    public void updateBalance(double newBalance) {
        double oldBalance = this.balance;
        this.balance = newBalance;
        
        LoggerUtil.logInfo(String.format("Balance updated for wallet %s: %.8f -> %.8f %s", 
            id.substring(0, 8), oldBalance, newBalance, type.getSymbol()));
    }
    
    /**
     * Abstract method to generate a type-specific address
     * Template Method Pattern
     * @return The generated address
     */
    protected abstract String generateAddress();
    
    /**
     * Checks if the wallet can afford a transaction
     * @param amount The amount to send
     * @param fees The transaction fees
     * @return true if wallet has sufficient funds
     */
    public boolean canAfford(double amount, double fees) {
        return balance >= (amount + fees);
    }
    
    /**
     * String representation of the wallet
     */
    @Override
    public String toString() {
        return String.format("Wallet[%s] %s - Balance: %.8f %s", 
            id.substring(0, 8), address, balance, type.getSymbol());
    }
}