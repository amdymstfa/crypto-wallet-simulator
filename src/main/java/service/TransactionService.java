package service;

import model.*;
import enums.*;
import exception.*;
import util.LoggerUtil;
import util.AddressValidator;
import java.util.*;

/**
 * Service de gestion des transactions
 */
public class TransactionService {
    
    private final Map<CryptoType, FeeCalculator> feeCalculators;
    private final WalletService walletService;
    private final List<Transaction> transactions;
    
    public TransactionService(WalletService walletService) {
        this.walletService = walletService;
        this.transactions = new ArrayList<>();
        this.feeCalculators = new HashMap<>();
        
        // Initialisation des calculateurs de frais
        feeCalculators.put(CryptoType.BITCOIN, new BitcoinFeeCalculator());
        feeCalculators.put(CryptoType.ETHEREUM, new EthereumFeeCalculator());
        
        LoggerUtil.logInfo("TransactionService initialisé avec calculateurs de frais");
    }
    
    /**
     * Crée une nouvelle transaction avec toutes les validations
     */
    public Transaction createTransaction(String fromAddress, String toAddress, 
                                      double amount, CryptoType type, FeeLevel feeLevel) 
            throws InvalidAddressException, InvalidAmountException, TransactionException {
        
        // Validations
        validateTransactionInputs(fromAddress, toAddress, amount, type);
        
        // Création de la transaction
        Transaction transaction = new Transaction(fromAddress, toAddress, amount, type, feeLevel);
        
        // Calcul des frais
        FeeCalculator calculator = feeCalculators.get(type);
        double fees = calculator.calculateFees(transaction);
        transaction.setFees(fees);
        
        // Stockage
        transactions.add(transaction);
        
        LoggerUtil.logTransaction(transaction.getId(), "CREATED", 
            String.format("Fees: %.8f %s", fees, type.getSymbol()));
        
        return transaction;
    }
    
    /**
     * Valide les données d'entrée pour une transaction
     */
    private void validateTransactionInputs(String fromAddress, String toAddress, 
                                         double amount, CryptoType type) 
            throws InvalidAddressException, InvalidAmountException {
        
        // Validation des adresses
        AddressValidator.validateAddress(fromAddress, type);
        AddressValidator.validateAddress(toAddress, type);
        
        // Validation du montant
        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }
        
        // Vérification que les adresses sont différentes
        if (fromAddress.equals(toAddress)) {
            throw new InvalidAddressException("Les adresses source et destination doivent être différentes");
        }
    }
    
    /**
     * Compare les frais pour les 3 niveaux de priorité
     */
    public Map<FeeLevel, Double> compareFees(double amount, CryptoType type) {
        FeeCalculator calculator = feeCalculators.get(type);
        Map<FeeLevel, Double> comparison = new HashMap<>();
        
        for (FeeLevel level : FeeLevel.values()) {
            double fees = calculator.calculateFees(amount, level);
            comparison.put(level, fees);
        }
        
        LoggerUtil.logInfo(String.format("Comparaison frais %s pour montant %.8f", type, amount));
        return comparison;
    }
    
    /**
     * Trouve une transaction par son ID
     */
    public Optional<Transaction> findById(String transactionId) {
        return transactions.stream()
                .filter(tx -> tx.getId().equals(transactionId))
                .findFirst();
    }
    
    /**
     * Liste toutes les transactions d'un type
     */
    public List<Transaction> getTransactionsByType(CryptoType type) {
        return transactions.stream()
                .filter(tx -> tx.getType() == type)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Liste toutes les transactions d'un statut
     */
    public List<Transaction> getTransactionsByStatus(TransactionStatus status) {
        return transactions.stream()
                .filter(tx -> tx.getStatus() == status)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Met à jour le statut d'une transaction
     */
    public void updateTransactionStatus(String transactionId, TransactionStatus newStatus) 
            throws TransactionException {
        Optional<Transaction> optTx = findById(transactionId);
        if (optTx.isPresent()) {
            optTx.get().setStatus(newStatus);
        } else {
            throw new TransactionException("Transaction introuvable", transactionId);
        }
    }
    
    /**
     * Statistiques des transactions
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
