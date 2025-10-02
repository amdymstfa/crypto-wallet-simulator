package exception;

/**
 * Exception levée quand un montant n'est pas valide
 * (négatif, zéro, ou format incorrect)
 */
public class InvalidAmountException extends Exception {
    
    private final double invalidAmount;
    
    public InvalidAmountException(String message) {
        super(message);
        this.invalidAmount = 0.0;
    }
    
    public InvalidAmountException(double invalidAmount) {
        super("Montant invalide : " + invalidAmount + ". Le montant doit être positif.");
        this.invalidAmount = invalidAmount;
    }
    
    public InvalidAmountException(String message, double invalidAmount) {
        super(message);
        this.invalidAmount = invalidAmount;
    }
    
    public double getInvalidAmount() {
        return invalidAmount;
    }
}