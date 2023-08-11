package me.honki12345.wantedassignment.controller.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SignupRequestDTO(
        @Pattern(regexp = ".+@.+", message = "{member.email}")
        String email,

        @Size(min = 8, message = "비밀번호 형식이 올바르지 않습니다")
        String pwd
) {
}
