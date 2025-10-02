package model;

import util.LoggerUtil;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Mempool {
    
    private final List<Transaction> transactions;
    private static final int BLOCK_TIME_MINUTES = 10;
    
    public Mempool() {
        this.transactions = new ArrayList<>();
        LoggerUtil.logInfo("Mempool initialisé");
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        sortTransactionsByFees();
        LoggerUtil.logTransaction(transaction.getId(), "ADDED_TO_MEMPOOL", 
            String.format("Position: %d", getPosition(transaction)));
    }

    public boolean removeTransaction(Transaction transaction) {
        boolean removed = transactions.remove(transaction);
        if (removed) {
            LoggerUtil.logTransaction(transaction.getId(), "REMOVED_FROM_MEMPOOL", "Confirmée");
        }
        return removed;
    }

    private void sortTransactionsByFees() {
        transactions.sort((t1, t2) -> Double.compare(t2.getFees(), t1.getFees()));
    }

    public int getPosition(Transaction transaction) {
        sortTransactionsByFees();
        return transactions.indexOf(transaction) + 1;
    }

    public Duration estimateWaitingTime(Transaction transaction) {
        int position = getPosition(transaction);
        long minutes = position * BLOCK_TIME_MINUTES;
        return Duration.ofMinutes(minutes);
    }

    public List<Transaction> getTransactionsByFees() {
        sortTransactionsByFees();
        return new ArrayList<>(transactions);
    }

    public void generateRandomTransactions(int count) {
        LoggerUtil.logInfo(String.format("Génération de %d transactions aléatoires", count));
    }

    public int size() {
        return transactions.size();
    }

    public boolean isEmpty() {
        return transactions.isEmpty();
    }
}