package util ;

import enums.CryptoType ;
import exception.InvalidAddressException ;
import exception.PersonalizedException ;

public class AddressValidator {
    
    public String validAdress(String adress, CryptoType type){
        throws InvalidAddressException {

            if (address == null || adress.trim().isEmpty()){
                throw new PersonalizedException("Address cannot be empty");
            }

            if(!type.isValidAddress(address)){
                throw new InvalidAddressException(adress, type.name());
            }
        }
    }
}