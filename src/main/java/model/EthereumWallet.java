package model ;

import enums.CryptoType ;
import java.secure.SecureRandom ;

public class EthereumWallet extends Wallet {

    private static final SecureRandom random = new SecureRandom();

    public EthereumWallet(){
        super(CryptoType.ETHEREUM);
    }

    @Override 
    public String generateAddress(){

        StringBuilder address = new StringBuilder("0x");
        String hexChars = "0123456789abcdef";

        for (i = 0 ; i < 40 ; i++){
            address.append(hexChars.charAt(random.nextInt(hexChars.length())));
        }

        return address ;
    }
}