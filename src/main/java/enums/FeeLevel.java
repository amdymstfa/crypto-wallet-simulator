package enums ;

public enum FeeLevel {
    ECONOMIC(0.5 , "around 25 mn"),
    STANDART(1, "around 10 mn"),
    FAST(2, "around 2 mn") ;

    private final double multiplier ;
    private final String description ;

    public FeeLevel(int multiplier, String description){
        this.multiplier = multiplier ;
        this.description = description ;
    }

    public double getMultiplier(){return multiplier ;}
    public String getDescription(){return description ;}


    // define logic of fees
    public double calculFee(double baseFee){
        return baseFee*multiplier;
    }
}