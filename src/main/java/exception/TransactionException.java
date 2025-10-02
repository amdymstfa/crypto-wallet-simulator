package exception ;

public class TransactionException extends Exception {
    
    private final String transactionId;
    
    public TransactionException(String message) {
        super(message);
        this.transactionId = null;  
    }
    
    public TransactionException(String message, String transactionId) {
        super(message);
        this.transactionId = transactionId;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
}