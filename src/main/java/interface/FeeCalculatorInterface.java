package interface ;

import model.Transaction ;
import enums.FeeLevel ;

public interface FeeCalculatorInterface {

    public interface FeeCalculator {
    
    double calculateFees(Transaction transaction);
    
    double calculateFees(double amount, FeeLevel feeLevel);

    double getBaseFee();
    
    String getCalculatorName();
    }
}
