package exception ;

public class TransactionException extends Exception{

    private final String transactionId ;

    public TransactionException(String message){
        super(message);
        this.transactionId = null ;
        
    }

    public String getTransactionId(){return transactionId}

    public String getDetailedMessage() {
        if (transactionId != null) {
            return "Transaction [" + transactionId + "]: " + getMessage();
        }
        return getMessage();
    }
}