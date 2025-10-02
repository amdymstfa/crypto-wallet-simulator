package enums;

public enum TransactionStatus {
    PENDING("En attente", "Transaction dans le mempool"),
    CONFIRMED("Confirmée", "Transaction validée par les mineurs"),
    REJECTED("Rejetée", "Transaction refusée");
    
    private final String displayName;
    private final String description;
    
    TransactionStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    
    public boolean canTransitionTo(TransactionStatus newStatus) {
        switch (this) {
            case PENDING:
                return newStatus == CONFIRMED || newStatus == REJECTED;
            case CONFIRMED:
            case REJECTED:
                return false; 
            default:
                return false;
        }
    }
}