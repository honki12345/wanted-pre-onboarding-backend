package me.honki12345.wantedassignment.dto;

import me.honki12345.wantedassignment.domain.Member;

public record PostDTO(
        Long id,
        String title,
        String content,
        Member member) {

    public static PostDTO of(String title, String content, Member member) {
        return new PostDTO(null, title, content, member);
    }

}
