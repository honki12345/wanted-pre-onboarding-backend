package me.honki12345.wantedassignment.service;

import me.honki12345.wantedassignment.common.MemberNotFoundException;
import me.honki12345.wantedassignment.common.NotAuthorizedException;
import me.honki12345.wantedassignment.common.PostNotFoundException;
import me.honki12345.wantedassignment.domain.Member;
import me.honki12345.wantedassignment.domain.Post;
import me.honki12345.wantedassignment.dto.PostDTO;
import me.honki12345.wantedassignment.repository.MemberRepository;
import me.honki12345.wantedassignment.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PostServiceTest {
    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    SessionService sessionService;

    final String USER_NAME = "user@bbb.com";
    Member member;

    @BeforeEach
    void setUp() {
        String email = USER_NAME;
        String pwd = "pwd";
        member = Member.builder()
                .email(email)
                .pwd(pwd)
                .build();

        memberRepository.saveAndFlush(member);
    }

    @AfterEach
    void tearDown() {
        postRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @WithMockUser(username = USER_NAME)
    @DisplayName("게시글 생성에 성공한다")
    @Test
    void create() {
        // given
        String title = "title1";
        String content = "content1";
        PostDTO postDTO = PostDTO.of(title, content);

        // when
        PostDTO savedPostDTO = postService.create(postDTO);

        // then
        Assertions.assertThat(savedPostDTO)
                .hasFieldOrPropertyWithValue("title", title)
                .hasFieldOrPropertyWithValue("content", content)
                .hasFieldOrPropertyWithValue("author", USER_NAME)
                .hasNoNullFieldsOrProperties();
    }

    @WithMockUser(username = "wrongUser")
    @DisplayName("게시글 생성시 유저 정보가 문제가 있어 예외를 반환한다")
    @Test
    void create_Member_Exception() {
        // given
        String title = "title1";
        String content = "content1";
        PostDTO postDTO = PostDTO.of(title, content);

        // when // then
        assertThrows(MemberNotFoundException.class, () -> postService.create(postDTO));
    }

    @DisplayName("게시물 목록조회를 성공한다")
    @Test
    void list() {
        // given
        String email = "aaa@bbb.com";
        String pwd = "pwd";

        Post post1 = Post.builder()
                .title("title1")
                .content("content1")
                .member(member)
                .build();

        Post post2 = Post.builder()
                .title("title2")
                .content("content2")
                .member(member)
                .build();

        postRepository.saveAll(List.of(post2, post1));

        int pageNumber = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(pageNumber, size, Sort.by("id"));

        // when
        List<PostDTO> list = postService.list(pageRequest);

        // then
        Assertions.assertThat(list).contains(PostDTO.from(post1), PostDTO.from(post2));
    }

    @DisplayName("게시글 상세조회에 성공한다")
    @Test
    void get() {
        // given
        Post post1 = Post.builder()
                .title("title1")
                .content("content1")
                .member(member)
                .build();

        Post post2 = Post.builder()
                .title("title2")
                .content("content2")
                .member(member)
                .build();

        Post savedPost1 = postRepository.saveAndFlush(post1);
        Post savedPost2 = postRepository.saveAndFlush(post2);

        // when
        PostDTO post1DTO = postService.get(savedPost1.getId());
        PostDTO post2DTO = postService.get(savedPost2.getId());

        // then
        Assertions.assertThat(post1DTO)
                .hasFieldOrPropertyWithValue("title", "title1")
                .hasFieldOrPropertyWithValue("content", "content1");
        Assertions.assertThat(post2DTO)
                .hasFieldOrPropertyWithValue("title", "title2")
                .hasFieldOrPropertyWithValue("content", "content2");
    }

    @DisplayName("게시글 상세조회시 존재하지 않는 게시물이라서 예외를 반환한다")
    @Test
    void get_Post_Exception() {
        // given
        long postId = 1L;

        // when // then
        assertThrows(PostNotFoundException.class, () -> postService.get(postId));
    }

    @WithMockUser(username = USER_NAME)
    @DisplayName("게시글 수정을 성공한다")
    @Test
    void update() {
        // given
        Post post = Post.builder()
                .title("title1")
                .content("content1")
                .member(member)
                .build();
        Post savedPost = postRepository.saveAndFlush(post);

        String title = "1title";
        String content = "1content";
        PostDTO postDTO = PostDTO.of(title, content);

        // when
        PostDTO updatedPostDTO = postService.update(savedPost.getId(), postDTO);

        // then
        Assertions.assertThat(updatedPostDTO)
                .hasFieldOrPropertyWithValue("id", savedPost.getId())
                .hasFieldOrPropertyWithValue("title", postDTO.title())
                .hasFieldOrPropertyWithValue("content", postDTO.content())
                .hasFieldOrPropertyWithValue("author", USER_NAME);
    }

    @WithMockUser("wrongUser")
    @DisplayName("게시글 수정시 해당 작성자이지 않아 예외를 반환한다")
    @Test
    void test() {
        // given
        Post post = Post.builder()
                .title("title1")
                .content("content1")
                .member(member)
                .build();
        Post savedPost = postRepository.saveAndFlush(post);

        String title = "1title";
        String content = "1content";
        PostDTO postDTO = PostDTO.of(title, content);

        // when // then
        assertThrows(NotAuthorizedException.class, () -> postService.update(savedPost.getId(), postDTO));
    }

    @DisplayName("게시물 수정시 게시글이 존재하지 않아 예외를 반환한다")
    @Test
    void update_Post_Exception() {
        // given
        Long postId = 1L;

        String title = "1title";
        String content = "1content";
        PostDTO postDTO = PostDTO.of(title, content);

        // when // then
        assertThrows(PostNotFoundException.class, () -> postService.update(postId, postDTO));
    }

    // TODO 삭제 테스트
}