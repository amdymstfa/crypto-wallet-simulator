package model ;

import enums.CryptoType;
import enums.FeeLevel;
import enums.TransactionStatus;
import util.UUIDGenerator;
import util.LoggerUtil;
import java.time.LocalDateTime;

public class Transaction {

    private final String idTransaction ;
    private final String fromAdress ;
    private final String toAdress ;
    private final double amount ;
    private final FeeLevel feeLevel ;
    private final CryptoType type ;
    private double fees ;
    private TransactionStatus status ;
    private final LocalDateTime createdAt ;

    public Transaction(
        String fromAdress ,
        String toAdress ,
        double amount ,
        FeeLevel feeLevel ,
        CryptoType type ,
    ){
        this.idTransaction = UUIDGenerator.generate();
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.amount = amount;
        this.type = type;
        this.feeLevel = feeLevel;
        this.fees = 0.0;  // set a default value
        this.status = TransactionStatus.PENDING;
        this.createdAt = LocalDateTime.now();

        // add loggin 
        LoggerUtil.logTransaction(idTransaction, "CREATED",
            String.format("From %s to %s, Amount %.8f %s", fromAdress, toAdress, amount, type.getSymbol())
        );
    }

    // Getters
    public String getId(){return idTransaction;}
    public String getFromAddress() { return fromAddress; }
    public String getToAddress() { return toAddress; }
    public double getAmount() { return amount; }
    public CryptoType getType() { return type; }
    public FeeLevel getFeeLevel() { return feeLevel; }
    public double getFees() { return fees; }
    public TransactionStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }


    // set the fees values bay calcul
    public void setFees(double fees){
        this.fees = fees ;
        // add logging about fees
        LoggerUtil.logFeeCalculation(idTransaction, fees, feeLevel.name());
    }

    // update validation
    public void setStatus(TransactionStatus newStatus){
        if(status.canTransiteTo(newStatus)){
            TransactionStatus oldSatatus = this.status ;
            this.status = newStatus;
            // add logging for updating status
            LoggerUtil.logTransaction(transactionId, "Status Changed", String.format("%s -> %s", oldStatus, newStatus));
        }else {
            // add logging in case of failure
            LoggerUtil.logWarning(String.format("Invalid transaction %s : %s -> %s", idTransaction, oldSatatus, newStatus ));
        }
    }


    // total amount 
    getTotalAmount(double amount){
        return amount + fees ;
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