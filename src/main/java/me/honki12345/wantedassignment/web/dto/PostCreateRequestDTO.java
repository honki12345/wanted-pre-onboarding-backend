package me.honki12345.wantedassignment.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PostCreateRequestDTO(
        @NotBlank(message = "{post.title}")
        String title,

        @NotBlank(message = "{post.content}")
        String content
) {
}
