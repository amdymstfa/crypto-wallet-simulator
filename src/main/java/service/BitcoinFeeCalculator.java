package service ;

import model.Transaction;
import enums.FeeLevel;
import util.LoggerUtil;

public class BitcoinFeeCalculator implements FeeCalculatorInterface {

    // fake values of bitcoins
    private static final int AVERAGE_TX_SIZE_BYTES = 250; 
    private static final double SATOSHI_PER_BYTE_BASE = 20.0;  
    private static final double SATOSHI_TO_BTC = 0.00000001;  

    @Override
    double calculateFees(double amount, FeeLevel feeLevel){
        double satoshiPerByte = SATOSHI_PER_BYTE_BASE*feeLevel.getMultiplier();
        double totalSatoshi = satoshiPerByte*AVERAGE_TX_SIZE_BYTES;
        double feesInBTC = totalSatoshi*SATOSHI_TO_BTC ;

        // add log for calculations
        LoggerUtil.logFeeCalculation("BITCOIN_CALC", feesInBTC, 
          String.format("Niveau: %s, Sat/byte: %.2f", feeLevel, satoshiPerByte));
    }

    @Override
    double calculateFees(Transaction transaction){
        return calculateFees(transaction.getId(), transaction.getFeeLevel());
    }
    
    
    @Override
    double getBaseFee(){
        return AVERAGE_TX_SIZE_BYTES * SATOSHI_PER_BYTE_BASE * SATOSHI_TO_BTC;
    }
    
    @Override
    public String getCalculatorName() {
        return "BitcoinFeeCalculator";
    }

    public double calculateFeesWithSize(int transactionSizeBytes, FeeLevel feeLevel) {
        double satoshiPerByte = SATOSHI_PER_BYTE_BASE * feeLevel.getMultiplier();
        double totalSatoshi = transactionSizeBytes * satoshiPerByte;
        return totalSatoshi * SATOSHI_TO_BTC;
    }
    
    
    public String getCalculationDetails(FeeLevel feeLevel) {
        double satoshiPerByte = SATOSHI_PER_BYTE_BASE * feeLevel.getMultiplier();
        return String.format("Bitcoin - Taille: %d bytes, Tarif: %.2f sat/byte, Multiplicateur: %.1fx",
            AVERAGE_TX_SIZE_BYTES, satoshiPerByte, feeLevel.getMultiplier());
    }
}