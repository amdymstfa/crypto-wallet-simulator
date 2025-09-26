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
    public String isValidAdress(String adress){
        if (adress == null || adress.trim().isEmpty()){
            return false ;
        }

        switch(this){
            case BITCOIN :
                return adress.startWith("1") || adress.startWith("3") || adress.startWith("bc1");

            case ETHEREUM :
                return adress.startWith("0x") && address.length() == 42;
        }
    }
}