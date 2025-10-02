package service;

import model.Transaction;
import enums.FeeLevel;

/**
 * Interface Strategy pour le calcul des frais selon le type de cryptomonnaie
 */
public interface FeeCalculator {
    
    /**
     * Calcule les frais pour une transaction donnée
     * @param transaction La transaction à traiter
     * @return Le montant des frais calculés
     */
    double calculateFees(Transaction transaction);
    
    /**
     * Calcule les frais pour un montant et niveau donnés
     * @param amount Le montant de la transaction
     * @param feeLevel Le niveau de priorité des frais
     * @return Le montant des frais calculés
     */
    double calculateFees(double amount, FeeLevel feeLevel);
    
    /**
     * Obtient les frais de base pour ce type de crypto
     * @return Les frais de base
     */
    double getBaseFee();
    
    /**
     * Obtient le nom du calculateur (pour logs/debug)
     * @return Le nom du calculateur
     */
    String getCalculatorName();
}