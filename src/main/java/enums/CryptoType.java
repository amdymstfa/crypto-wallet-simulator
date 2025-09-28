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

    // define adress validation
   public boolean isValidAddress(String address) {
        if (address == null || address.trim().isEmpty()) return false;

        switch(this) {
            case BITCOIN:
                return address.startsWith("1") || address.startsWith("3") || address.startsWith("bc1");
            case ETHEREUM:
                return address.startsWith("0x") && address.length() == 42;
            default:
                return false;
        }
    }

}