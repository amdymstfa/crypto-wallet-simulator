package util;

import enums.CryptoType;
import exception.InvalidAddressException;

public class AddressValidator {

    public static boolean isValidAddress(String address, CryptoType type) {
        if (address == null || address.trim().isEmpty()) return false;

        switch (type) {
            case BITCOIN:
                return address.startsWith("1") || address.startsWith("3") || address.startsWith("bc1");
            case ETHEREUM:
                return address.startsWith("0x") && address.length() == 42;
            default:
                return false;
        }
    }

    public static void validateAddress(String address, CryptoType type) 
        throws InvalidAddressException {
        if (!isValidAddress(address, type)) {
            throw new InvalidAddressException("Invalid address format for " + type);
        }
    }
}
