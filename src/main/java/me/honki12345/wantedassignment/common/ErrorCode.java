package me.honki12345.wantedassignment.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_INPUT_VALUE("올바르지 않은 입력값입니다.", "COMMON-001", HttpStatus.BAD_REQUEST),
    NOT_FOUND("해당 값은 존재하지 않습니다", "COMMON-002", HttpStatus.NOT_FOUND);

    private final String message;
    private final String code;
    private final HttpStatus status;

    ErrorCode(String message, String code, HttpStatus status) {
        this.message = message;
        this.code = code;
        this.status = status;
    }
}
