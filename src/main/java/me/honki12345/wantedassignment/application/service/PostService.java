package me.honki12345.wantedassignment.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.honki12345.wantedassignment.common.exception.MemberNotFoundException;
import me.honki12345.wantedassignment.common.exception.NotAuthorizedException;
import me.honki12345.wantedassignment.common.exception.PostNotFoundException;
import me.honki12345.wantedassignment.web.dto.PostCreateRequestDTO;
import me.honki12345.wantedassignment.web.dto.PostCreateResponseDTO;
import me.honki12345.wantedassignment.web.dto.PostGetResponseDTO;
import me.honki12345.wantedassignment.web.dto.PostUpdateRequestDTO;
import me.honki12345.wantedassignment.web.dto.PostUpdateResponseDTO;
import me.honki12345.wantedassignment.application.domain.Member;
import me.honki12345.wantedassignment.application.domain.Post;
import me.honki12345.wantedassignment.application.repository.MemberRepository;
import me.honki12345.wantedassignment.application.repository.PostRepository;
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
    public PostCreateResponseDTO create(PostCreateRequestDTO requestDTO) {
        Member member = memberRepository.findByEmail(getUsername())
               .orElseThrow(MemberNotFoundException::new);

        Post post = Post.builder()
                .title(requestDTO.title())
                .content(requestDTO.content())
                .member(member)
                .build();

        return PostCreateResponseDTO.from(postRepository.save(post));
    }

    @Transactional(readOnly = true)
    public List<PostGetResponseDTO> list(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(PostGetResponseDTO::from)
                        .stream().toList();
    }

    @Transactional(readOnly = true)
    public PostGetResponseDTO get(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        return PostGetResponseDTO.from(post);
    }

    @Transactional
    public PostUpdateResponseDTO update(Long id, PostUpdateRequestDTO postDTO) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        authorizePostAuthor(post);

        post.update(postDTO);
        Post savedPost = postRepository.save(post);

        return PostUpdateResponseDTO.from(savedPost);
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
