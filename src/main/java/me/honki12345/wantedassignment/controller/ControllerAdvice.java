package me.honki12345.wantedassignment.controller;

import me.honki12345.wantedassignment.exception.ErrorCode;
import me.honki12345.wantedassignment.exception.ErrorResponse;
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
}
