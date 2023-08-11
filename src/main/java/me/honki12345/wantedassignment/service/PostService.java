package me.honki12345.wantedassignment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.honki12345.wantedassignment.common.MemberNotFoundException;
import me.honki12345.wantedassignment.common.NotAuthorizedException;
import me.honki12345.wantedassignment.common.NotFoundException;
import me.honki12345.wantedassignment.common.PostNotFoundException;
import me.honki12345.wantedassignment.domain.Member;
import me.honki12345.wantedassignment.domain.Post;
import me.honki12345.wantedassignment.dto.PostDTO;
import me.honki12345.wantedassignment.repository.MemberRepository;
import me.honki12345.wantedassignment.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public PostDTO create(PostDTO postDTO) {
        Member member = memberRepository.findByEmail(getUsername())
               .orElseThrow(MemberNotFoundException::new);

        Post post = Post.builder()
                .title(postDTO.title())
                .content(postDTO.content())
                .member(member)
                .build();

        return PostDTO.from(postRepository.save(post));
    }

    @Transactional(readOnly = true)
    public List<PostDTO> list(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(post ->
                        PostDTO.of(
                                String.valueOf(post.getId()),
                                post.getTitle(),
                                post.getContent(),
                                post.getMember().getEmail()))
                .stream().toList();
    }

    @Transactional(readOnly = true)
    public PostDTO get(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        return PostDTO.from(post);
    }

    @Transactional
    public PostDTO update(Long id, PostDTO postDTO) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        authorizePostAuthor(post);

        post.update(postDTO);
        Post savedPost = postRepository.save(post);

        return PostDTO.from(savedPost);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        authorizePostAuthor(post);
        postRepository.delete(post);
    }

    private void authorizePostAuthor(Post post) {
        String userName = getUsername();
        if (!post.getMember().getEmail().equals(userName)) {
            throw new NotAuthorizedException();
        }
    }

    private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
