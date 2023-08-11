package me.honki12345.wantedassignment.web.dto;

import lombok.Builder;
import me.honki12345.wantedassignment.application.domain.Post;

@Builder
public record PostGetResponseDTO(
        Long id,
        String title,
        String content,
        String author
) {

    public static PostGetResponseDTO from(Post post) {
        return PostGetResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getMember().getEmail())
                .build();
    }
}
