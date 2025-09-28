package exception ;

public class WalletNotFoundException extends Exception {

    private final String walletId ;

    public WalletNotFoundException(String message){
        super(message);
        this.walletId = null ;
    }

    public WalletNotFoundException(String walletId){
        super("Wallet not found : " + walletId);
        this.walletId = walletId ;
    }

    public String getWalletId(){return walletId ;}
}