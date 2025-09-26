package enums ;

public enum CryptoType {
    BITCOIN("BTC"),
    ETHEREUM("ETH") ;

    // define symblo for hundling this unit above
    private final String symbol ;

    public CryptoType(String symbol){
        this.symbol = symbol ;
    }

    // return the unit 
    public String getSymbol(){
        return symbol ;
    }
}