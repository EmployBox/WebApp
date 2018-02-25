package dataMapper;

public class ConcurrencyException extends RuntimeException{
    public ConcurrencyException(String message) {
        super(message);
    }
}
