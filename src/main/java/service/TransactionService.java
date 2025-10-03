package service;

import model.*;
import enums.*;
import exception.*;
import util.LoggerUtil;
import util.AddressValidator;
import java.util.*;

/**
 * Service to manage transactions
 */
public class TransactionService {
    
    private final Map<CryptoType, FeeCalculator> feeCalculators;
    private final WalletService walletService;
    private final List<Transaction> transactions;
    
    public TransactionService(WalletService walletService) {
        this.walletService = walletService;
        this.transactions = new ArrayList<>();
        this.feeCalculators = new HashMap<>();
        
        // Initialize fee calculators
        feeCalculators.put(CryptoType.BITCOIN, new BitcoinFeeCalculator());
        feeCalculators.put(CryptoType.ETHEREUM, new EthereumFeeCalculator());
        
        LoggerUtil.logInfo("TransactionService initialized with fee calculators");
    }
    
    /**
     * Create a new transaction with validations
     */
    public Transaction createTransaction(String fromAddress, String toAddress, 
                                         double amount, CryptoType type, FeeLevel feeLevel) 
            throws InvalidAddressException, InvalidAmountException, TransactionException {
        
        // Validate inputs
        validateTransactionInputs(fromAddress, toAddress, amount, type);
        
        // Create transaction
        Transaction transaction = new Transaction(fromAddress, toAddress, amount, type, feeLevel);
        
        // Calculate fees
        FeeCalculator calculator = feeCalculators.get(type);
        double fees = calculator.calculateFees(transaction);
        transaction.setFees(fees);
        
        // Store transaction
        transactions.add(transaction);
        
        LoggerUtil.logTransaction(transaction.getId(), "CREATED", 
            String.format("Fees: %.8f %s", fees, type.getSymbol()));
        
        return transaction;
    }
    
    /**
     * Validate transaction inputs
     */
    private void validateTransactionInputs(String fromAddress, String toAddress, 
                                           double amount, CryptoType type) 
            throws InvalidAddressException, InvalidAmountException {
        
        // Validate addresses
        AddressValidator.validateAddress(fromAddress, type);
        AddressValidator.validateAddress(toAddress, type);
        
        // Validate amount
        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }
        
        // Ensure addresses are different
        if (fromAddress.equals(toAddress)) {
            throw new InvalidAddressException("Source and destination addresses must be different");
        }
    }
    
    /**
     * Compare fees for all 3 priority levels
     */
    public Map<FeeLevel, Double> compareFees(double amount, CryptoType type) {
        FeeCalculator calculator = feeCalculators.get(type);
        Map<FeeLevel, Double> comparison = new HashMap<>();
        
        for (FeeLevel level : FeeLevel.values()) {
            double fees = calculator.calculateFees(amount, level);
            comparison.put(level, fees);
        }
        
        LoggerUtil.logInfo(String.format("Fee comparison for %s and amount %.8f", type, amount));
        return comparison;
    }
    
    /**
     * Find a transaction by ID
     */
    public Optional<Transaction> findById(String transactionId) {
        return transactions.stream()
                .filter(tx -> tx.getId().equals(transactionId))
                .findFirst();
    }
    
    /**
     * Get all transactions of a specific type
     */
    public List<Transaction> getTransactionsByType(CryptoType type) {
        return transactions.stream()
                .filter(tx -> tx.getType() == type)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Get all transactions by status
     */
    public List<Transaction> getTransactionsByStatus(TransactionStatus status) {
        return transactions.stream()
                .filter(tx -> tx.getStatus() == status)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Update the status of a transaction
     */
    public void updateTransactionStatus(String transactionId, TransactionStatus newStatus) 
            throws TransactionException {
        Optional<Transaction> optTx = findById(transactionId);
        if (optTx.isPresent()) {
            optTx.get().setStatus(newStatus);
        } else {
            throw new TransactionException("Transaction not found", transactionId);
        }
    }
    
    /**
     * Transaction statistics
     */
    public String getTransactionStats() {
        long pendingCount = transactions.stream()
                .filter(tx -> tx.getStatus() == TransactionStatus.PENDING)
                .count();
        long confirmedCount = transactions.stream()
                .filter(tx -> tx.getStatus() == TransactionStatus.CONFIRMED)
                .count();
        
        return String.format("Transactions: %d total (%d pending, %d confirmed)", 
            transactions.size(), pendingCount, confirmedCount);
    }
}
