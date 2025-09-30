package service;

import model.Transaction;
import enums.FeeLevel;
import util.LoggerUtil;

public class EthereumFeeCalculator implements FeeCalculator {
    
    private static final long GAS_LIMIT = 21000;  
    private static final double GWEI_TO_ETH = 0.000000001;  
    private static final double BASE_GAS_PRICE_GWEI = 30.0;  
    
    @Override
    public double calculateFees(Transaction transaction) {
        return calculateFees(transaction.getAmount(), transaction.getFeeLevel());
    }
    
    @Override
    public double calculateFees(double amount, FeeLevel feeLevel) {
        double gasPriceGwei = BASE_GAS_PRICE_GWEI * feeLevel.getMultiplier();
        double gasPriceEth = gasPriceGwei * GWEI_TO_ETH;
        double feesInETH = GAS_LIMIT * gasPriceEth;
        
        LoggerUtil.logFeeCalculation("ETHEREUM_CALC", feesInETH, 
            String.format("Niveau: %s, Gas: %.2f Gwei", feeLevel, gasPriceGwei));
        
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
    
    
    public double calculateFeesWithGasLimit(long gasLimit, FeeLevel feeLevel) {
        double gasPriceGwei = BASE_GAS_PRICE_GWEI * feeLevel.getMultiplier();
        double gasPriceEth = gasPriceGwei * GWEI_TO_ETH;
        return gasLimit * gasPriceEth;
    }
    
    
    public double gweiToEth(double gwei) {
        return gwei * GWEI_TO_ETH;
    }
    
    
    public String getCalculationDetails(FeeLevel feeLevel) {
        double gasPriceGwei = BASE_GAS_PRICE_GWEI * feeLevel.getMultiplier();
        return String.format("Ethereum - Gas Limit: %d, Prix: %.2f Gwei, Multiplicateur: %.1fx",
            GAS_LIMIT, gasPriceGwei, feeLevel.getMultiplier());
    }
}