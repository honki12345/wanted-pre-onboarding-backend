package me.honki12345.wantedassignment.service;

import lombok.extern.slf4j.Slf4j;
import me.honki12345.wantedassignment.domain.Post;
import me.honki12345.wantedassignment.dto.PostDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PostService {
    public void create(PostDTO postDTO) {

    }

    public List<PostDTO> list() {
        return null;
    }

    public PostDTO get(Long id) {
        return null;
    }

    public void update(Long id, PostDTO postDTO) {
    }

    public void delete(Long id) {

    }

    private void authorizePostAuthor(Post post) {
        String userName
                = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!post.getAuthor().equals(userName)) {
            throw new IllegalArgumentException("not authorized");
        }
    }
}
