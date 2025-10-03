package app;

import model.*;
import enums.CryptoType;
import util.ConsolePrinter;
import util.LoggerUtil;
import java.sql.SQLException;
import java.util.List;

/**
 * Handles wallet-related operations
 */
public class WalletHandler {
    
    private final ApplicationContext context;
    private final InputReader inputReader;
    private Wallet currentWallet;
    
    public WalletHandler(ApplicationContext context, InputReader inputReader) {
        this.context = context;
        this.inputReader = inputReader;
    }
    
    /**
     * Creates a new wallet
     */
    public void createWallet() {
        ConsolePrinter.printTitle("1️⃣  Create Crypto Wallet");
        
        try {
            // Choose crypto type
            System.out.println("Choose cryptocurrency type:");
            System.out.println("1. Bitcoin (BTC)");
            System.out.println("2. Ethereum (ETH)");
            
            // int typeChoice = inputReader.readInt("\n👉 Your choice: ");
            int typeChoice = inputReader.readIntInRange("\n👉 Your choice: ", 1, 2);
            CryptoType type = (typeChoice == 1) ? CryptoType.BITCOIN : CryptoType.ETHEREUM;
            
            // Create wallet
            ConsolePrinter.printLoading("Creating " + type + " wallet");
            Wallet wallet = context.getWalletService().createWallet(type);
            currentWallet = wallet;
            
            // Save to database
            try {
                context.getWalletRepository().save(wallet);
                ConsolePrinter.printSuccess("Wallet saved to database");
            } catch (SQLException e) {
                ConsolePrinter.printWarning("Wallet created but not saved to DB (memory only)");
            }
            
            // Display details
            ConsolePrinter.printSuccess("Wallet created successfully!");
            System.out.println("\n📋 Wallet Details:");
            System.out.println("Type: " + wallet.getType());
            System.out.println("Address: " + wallet.getAddress());
            System.out.println("Balance: " + wallet.getBalance() + " " + wallet.getType().getSymbol());
            System.out.println("Created: " + wallet.getCreatedAt());
            
            // Optional: Initialize balance
            if (inputReader.readConfirmation("\n💰 Initialize balance?")) {
                double balance = inputReader.readDouble("Amount: ");
                if (balance > 0) {
                    wallet.updateBalance(balance);
                    try {
                        context.getWalletRepository().updateBalance(wallet.getId(), balance);
                        ConsolePrinter.printSuccess("Balance updated: " + balance + " " + type.getSymbol());
                    } catch (SQLException e) {
                        ConsolePrinter.printWarning("Balance updated in memory only");
                    }
                }
            }
            
        } catch (Exception e) {
            ConsolePrinter.printError("Error creating wallet: " + e.getMessage());
            LoggerUtil.logError("Wallet creation error", e);
        }
    }
    
    /**
     * Views all wallets
     */
    public void viewMyWallets() {
        ConsolePrinter.printTitle("6️⃣  My Wallets");
        
        try {
            List<Wallet> wallets = context.getWalletService().getAllWallets();
            
            if (wallets.isEmpty()) {
                ConsolePrinter.printInfo("No wallets created. Use option 1 to create one.");
                return;
            }
            
            System.out.println("Number of wallets: " + wallets.size() + "\n");
            
            for (int i = 0; i < wallets.size(); i++) {
                Wallet w = wallets.get(i);
                System.out.println((i + 1) + ". " + w.getType() + " Wallet");
                System.out.println("   Address: " + w.getAddress());
                System.out.println("   Balance: " + w.getBalance() + " " + w.getType().getSymbol());
                System.out.println("   Transactions: " + w.getTransactions().size());
                
                if (currentWallet != null && w.getId().equals(currentWallet.getId())) {
                    System.out.println("   ✅ (Current wallet)");
                }
                System.out.println();
            }
            
        } catch (Exception e) {
            ConsolePrinter.printError("Error: " + e.getMessage());
            LoggerUtil.logError("View wallets error", e);
        }
    }
    
    public Wallet getCurrentWallet() {
        return currentWallet;
    }
}
