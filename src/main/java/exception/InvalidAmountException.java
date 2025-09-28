package exception ;

public class InvalidAmountException extends Exception{

    final final double invalidAmont ;

    // case of null value 
    public InvalidAmountException(String message){
        super(message);
        this.invalidAmont = 0.0 ;
    }

    // case of negative value 
    public double InvalidAddressException(double invalidAmont){
        super("Message : " + invalidAmont + "must be positif");
        this.invalidAmont = invalidAmont ;
    }

    public double getInvalidAmont(){
        return invalidAmont ;
    }

    

}