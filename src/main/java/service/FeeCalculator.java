package service;

import model.Transaction;
import enums.FeeLevel;

/**
 * Strategy interface for calculating fees based on cryptocurrency type
 */
public interface FeeCalculator {
    
    /**
     * Calculate fees for a given transaction
     * @param transaction The transaction to process
     * @return Calculated fee amount
     */
    double calculateFees(Transaction transaction);
    
    /**
     * Calculate fees for a given amount and fee level
     * @param amount Transaction amount
     * @param feeLevel Fee priority level
     * @return Calculated fee amount
     */
    double calculateFees(double amount, FeeLevel feeLevel);
    
    /**
     * Get the base fee for this cryptocurrency type
     * @return Base fee amount
     */
    double getBaseFee();
    
    /**
     * Get the calculator name (for logging/debugging)
     * @return Calculator name
     */
    String getCalculatorName();
}
