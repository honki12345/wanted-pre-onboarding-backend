package me.honki12345.wantedassignment.web.dto;

import lombok.Builder;
import me.honki12345.wantedassignment.application.domain.Member;

@Builder
public record SignupResponseDTO(
        Long id
) {
    public static SignupResponseDTO from(Member member) {
        return new SignupResponseDTO(member.getId());
    }
}
