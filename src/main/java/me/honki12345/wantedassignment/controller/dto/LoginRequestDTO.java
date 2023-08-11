package me.honki12345.wantedassignment.controller.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record LoginRequestDTO(
        @Pattern(regexp = ".+@.+", message = "{member.email}")
        String email,

        @Size(min = 8, message = "{member.pwd}")
        String pwd
) {
}
