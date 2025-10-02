package enums;

public enum CryptoType {
    BITCOIN("BTC"),
    ETHEREUM("ETH");
    
    private final String symbol;
    
    CryptoType(String symbol) {
        this.symbol = symbol;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
   
    public boolean isValidAddress(String address) {
        if (address == null || address.trim().isEmpty()) return false;
        
        switch (this) {
            case BITCOIN:
                return address.startsWith("1") || address.startsWith("3") || address.startsWith("bc1");
            case ETHEREUM:
                return address.startsWith("0x") && address.length() == 42;
            default:
                return false;
        }
    }
}