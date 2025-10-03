package app;

import model.*;
import enums.*;
import exception.*;
import util.ConsolePrinter;
import util.LoggerUtil;
import java.sql.SQLException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles transaction-related operations
 */
public class TransactionHandler {
    
    private final ApplicationContext context;
    private final InputReader inputReader;
    private Transaction lastTransaction;
    
    public TransactionHandler(ApplicationContext context, InputReader inputReader) {
        this.context = context;
        this.inputReader = inputReader;
    }

    public void createTransaction() {
        ConsolePrinter.printTitle("2️⃣  Create Transaction");

        try {
            System.out.println("Choose cryptocurrency:");
            System.out.println("1. Bitcoin");
            System.out.println("2. Ethereum");
            // int cryptoChoice = inputReader.readInt("\n👉 Your choice: ");
            int cryptoChoice = inputReader.readIntInRange("\n👉 Your choice: ", 1, 2);
            CryptoType type = (cryptoChoice == 1) ? CryptoType.BITCOIN : CryptoType.ETHEREUM;
            
            // Get addresses
            String fromAddress = inputReader.readString("\n📬 From address: ");
            String toAddress = inputReader.readString("📬 To address: ");
            
            // Get amount
            double amount = inputReader.readDouble("💵 Amount: ");
            if (amount <= 0) {
                ConsolePrinter.printError("Amount must be positive");
                return;
            }
            
            // Choose fee level
            System.out.println("\n📊 Fee level:");
            System.out.println("1. ECONOMIQUE - " + FeeLevel.ECONOMIQUE.getDescription());
            System.out.println("2. STANDARD - " + FeeLevel.STANDARD.getDescription());
            System.out.println("3. RAPIDE - " + FeeLevel.RAPIDE.getDescription());
            
            // int feeLevelChoice = inputReader.readInt("\n👉 Your choice: ");
            int feeLevelChoice = inputReader.readIntInRange("\n👉 Your choice: ", 1, 3);
            FeeLevel feeLevel = FeeLevel.values()[feeLevelChoice - 1];
            
            // Create transaction
            ConsolePrinter.printLoading("Creating transaction");
            Transaction tx = context.getTransactionService().createTransaction(
                fromAddress, toAddress, amount, type, feeLevel
            );
            
            lastTransaction = tx;
            
            // Add to mempool
            context.getMempoolService().addTransaction(tx);
            
            // Save to DB
            try {
                context.getTransactionRepository().save(tx);
                ConsolePrinter.printSuccess("Transaction saved to database");
            } catch (SQLException e) {
                ConsolePrinter.printWarning("Transaction created but not saved to DB");
            }
            
            // Display details
            ConsolePrinter.printSuccess("Transaction created successfully!");
            int position = context.getMempoolService().getPosition(tx);
            Duration waitTime = context.getMempoolService().estimateWaitingTime(tx);
            ConsolePrinter.printTransactionDetails(tx, position, waitTime);
            
        } catch (InvalidAddressException e) {
            ConsolePrinter.printError("Invalid address: " + e.getMessage());
        } catch (InvalidAmountException e) {
            ConsolePrinter.printError("Invalid amount: " + e.getMessage());
        } catch (Exception e) {
            ConsolePrinter.printError("Error creating transaction: " + e.getMessage());
            LoggerUtil.logError("Transaction creation error", e);
        }
    }

    public void compareFees() {
        ConsolePrinter.printTitle("4️⃣  Compare Fee Levels");
        
        try {
            // Choose crypto type
            System.out.println("Cryptocurrency type:");
            System.out.println("1. Bitcoin (BTC)");
            System.out.println("2. Ethereum (ETH)");
            
            int typeChoice = inputReader.readInt("\n👉 Your choice: ");
            CryptoType type = (typeChoice == 1) ? CryptoType.BITCOIN : CryptoType.ETHEREUM;
            
            // Get amount
            double amount = inputReader.readDouble("\n💵 Transaction amount: ");
            if (amount <= 0) {
                ConsolePrinter.printError("Amount must be positive");
                return;
            }
            
            // Calculate fees
            Map<FeeLevel, Double> feesMap = context.getTransactionService().compareFees(amount, type);
            
            // Simulate positions
            Map<FeeLevel, Integer> positionsMap = new HashMap<>();
            int basePosition = context.getMempoolService().getSize() / 2;
            positionsMap.put(FeeLevel.ECONOMIQUE, basePosition + 5);
            positionsMap.put(FeeLevel.STANDARD, basePosition);
            positionsMap.put(FeeLevel.RAPIDE, Math.max(1, basePosition - 5));
            
            // Display comparison
            ConsolePrinter.printFeeComparison(amount, type.getSymbol(), feesMap, positionsMap);
            
        } catch (Exception e) {
            ConsolePrinter.printError("Error: " + e.getMessage());
            LoggerUtil.logError("Fee comparison error", e);
        }
    }
    
    public Transaction getLastTransaction() {
        return lastTransaction;
    }
}