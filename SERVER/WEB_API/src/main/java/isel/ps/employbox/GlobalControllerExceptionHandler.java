package isel.ps.employbox;

import isel.ps.employbox.exceptions.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorModel> handleBadRequest(BadRequestException exception){
        ErrorModel error = new ErrorModel();
        error.detail = exception.getMessage();
        error.status = HttpStatus.BAD_REQUEST.value();
        error.type = "http://example.com/error/types/rels/some-type";
        error.title = HttpStatus.BAD_REQUEST.getReasonPhrase();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorModel> handleUnauthorized (UnauthorizedException exception){
        HttpHeaders headers = new HttpHeaders();
        headers.add("WWW-Authenticate","Basic realm = \"checklists\" charset=\"UTF-8\"");
        ErrorModel error = new ErrorModel();
        error.detail = exception.getMessage();
        error.status = HttpStatus.UNAUTHORIZED.value();
        error.type = "about:blank";
        error.title = HttpStatus.UNAUTHORIZED.getReasonPhrase();
        return new ResponseEntity<>(error, headers, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorModel> handleForbiddenRequest(ForbiddenException exception){
        ErrorModel error = new ErrorModel();
        error.detail = exception.getMessage();
        error.status = HttpStatus.FORBIDDEN.value();
        error.type = "about:blank";
        error.title = HttpStatus.FORBIDDEN.getReasonPhrase();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorModel> handleResourceNotFound (ResourceNotFoundException exception){
        ErrorModel error = new ErrorModel();
        error.detail = exception.getMessage();
        error.status = HttpStatus.NOT_FOUND.value();
        error.type = "about:blank";
        error.title = HttpStatus.NOT_FOUND.getReasonPhrase();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorModel> handleConflict(ConflictException exception){
        ErrorModel error = new ErrorModel();
        error.detail = exception.getMessage();
        error.status = HttpStatus.CONFLICT.value();
        error.type = "about:blank";
        error.title = HttpStatus.CONFLICT.getReasonPhrase();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    public static class ErrorModel {
        public String type;
        public int status;
        public String detail;
        public String title;
    }
}