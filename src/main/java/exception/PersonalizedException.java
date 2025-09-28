package exception;

public class PersonalizedException extends Exception {
    // constructor for a simple exception for a clear message
    public PersonalizedException(String message){
        super(message);
    }

    // construtor for a message and cause of error
    public PersonalizedException(String message, Throwable cause){
        super(message, cause);
    }

    public PersonalizedException(String firstMessage, String secondMessage){
        super(firstMessage, secondMessage)
    }
}