package exception;

/**
 * Exception thrown when a requested wallet does not exist
 * or is not accessible.
 */
public class WalletNotFoundException extends Exception {

    private final String walletId;

    /**
     * Constructor with wallet ID, builds a default message.
     */
    public WalletNotFoundException(String walletId) {
        super("Wallet not found with ID: " + walletId);
        this.walletId = walletId;
    }

    /**
     * Constructor with custom message.
     */
    public WalletNotFoundException(String message, boolean isCustomMessage) {
        super(message);
        this.walletId = null;
    }

    /**
     * Constructor with custom message and wallet ID.
     */
    public WalletNotFoundException(String message, String walletId) {
        super(message);
        this.walletId = walletId;
    }

    /**
     * Getter for wallet ID.
     */
    public String getWalletId() {
        return walletId;
    }
}
