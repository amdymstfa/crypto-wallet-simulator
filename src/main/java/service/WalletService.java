package service ;

import model.*;
import enums.CryptoType;
import exception.WalletNotFoundException;
import util.LoggerUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional; 

public class WalletService {

    public final Map<String, Wallet> wallets ;

    public WalletService(){
        this.wallets = new HashMap<>();
        LoggerUtil.logInfo("Initialization of wallet service");
    }

    public Wallet create(CryptoType type) {
    Wallet wallet;
    switch (type) {
        case BITCOIN:
            wallet = new BitcoinWallet();
            break;
        case ETHEREUM:
            wallet = new EthereumWallet();
            break;
        default:
            throw new IllegalArgumentException(type + " not supported");
    }
    wallets.put(wallet.getId(), wallet);
    LoggerUtil.logInfo(String.format("Wallet créé: %s [%s]",
        wallet.getId().substring(0, 8), type));
    return wallet;
}



    public Wallet findById(String walletId) throws WalletNotFoundException {
        Wallet wallet = wallets.get(walletId);
        if(wallet == null){
            throw new WalletNotFoundException(walletId);
        }

        return wallet;
    }

    public Optional<Wallet> findByAddress(String adress){
        return wallets.values().stream()
                                .filter(wallet -> wallet.getAddress().equals(adress))
                                .findFirst();
    }

    public List<Wallet> getAllWallets(){
        return new ArrayList<>(wallets.values());
    }

    public List<Wallet> getWalletByType(CryptoType type){
        return wallets.values().stream()
                                .filter(wallet -> wallet.getType() == type)
                                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public void updateBalance(String walletId, double newBalance) throws WalletNotFoundException{
        Wallet wallet = findById(walletId);
        wallet.updateBalance(newBalance);
    }

    public boolean deleteWallet(String walletId){
       Wallet wallet = findById(walletId);
        if (wallet != null && wallet.getBalance() == 0.0) {
            wallets.remove(walletId);
            LoggerUtil.logInfo(String.format("Wallet supprimé: %s", walletId.substring(0, 8)));
            return true;
        }
        return false;

    }

    // public String getWalletStats() {
    //     long bitcoinCount = wallets.values().stream()
    //             .filter(w -> w.getType() == CryptoType.BITCOIN)
    //             .count();
    //     long ethereumCount = wallets.values().stream()
    //             .filter(w -> w.getType() == CryptoType.ETHEREUM)
    //             .count();
        
    //     return String.format("Wallets: %d total (%d Bitcoin, %d Ethereum)", 
    //         wallets.size(), bitcoinCount, ethereumCount);
    // }

}