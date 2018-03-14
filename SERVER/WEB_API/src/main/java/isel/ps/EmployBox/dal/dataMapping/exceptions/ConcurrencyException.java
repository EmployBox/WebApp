package isel.ps.EmployBox.dal.dataMapping.exceptions;

public class ConcurrencyException extends RuntimeException{
    public ConcurrencyException(String message) {
        super(message);
    }
}
