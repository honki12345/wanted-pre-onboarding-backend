package me.honki12345.wantedassignment.web.dto;

import lombok.Builder;

@Builder
public record PostUpdateRequestDTO(
        String title,
        String content
) {
}
