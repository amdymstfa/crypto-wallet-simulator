package service;

import model.Transaction;
import enums.FeeLevel;
import util.LoggerUtil;

/**
 * Calculateur de frais spécifique pour Bitcoin
 * Utilise : taille estimée en bytes × tarif satoshi par byte
 */
public class BitcoinFeeCalculator implements FeeCalculator {
    
    // Paramètres Bitcoin réalistes (valeurs fictives mais cohérentes)
    private static final int AVERAGE_TX_SIZE_BYTES = 250;  // Taille moyenne transaction Bitcoin
    private static final double SATOSHI_PER_BYTE_BASE = 20.0;  // Satoshi par byte (base)
    private static final double SATOSHI_TO_BTC = 0.00000001;   // 1 satoshi = 0.00000001 BTC
    
    @Override
    public double calculateFees(Transaction transaction) {
        return calculateFees(transaction.getAmount(), transaction.getFeeLevel());
    }
    
    @Override
    public double calculateFees(double amount, FeeLevel feeLevel) {
        // Calcul Bitcoin : taille × tarif par byte × multiplicateur de priorité
        double satoshiPerByte = SATOSHI_PER_BYTE_BASE * feeLevel.getMultiplier();
        double totalSatoshi = AVERAGE_TX_SIZE_BYTES * satoshiPerByte;
        double feesInBTC = totalSatoshi * SATOSHI_TO_BTC;
        
        LoggerUtil.logFeeCalculation("BITCOIN_CALC", feesInBTC, 
            String.format("Niveau: %s, Sat/byte: %.2f", feeLevel, satoshiPerByte));
        
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
     * Méthode spécifique Bitcoin pour calculer selon la taille réelle
     */
    public double calculateFeesWithSize(int transactionSizeBytes, FeeLevel feeLevel) {
        double satoshiPerByte = SATOSHI_PER_BYTE_BASE * feeLevel.getMultiplier();
        double totalSatoshi = transactionSizeBytes * satoshiPerByte;
        return totalSatoshi * SATOSHI_TO_BTC;
    }
    
    /**
     * Informations de debug sur les paramètres Bitcoin
     */
    public String getCalculationDetails(FeeLevel feeLevel) {
        double satoshiPerByte = SATOSHI_PER_BYTE_BASE * feeLevel.getMultiplier();
        return String.format("Bitcoin - Taille: %d bytes, Tarif: %.2f sat/byte, Multiplicateur: %.1fx",
            AVERAGE_TX_SIZE_BYTES, satoshiPerByte, feeLevel.getMultiplier());
    }
}