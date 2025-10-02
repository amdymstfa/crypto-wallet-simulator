package model;

import enums.CryptoType;
import java.security.SecureRandom;
import java.time.LocalDateTime;

/**
 * Ethereum wallet implementation
 */
public class EthereumWallet extends Wallet {
    
    private static final SecureRandom random = new SecureRandom();
    
    /**
     * Constructor for Ethereum wallet
     */
    public EthereumWallet() {
        super(CryptoType.ETHEREUM);
    }

    public EthereumWallet(String id, String address, double balance, LocalDateTime createdAt) {
        super(id, address, CryptoType.ETHEREUM, balance, createdAt);
    }
    
    /**
     * Generates an Ethereum address (format "0x" + 40 hex characters)
     * @return A valid Ethereum address
     */
    @Override
    protected String generateAddress() {
        // Simplified Ethereum address generation
        StringBuilder address = new StringBuilder("0x");
        String hexChars = "0123456789abcdef";
        
        for (int i = 0; i < 40; i++) {
            address.append(hexChars.charAt(random.nextInt(hexChars.length())));
        }
        
        return address.toString();
    }
}