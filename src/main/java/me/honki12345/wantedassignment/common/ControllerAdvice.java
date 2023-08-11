package me.honki12345.wantedassignment.common;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.StringJoiner;

@RestControllerAdvice
public class ControllerAdvice {
    //    EMAIL_DUPLICATE("중복된 이메일이 존재합니다", "MEMBER-002", HttpStatus.BAD_REQUEST),
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> duplicateEmailException(DuplicateEmailException e) {
        return new ResponseEntity<>(
                ErrorResponse.of(ErrorCode.EMAIL_DUPLICATE),
                ErrorCode.EMAIL_DUPLICATE.getStatus()
        );
    }


//    POST_NOT_FOUND("해당 게시글을 찾을 수 없습니다", "POST-001", HttpStatus.NOT_FOUND);
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorResponse> postNotFoundException(PostNotFoundException e) {
        return new ResponseEntity<>(
                ErrorResponse.of(ErrorCode.POST_NOT_FOUND),
                ErrorCode.POST_NOT_FOUND.getStatus()
        );
    }

//    MEMBER_NOT_FOUND("해당 유저를 찾을 수 없습니다", "MEMBER-001", HttpStatus.NOT_FOUND),
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> memberNotFoundException(MemberNotFoundException e) {
        return new ResponseEntity<>(
                ErrorResponse.of(ErrorCode.MEMBER_NOT_FOUND),
                ErrorCode.MEMBER_NOT_FOUND.getStatus()
        );
    }

//    NOT_FOUND("해당 값은 존재하지 않습니다", "COMMON-002", HttpStatus.NOT_FOUND),
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundException(NotFoundException e) {
        return new ResponseEntity<>(
                ErrorResponse.of(ErrorCode.NOT_FOUND),
                ErrorCode.NOT_FOUND.getStatus()
        );
    }

//    INVALID_INPUT_VALUE("올바르지 않은 입력값입니다.", "COMMON-001",HttpStatus.BAD_REQUEST),
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> bindException(BindException e) {
        StringJoiner sj = new StringJoiner(", ");
        e.getBindingResult().getAllErrors().stream().forEach(error -> sj.add(error.getDefaultMessage()));

        return new ResponseEntity<>(
                ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, sj.toString()),
                ErrorCode.INVALID_INPUT_VALUE.getStatus());
    }

//    NOT_AUTHORIZED("권한이 없습니다", "COMMON-003", HttpStatus.FORBIDDEN),
    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ErrorResponse> notAuthorizedException(NotAuthorizedException e) {
        return new ResponseEntity<>(
                ErrorResponse.of(ErrorCode.NOT_AUTHORIZED),
                ErrorCode.NOT_AUTHORIZED.getStatus()
        );
    }
}
