package service;

import model.Transaction;
import enums.FeeLevel;
import util.LoggerUtil;

/**
 * Calculateur de frais spécifique pour Ethereum
 * Utilise : limite de gas × prix du gas
 */
public class EthereumFeeCalculator implements FeeCalculator {
    
    // Paramètres Ethereum réalistes (valeurs fictives mais cohérentes)
    private static final long GAS_LIMIT = 21000;  // Gas limit standard pour transfert ETH
    private static final double GWEI_TO_ETH = 0.000000001;  // 1 Gwei = 0.000000001 ETH
    private static final double BASE_GAS_PRICE_GWEI = 30.0;  // Prix base en Gwei
    
    @Override
    public double calculateFees(Transaction transaction) {
        return calculateFees(transaction.getAmount(), transaction.getFeeLevel());
    }
    
    @Override
    public double calculateFees(double amount, FeeLevel feeLevel) {
        // Calcul Ethereum : gas limit × prix gas × multiplicateur de priorité
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
    
    /**
     * Méthode spécifique Ethereum pour calculer avec gas limit personnalisé
     */
    public double calculateFeesWithGasLimit(long gasLimit, FeeLevel feeLevel) {
        double gasPriceGwei = BASE_GAS_PRICE_GWEI * feeLevel.getMultiplier();
        double gasPriceEth = gasPriceGwei * GWEI_TO_ETH;
        return gasLimit * gasPriceEth;
    }
    
    /**
     * Convertit un prix en Gwei vers ETH
     */
    public double gweiToEth(double gwei) {
        return gwei * GWEI_TO_ETH;
    }
    
    /**
     * Informations de debug sur les paramètres Ethereum
     */
    public String getCalculationDetails(FeeLevel feeLevel) {
        double gasPriceGwei = BASE_GAS_PRICE_GWEI * feeLevel.getMultiplier();
        return String.format("Ethereum - Gas Limit: %d, Prix: %.2f Gwei, Multiplicateur: %.1fx",
            GAS_LIMIT, gasPriceGwei, feeLevel.getMultiplier());
    }
}