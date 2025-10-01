package service ;

import model.*;
import enums.*;
import exception.*;
import util.LoggerUtil;
import util.AddressValidator;
import java.util.*;

public class TransactionService {

    private final Map<CryptoType, FeeCalculator> feeCalculators;
    private final WalletService walletService ;
    private final List<Transaction> transactions;

    public TransactionService(WalletService walletService){
        this.feeCalculators = new HashMap<>();
        this.walletService = walletService;
        this.transactions = new ArrayList<>();

        feeCalculators.put(CryptoType.BITCOIN, new BitcoinFeeCalculator());
        feeCalculators,put(CryptoType.ETHEREUM, new EthereumFeeCalculator());

        LoggerUtil.logInfo("TransactionService initialized with fee calculators");
    }

    public Transaction createTransaction(
        String fromAddress, String toAddress, double amount, CryptoType type, FeeLevel feeLevel
    )
        throws InvalidAddressException, InvalidAddressException, TransactionException
    {
        // transaction validation
        validateTransactionInputs(fromAddress, toAdress, amount, type, feeLevel);
        // create a new transaction
        Transaction transaction = new Transaction(fromAddress, toAdress, amount, type, feeLevel);
        // fees calulations
        FeeCalculator calculator = feeCalculators.get(type);
        double fees = calculator.calculateFees(transaction);
        transaction.setFees(fees);
        transactions.add(transaction);

        LoggerUtil.logTransaction(transaction.getId(), "CREATED", 
            String.format("Fees: %.8f %s", fees, type.getSymbol()));

        return transaction;

    }

    public void validateTransactionInputs(String fromAddress, String toAdress, double amount, CryptoType type)
        throws InvalidAddressException, InvalidAmountException
    {
        AddressValidator.validateAdress(fromAddress, type);
        AddressValidator.validateAdress(toAddress, type);

        if (amount <= 0){throw new InvalidAmountException(amount);}
        if(fromAdress.equals(toAddress)){throw new InvalidAddressException("The source and destination addresses must be different");}
    }

    public Map<FeeLevel, Double> compareFees(double amount, CryptoType type){
        FeeCalculator calculator = feeCalculators.get(type);
        Map<FeeLevel, Double> comparison = new HashMap<>();

        for(Level level : FeeLevel.values()){
            double fees = calculator.calculateFees(amount, type);
            comparison.put(level, fees);
        }

        LoggerUtil.logInfo(String.format("Comparaison fees %s for %.8f", type, amount));
        return comparison ;
    }

    // looking fot transaction id 
    public Optional<Transaction> getById(String transactionId){
        return transactions.stream()
                            .filter(tx -> tx.getId().equals(transactionId))
                            .findFirst();
    }

    // get all transaction by type
    public List<Transaction> getTransactionsByType(CryptoType type){
        return transactions.stream()
                            .filter(tx -> tx.getType() == type)
                            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    // get all transaction by status
    public List<Transaction> getTransactionsByStatus(TransactionStatus status){
        return transactions.stream()
                            .filter(tx -> tx.getStatus() == status)
                            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    // update trasactions status
    public void updateTransactionStatus(String transactionId, String newStatus) throws TransactionException{
        Optional<Transaction> tx = findById(transactionId);
        if(tx.isPresent){
            tx.get().setStatus(newStatus)  ;
        }else {
            throw new TransactionException("Not found :" , transactionId);
        }
    }


    //  public String getTransactionStats() {
    //     long pendingCount = transactions.stream()
    //             .filter(tx -> tx.getStatus() == TransactionStatus.PENDING)
    //             .count();
    //     long confirmedCount = transactions.stream()
    //             .filter(tx -> tx.getStatus() == TransactionStatus.CONFIRMED)
    //             .count();
        
    //     return String.format("Transactions: %d total (%d pending, %d confirmed)", 
    //         transactions.size(), pendingCount, confirmedCount);
    // }
}