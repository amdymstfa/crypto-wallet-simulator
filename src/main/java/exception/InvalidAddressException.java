package exception ;

public class InvalidAddressException extends Exception {

    public InvalidAddressException(String message) {
    super(message);
    }

    public InvalidAddressException(String address, String cryptoType){
        super("Invalide adress " + address + " for crypto " + cryptoType);
    }
}