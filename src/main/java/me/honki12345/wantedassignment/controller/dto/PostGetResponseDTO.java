package me.honki12345.wantedassignment.controller.dto;

import lombok.Builder;
import me.honki12345.wantedassignment.domain.Post;

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
