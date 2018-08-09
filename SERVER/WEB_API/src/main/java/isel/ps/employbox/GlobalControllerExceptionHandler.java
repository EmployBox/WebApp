package isel.ps.employbox;

import isel.ps.employbox.exceptions.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final String BLANK = "about:BLANK";

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorModel> handleBadRequest(BadRequestException exception){
        ErrorModel error = new ErrorModel("http://example.com/error/types/rels/some-type", HttpStatus.BAD_REQUEST.value(), exception.getMessage(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorModel> handleUnauthorized (UnauthorizedException exception){
        HttpHeaders headers = new HttpHeaders();
        headers.add("WWW-Authenticate","Basic realm = \"checklists\" charset=\"UTF-8\"");
        ErrorModel error = new ErrorModel(BLANK, HttpStatus.UNAUTHORIZED.value(), exception.getMessage(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        return new ResponseEntity<>(error, headers, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorModel> handleForbiddenRequest(ForbiddenException exception){
        ErrorModel error = new ErrorModel(BLANK, HttpStatus.FORBIDDEN.value(), exception.getMessage(), HttpStatus.FORBIDDEN.getReasonPhrase());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorModel> handleResourceNotFound (ResourceNotFoundException exception){
        ErrorModel error = new ErrorModel(BLANK, HttpStatus.NOT_FOUND.value(), exception.getMessage(), HttpStatus.NOT_FOUND.getReasonPhrase());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorModel> handleConflict(ConflictException exception){
        ErrorModel error = new ErrorModel(BLANK, HttpStatus.CONFLICT.value(), exception.getMessage(), HttpStatus.CONFLICT.getReasonPhrase());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    public static class ErrorModel {
        private final String type;
        private final int status;
        private final String detail;
        private final String title;

        public ErrorModel(String type, int status, String detail, String title) {
            this.type = type;
            this.status = status;
            this.detail = detail;
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public int getStatus() {
            return status;
        }

        public String getDetail() {
            return detail;
        }

        public String getTitle() {
            return title;
        }
    }
}