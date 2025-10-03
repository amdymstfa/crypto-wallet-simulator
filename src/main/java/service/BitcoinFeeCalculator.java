package service;

import model.Transaction;
import enums.FeeLevel;
import util.LoggerUtil;

/**
 * Bitcoin-specific fee calculator
 * Formula: estimated size in bytes × satoshi per byte rate
 */
public class BitcoinFeeCalculator implements FeeCalculator {
    
    // Realistic Bitcoin parameters (fictional but coherent)
    private static final int AVERAGE_TX_SIZE_BYTES = 250;  
    private static final double SATOSHI_PER_BYTE_BASE = 20.0;  
    private static final double SATOSHI_TO_BTC = 0.00000001;  
    
    @Override
    public double calculateFees(Transaction transaction) {
        return calculateFees(transaction.getAmount(), transaction.getFeeLevel());
    }
    
    @Override
    public double calculateFees(double amount, FeeLevel feeLevel) {
        // Bitcoin fee: size × rate per byte × priority multiplier
        double satoshiPerByte = SATOSHI_PER_BYTE_BASE * feeLevel.getMultiplier();
        double totalSatoshi = AVERAGE_TX_SIZE_BYTES * satoshiPerByte;
        double feesInBTC = totalSatoshi * SATOSHI_TO_BTC;
        
        LoggerUtil.logFeeCalculation("BITCOIN_CALC", feesInBTC, 
            String.format("Level: %s, Sat/byte: %.2f", feeLevel, satoshiPerByte));
        
        return feesInBTC;
    }
    
    @Override
    public double getBaseFee() {
        return AVERAGE_TX_SIZE_BYTES * SATOSHI_PER_BYTE_BASE * SATOSHI_TO_BTC;
    }
    
    @Override
    public String getCalculatorName() {
        return "BitcoinFeeCalculator";
    }
    
    /**
     * Bitcoin-specific method to calculate fee for a custom transaction size
     */
    public double calculateFeesWithSize(int transactionSizeBytes, FeeLevel feeLevel) {
        double satoshiPerByte = SATOSHI_PER_BYTE_BASE * feeLevel.getMultiplier();
        double totalSatoshi = transactionSizeBytes * satoshiPerByte;
        return totalSatoshi * SATOSHI_TO_BTC;
    }
    
    /**
     * Returns debug information about Bitcoin fee calculation
     */
    public String getCalculationDetails(FeeLevel feeLevel) {
        double satoshiPerByte = SATOSHI_PER_BYTE_BASE * feeLevel.getMultiplier();
        return String.format("Bitcoin - Size: %d bytes, Rate: %.2f sat/byte, Multiplier: %.1fx",
            AVERAGE_TX_SIZE_BYTES, satoshiPerByte, feeLevel.getMultiplier());
    }
}
