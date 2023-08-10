package me.honki12345.wantedassignment.dto;

import me.honki12345.wantedassignment.domain.Member;
import me.honki12345.wantedassignment.domain.Post;

public record PostDTO(
        Long id,
        String title,
        String content,
        String author) {

    public static PostDTO of(String title, String content) {
        return new PostDTO(null, title, content, null);
    }

    public static PostDTO from(Post post) {
        return new PostDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getMember().getEmail()
        );
    }
}
