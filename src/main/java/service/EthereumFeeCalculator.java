package service;

import model.Transaction;
import enums.FeeLevel;
import util.LoggerUtil;

/**
 * Ethereum-specific fee calculator
 * Formula: gas limit × gas price
 */
public class EthereumFeeCalculator implements FeeCalculator {
    
    // Realistic Ethereum parameters (fictional but coherent)
    private static final long GAS_LIMIT = 21000;       
    private static final double GWEI_TO_ETH = 0.000000001;
    private static final double BASE_GAS_PRICE_GWEI = 30.0; 
    
    @Override
    public double calculateFees(Transaction transaction) {
        return calculateFees(transaction.getAmount(), transaction.getFeeLevel());
    }
    
    @Override
    public double calculateFees(double amount, FeeLevel feeLevel) {
        // Ethereum fee: gas limit × gas price × priority multiplier
        double gasPriceGwei = BASE_GAS_PRICE_GWEI * feeLevel.getMultiplier();
        double gasPriceEth = gasPriceGwei * GWEI_TO_ETH;
        double feesInETH = GAS_LIMIT * gasPriceEth;
        
        LoggerUtil.logFeeCalculation("ETHEREUM_CALC", feesInETH, 
            String.format("Level: %s, Gas: %.2f Gwei", feeLevel, gasPriceGwei));
        
        return feesInETH;
    }
    
    @Override
    public double getBaseFee() {
        return GAS_LIMIT * BASE_GAS_PRICE_GWEI * GWEI_TO_ETH;
    }
    
    @Override
    public String getCalculatorName() {
        return "EthereumFeeCalculator";
    }
    
    /**
     * Ethereum-specific method to calculate fee with custom gas limit
     */
    public double calculateFeesWithGasLimit(long gasLimit, FeeLevel feeLevel) {
        double gasPriceGwei = BASE_GAS_PRICE_GWEI * feeLevel.getMultiplier();
        double gasPriceEth = gasPriceGwei * GWEI_TO_ETH;
        return gasLimit * gasPriceEth;
    }
    
    /**
     * Convert Gwei to ETH
     */
    public double gweiToEth(double gwei) {
        return gwei * GWEI_TO_ETH;
    }
    
    /**
     * Returns debug info about Ethereum fee calculation
     */
    public String getCalculationDetails(FeeLevel feeLevel) {
        double gasPriceGwei = BASE_GAS_PRICE_GWEI * feeLevel.getMultiplier();
        return String.format("Ethereum - Gas Limit: %d, Price: %.2f Gwei, Multiplier: %.1fx",
            GAS_LIMIT, gasPriceGwei, feeLevel.getMultiplier());
    }
}
