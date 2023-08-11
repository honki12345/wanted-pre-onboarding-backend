package me.honki12345.wantedassignment.controller.dto;

import lombok.Builder;
import me.honki12345.wantedassignment.domain.Member;

@Builder
public record SignupResponseDTO(
        Long id
) {
    public static SignupResponseDTO from(Member member) {
        return new SignupResponseDTO(member.getId());
    }
}
