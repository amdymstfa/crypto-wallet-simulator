package model ;

import enums.CryptoType ;
import java.security.SecureRandom ;

public class BitcoinWallet extends Wallet {

    private static final SecureRandom randon = new SecureRandom();

    public BitcoinWallet(){
        super(CryptoType.BITCOIN);
    }

    @Override 
    public String generateAddress(){
        // bitcoin must start with 1 3 bc1
        String[] prefixes = {"1", "3", "bc1"};
        // choice methode 
        String prefix = prefixes[random.nextInt(prefixes.length)];

        String chars = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";

        StringBuilder  address = new StringBuilder(prefix);

        int length = prefix.equals("bc1") ? 39 : 33;

        for (i = 0 ; i < length ; i++){
            address.append(chars.charAt(random.nextInt(chars.length())));
        }

        return address.toString();
    }
}