package service;

import model.*;
import enums.CryptoType;
import exception.WalletNotFoundException;
import util.LoggerUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Service to manage wallets
 */
public class WalletService {
    
    private final Map<String, Wallet> wallets;
    
    public WalletService() {
        this.wallets = new HashMap<>();
        LoggerUtil.logInfo("WalletService initialized");
    }
    
    /**
     * Create a new wallet for a given crypto type
     * @param type Crypto type
     * @return Created wallet
     */
    public Wallet createWallet(CryptoType type) {
        Wallet wallet;
        
        switch (type) {
            case BITCOIN:
                wallet = new BitcoinWallet();
                break;
            case ETHEREUM:
                wallet = new EthereumWallet();
                break;
            default:
                throw new IllegalArgumentException("Unsupported crypto type: " + type);
        }
        
        wallets.put(wallet.getId(), wallet);
        LoggerUtil.logInfo(String.format("Wallet created: %s [%s]", 
            wallet.getId().substring(0, 8), type));
        
        return wallet;
    }
    
    /**
     * Find wallet by ID
     * @param walletId Wallet ID
     * @return Found wallet
     * @throws WalletNotFoundException if wallet does not exist
     */
    public Wallet findById(String walletId) throws WalletNotFoundException {
        Wallet wallet = wallets.get(walletId);
        if (wallet == null) {
            throw new WalletNotFoundException(walletId);
        }
        return wallet;
    }
    
    /**
     * Find wallet by address
     * @param address Wallet address
     * @return Optional containing wallet or empty
     */
    public Optional<Wallet> findByAddress(String address) {
        return wallets.values().stream()
                .filter(wallet -> wallet.getAddress().equals(address))
                .findFirst();
    }
    
    /**
     * Get all wallets
     * @return List of all wallets
     */
    public List<Wallet> getAllWallets() {
        return new ArrayList<>(wallets.values());
    }
    
    /**
     * Get wallets of a specific type
     * @param type Crypto type
     * @return List of wallets of that type
     */
    public List<Wallet> getWalletsByType(CryptoType type) {
        return wallets.values().stream()
                .filter(wallet -> wallet.getType() == type)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Update wallet balance
     * @param walletId Wallet ID
     * @param newBalance New balance
     * @throws WalletNotFoundException if wallet does not exist
     */
    public void updateBalance(String walletId, double newBalance) throws WalletNotFoundException {
        Wallet wallet = findById(walletId);
        wallet.updateBalance(newBalance);
    }
    
    /**
     * Delete a wallet (only if balance is 0)
     * @param walletId Wallet ID
     * @return true if deleted successfully
     */
    public boolean deleteWallet(String walletId) {
        Wallet wallet = wallets.get(walletId);
        if (wallet != null && wallet.getBalance() == 0.0) {
            wallets.remove(walletId);
            LoggerUtil.logInfo(String.format("Wallet deleted: %s", walletId.substring(0, 8)));
            return true;
        }
        return false;
    }
    
    /**
     * Wallet statistics
     */
    public String getWalletStats() {
        long bitcoinCount = wallets.values().stream()
                .filter(w -> w.getType() == CryptoType.BITCOIN)
                .count();
        long ethereumCount = wallets.values().stream()
                .filter(w -> w.getType() == CryptoType.ETHEREUM)
                .count();
        
        return String.format("Wallets: %d total (%d Bitcoin, %d Ethereum)", 
            wallets.size(), bitcoinCount, ethereumCount);
    }
}
