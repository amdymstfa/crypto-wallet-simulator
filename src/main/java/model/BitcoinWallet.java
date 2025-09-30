package model;

import enums.CryptoType;
import java.security.SecureRandom;

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