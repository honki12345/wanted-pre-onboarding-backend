package me.honki12345.wantedassignment.common;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.StringJoiner;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> bindException(BindException e) {
        StringJoiner sj = new StringJoiner(", ");
        e.getBindingResult().getAllErrors().stream().forEach(error -> sj.add(error.getDefaultMessage()));

        return new ResponseEntity<>(
                ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, sj.toString()),
                ErrorCode.INVALID_INPUT_VALUE.getStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundException(NotFoundException e) {
        return new ResponseEntity<>(
                ErrorResponse.of(ErrorCode.NOT_FOUND, e.getMessage()),
                ErrorCode.NOT_FOUND.getStatus()
        );
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ErrorResponse> notAuthorizedException(NotAuthorizedException e) {
        return new ResponseEntity<>(
                ErrorResponse.of(ErrorCode.NOT_AUTHORIZED),
                ErrorCode.NOT_AUTHORIZED.getStatus()
        );
    }
}
