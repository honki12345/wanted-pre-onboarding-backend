package me.honki12345.wantedassignment.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MemberDTO(
        Long id,

        @Pattern(regexp = ".+@.+", message = "{member.email}")
        String email,

        @Size(min = 8, message = "비밀번호 형식이 올바르지 않습니다")
        String pwd) {
    public static MemberDTO of(String email, String pwd) {
        return new MemberDTO(null, email, pwd);
    }
}
