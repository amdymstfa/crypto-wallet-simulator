package enums ;

public enum TransactionStatus {
    PENDING("In progress" , "Waiting for validation"),
    CONFIRMED("Confirmed", "Your transaction was valited with sucess"),
    REJECTED("Rejected" , "Sorry, you transaction was refused");

    private final String status ;
    private final String description ;

    public TransactionStatus(String status ,String description){
        this.status = status ;
        this.description = description ;
    }

    public String getStatus(){return status;}
    public String getDescription(){return description ;}
}