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
 * Service de gestion des wallets
 */
public class WalletService {
    
    private final Map<String, Wallet> wallets;
    
    public WalletService() {
        this.wallets = new HashMap<>();
        LoggerUtil.logInfo("WalletService initialisé");
    }
    
    /**
     * Crée un nouveau wallet selon le type de cryptomonnaie
     * @param type Le type de cryptomonnaie
     * @return Le wallet créé
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
                throw new IllegalArgumentException("Type de crypto non supporté : " + type);
        }
        
        wallets.put(wallet.getId(), wallet);
        LoggerUtil.logInfo(String.format("Wallet créé: %s [%s]", 
            wallet.getId().substring(0, 8), type));
        
        return wallet;
    }
    
    /**
     * Trouve un wallet par son ID
     * @param walletId L'ID du wallet
     * @return Le wallet trouvé
     * @throws WalletNotFoundException Si le wallet n'existe pas
     */
    public Wallet findById(String walletId) throws WalletNotFoundException {
        Wallet wallet = wallets.get(walletId);
        if (wallet == null) {
            throw new WalletNotFoundException(walletId);
        }
        return wallet;
    }
    
    /**
     * Trouve un wallet par son adresse
     * @param address L'adresse du wallet
     * @return Optional contenant le wallet ou empty
     */
    public Optional<Wallet> findByAddress(String address) {
        return wallets.values().stream()
                .filter(wallet -> wallet.getAddress().equals(address))
                .findFirst();
    }
    
    /**
     * Liste tous les wallets
     * @return Liste de tous les wallets
     */
    public List<Wallet> getAllWallets() {
        return new ArrayList<>(wallets.values());
    }
    
    /**
     * Liste les wallets d'un type spécifique
     * @param type Le type de cryptomonnaie
     * @return Liste des wallets de ce type
     */
    public List<Wallet> getWalletsByType(CryptoType type) {
        return wallets.values().stream()
                .filter(wallet -> wallet.getType() == type)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Met à jour le solde d'un wallet
     * @param walletId L'ID du wallet
     * @param newBalance Le nouveau solde
     * @throws WalletNotFoundException Si le wallet n'existe pas
     */
    public void updateBalance(String walletId, double newBalance) throws WalletNotFoundException {
        Wallet wallet = findById(walletId);
        wallet.updateBalance(newBalance);
    }
    
    /**
     * Supprime un wallet (avec vérification de sécurité)
     * @param walletId L'ID du wallet à supprimer
     * @return true si supprimé avec succès
     */
    public boolean deleteWallet(String walletId) {
        Wallet wallet = wallets.get(walletId);
        if (wallet != null && wallet.getBalance() == 0.0) {
            wallets.remove(walletId);
            LoggerUtil.logInfo(String.format("Wallet supprimé: %s", walletId.substring(0, 8)));
            return true;
        }
        return false;
    }
    
    /**
     * Statistiques des wallets
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