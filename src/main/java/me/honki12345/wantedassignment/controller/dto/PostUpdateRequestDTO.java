package me.honki12345.wantedassignment.controller.dto;

import lombok.Builder;

@Builder
public record PostUpdateRequestDTO(
        String title,
        String content
) {
}
