package model;

import enums.CryptoType;
import enums.FeeLevel;
import enums.TransactionStatus;
import util.UUIDGenerator;
import util.LoggerUtil;
import java.time.LocalDateTime;

public class Transaction {
    
    private final String id;
    private final String fromAddress;
    private final String toAddress;
    private final double amount;
    private final CryptoType type;
    private final FeeLevel feeLevel;
    private double fees;
    private TransactionStatus status;
    private final LocalDateTime createdAt;
    
    public Transaction(String fromAddress, String toAddress, double amount, 
                      CryptoType type, FeeLevel feeLevel) {
        this.id = UUIDGenerator.generate();
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.amount = amount;
        this.type = type;
        this.feeLevel = feeLevel;
        this.fees = 0.0; // Sera calculé par le service
        this.status = TransactionStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        
        LoggerUtil.logTransaction(id, "CREATED", 
            String.format("From: %s, To: %s, Amount: %.8f %s", 
                fromAddress, toAddress, amount, type.getSymbol()));
    }
    
    // Getters
    public String getId() { return id; }
    public String getFromAddress() { return fromAddress; }
    public String getToAddress() { return toAddress; }
    public double getAmount() { return amount; }
    public CryptoType getType() { return type; }
    public FeeLevel getFeeLevel() { return feeLevel; }
    public double getFees() { return fees; }
    public TransactionStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    public void setFees(double fees) {
        this.fees = fees;
        LoggerUtil.logFeeCalculation(id, fees, feeLevel.name());
    }
    
    public void setStatus(TransactionStatus newStatus) {
        if (status.canTransitionTo(newStatus)) {
            TransactionStatus oldStatus = this.status;
            this.status = newStatus;
            LoggerUtil.logTransaction(id, "STATUS_CHANGE", 
                String.format("%s -> %s", oldStatus, newStatus));
        } else {
            LoggerUtil.logWarning(String.format("Transition invalide pour transaction %s: %s -> %s", 
                id, status, newStatus));
        }
    }
    

    public double getTotalAmount() {
        return amount + fees;
    }

    public String getShortId() {
        return id.substring(0, 8) + "...";
    }
    
    @Override
    public String toString() {
        return String.format("Transaction[%s] %s -> %s: %.8f %s (fees: %.8f)", 
            getShortId(), fromAddress, toAddress, amount, type.getSymbol(), fees);
    }
}