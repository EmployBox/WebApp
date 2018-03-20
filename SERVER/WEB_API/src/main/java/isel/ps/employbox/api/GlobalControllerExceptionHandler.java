package isel.ps.employbox.api;

import isel.ps.employbox.api.exceptions.InvalidApiKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidApiKeyException.class)
    public void handleInvalidAPIKey(){
        //Nothing to do
    }
}
