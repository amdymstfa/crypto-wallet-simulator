package model;

import enums.CryptoType;
import java.security.SecureRandom;
import java.time.LocalDateTime;

/**
 * Bitcoin wallet implementation
 */
public class BitcoinWallet extends Wallet {
    
    private static final SecureRandom random = new SecureRandom();
    
    /**
     * Constructor for Bitcoin wallet
     */
    public BitcoinWallet() {
        super(CryptoType.BITCOIN);
    }

    public BitcoinWallet(String id, String address, double balance, LocalDateTime createdAt) {
        super(id, address, CryptoType.BITCOIN, balance, createdAt);
    }
    
    /**
     * Generates a Bitcoin address (simplified format starting with "1")
     * @return A valid Bitcoin address
     */
    @Override
    protected String generateAddress() {
        // Simplified Bitcoin address generation (format "1")
        StringBuilder address = new StringBuilder("1");
        String chars = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
        
        for (int i = 0; i < 33; i++) {
            address.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return address.toString();
    }
}