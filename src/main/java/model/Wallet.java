package model ;

import enums.CryptoType ;
import util.UUIDGenerator ;
import util.LoggerUtil ;
import java.time.LocalDateTime ;
import java.util.List ;
import java.util.ArrayList ;

public class Wallet {

    private final String walletId ;
    private final String adress ;
    private CryptoType type ;
    private final List<Transaction> transactions ;
    private final LocalDateTime createdAt ;
    private double balance ;

     protected Wallet(CryptoType type) {
        this.id = UUIDGenerator.generate();
        this.type = type;
        this.address = generateAddress();
        this.balance = 0.0;
        this.createdAt = LocalDateTime.now();
        this.transactions = new ArrayList<>();

        LoggerUtil.logInfo(String.format("Wallet created: %s [%s] - Adress: %s", 
            id.substring(0, 8), type, address));
    }


    // getters
    public String getId() { return id; }
    public String getAddress() { return address; }
    public CryptoType getType() { return type; }
    public double getBalance() { return balance; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<Transaction> getTransactions() { return new ArrayList<>(transactions); }

    // add a new transactions 
    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
        LoggerUtil.logTransaction(transaction.getId(), "ADD TRANSACTION", String.format(
            "Wallet id %s", id.substring(0,8)
        ));
    }

    // update transactions
    public void updateTransaction(Transaction newBalance){
        String oldBalance = this.balance ;
        this.balance = newBalance ;
        LoggerUtil.logInfo(String.format("Update %s: %.8f -> %.8f %s", 
            id.substring(0, 8), oldBalance, newBalance, type.getSymbol()));
    }

    protected abstract String generateAddress();

    // check if wallet can make transaction
    public boolean canAfford(double amount, double fees){
        return balance >= (amount+fees);
    }

    @Override 
    public String toString() {
        return String.format("Wallet[%s] %s - Balance: %.8f %s", 
            id.substring(0, 8), address, balance, type.getSymbol());
    }
}