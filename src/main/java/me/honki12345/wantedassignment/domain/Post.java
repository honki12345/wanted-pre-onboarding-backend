package me.honki12345.wantedassignment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.honki12345.wantedassignment.controller.dto.PostUpdateRequestDTO;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @Builder
    private Post(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post post)) return false;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void update(PostUpdateRequestDTO postDTO) {
        if (postDTO.content() != null) {
            this.content = postDTO.content();
        }

        if (postDTO.title() != null) {
            this.title = postDTO.title();
        }
    }
}
