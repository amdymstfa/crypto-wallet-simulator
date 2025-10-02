package enums;

public enum FeeLevel {
    ECONOMIQUE(0.5, "Lent - 30-60 min"),
    STANDARD(1.0, "Moyen - 10-20 min"), 
    RAPIDE(2.0, "Rapide - 1-5 min");
    
    private final double multiplier;
    private final String description;
    
    FeeLevel(double multiplier, String description) {
        this.multiplier = multiplier;
        this.description = description;
    }
    
  
    public double calculateFee(double baseFee) {
        return baseFee * multiplier;
    }
    
    public double getMultiplier() {
        return multiplier;
    }
    
    public String getDescription() {
        return description;
    }
}