package me.honki12345.wantedassignment.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_INPUT_VALUE("올바르지 않은 입력값입니다.", "COMMON-001", HttpStatus.BAD_REQUEST),
    NOT_FOUND("해당 값은 존재하지 않습니다", "COMMON-002", HttpStatus.NOT_FOUND),
    NOT_AUTHORIZED("권한이 없습니다", "COMMON-003", HttpStatus.FORBIDDEN),
    NEED_LOGIN("로그인이 필요합니다", "COMMON-004", HttpStatus.UNAUTHORIZED),

    MEMBER_NOT_FOUND("해당 유저를 찾을 수 없습니다", "MEMBER-001", HttpStatus.NOT_FOUND),
    EMAIL_DUPLICATE("중복된 이메일이 존재합니다", "MEMBER-002", HttpStatus.BAD_REQUEST),

    POST_NOT_FOUND("해당 게시글을 찾을 수 없습니다", "POST-001", HttpStatus.NOT_FOUND);

    private final String message;
    private final String code;
    private final HttpStatus status;

    ErrorCode(String message, String code, HttpStatus status) {
        this.message = message;
        this.code = code;
        this.status = status;
    }

}
