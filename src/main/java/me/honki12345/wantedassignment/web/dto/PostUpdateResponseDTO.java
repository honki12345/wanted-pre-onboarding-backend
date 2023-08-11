package me.honki12345.wantedassignment.web.dto;

import lombok.Builder;
import me.honki12345.wantedassignment.application.domain.Post;

@Builder
public record PostUpdateResponseDTO(
        Long id,
        String title,
        String content,
        String author
) {
    public static PostUpdateResponseDTO from(Post post) {
        return PostUpdateResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getMember().getEmail())
                .build();
    }
}
