package me.honki12345.wantedassignment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.honki12345.wantedassignment.domain.Member;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private Member member;

    public static PostDTO of(String title, String content) {
        return new PostDTO(null, title, content, null);
    }

    public static PostDTO of(String title, String content, Member member) {
        return new PostDTO(null, title, content, member);
    }
}
