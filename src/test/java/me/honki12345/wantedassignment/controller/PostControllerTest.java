package me.honki12345.wantedassignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.honki12345.wantedassignment.common.ErrorCode;
import me.honki12345.wantedassignment.common.NotAuthorizedException;
import me.honki12345.wantedassignment.common.PostNotFoundException;
import me.honki12345.wantedassignment.config.SecurityConfig;
import me.honki12345.wantedassignment.config.jwt.JwtAccessDeniedHandler;
import me.honki12345.wantedassignment.config.jwt.JwtAuthenticationEntryPoint;
import me.honki12345.wantedassignment.config.jwt.TokenProvider;
import me.honki12345.wantedassignment.dto.PostDTO;
import me.honki12345.wantedassignment.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {PostController.class})
@Import({
        SecurityConfig.class,
        TokenProvider.class,
        JwtAuthenticationEntryPoint.class,
        JwtAccessDeniedHandler.class
})
class PostControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PostService postService;

    @WithMockUser
    @DisplayName("게시글 생성을 @WithMockUser을 통해 성공한다")
    @Test
    void create() throws Exception {
        // given
        long id = 1L;
        String title = "title1";
        String content = "content1";
        String author = "author";
        PostDTO postDTO = PostDTO.of(title, content);
        PostDTO savedPostDTO = new PostDTO(id, title, content, author);

        String requestBody = objectMapper.writeValueAsString(postDTO);
        when(postService.create(postDTO)).thenReturn(savedPostDTO);

        // when // then
        mockMvc.perform(
                        post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.author").value(author));
    }

    @DisplayName("게시글 생성을 로그인하지 않아 권한이 없어 실패합니다")
    @Test
    void createException() throws Exception {
        // given
        long id = 1L;
        String title = "title1";
        String content = "content1";
        String author = "author";
        PostDTO postDTO = PostDTO.of(title, content);
        PostDTO savedPostDTO = new PostDTO(id, title, content, author);

        String requestBody = objectMapper.writeValueAsString(postDTO);
        when(postService.create(postDTO)).thenReturn(savedPostDTO);

        // when // then
        mockMvc.perform(
                        post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("게시글 목록조회를 성공한다")
    @Test
    void list() throws Exception {
        // given
        String postId1 = "1";
        String title1 = "title1";
        String content1 = "content1";
        String author1 = "author1";
        PostDTO postDTO1 = PostDTO.of(postId1, title1, content1, author1);

        String postId2 = "2";
        String title2 = "title2";
        String content2 = "content2";
        String author2 = "author2";
        PostDTO postDTO2 = PostDTO.of(postId2, title2, content2, author2);

        int pageNumber = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(pageNumber, size, Sort.by("id"));

        when(postService.list(pageRequest)).thenReturn(List.of(postDTO1, postDTO2));

        // when // then
        mockMvc.perform(get("/posts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(postDTO1.title()))
                .andExpect(jsonPath("$[0].content").value(postDTO1.content()))
                .andExpect(jsonPath("$[1].title").value(postDTO2.title()))
                .andExpect(jsonPath("$[1].title").value(postDTO2.title()));
    }

    @DisplayName("게시글 상세조회를 성공한다")
    @Test
    void getPost() throws Exception {
        // given
        long postId = 1L;

        String title = "title1";
        String content = "content1";
        PostDTO postDTO = PostDTO.of(title, content);

        when(postService.get(postId)).thenReturn(postDTO);

        // when // then
        mockMvc.perform(
                        get("/posts/{id}", postId)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(postDTO.title()))
                .andExpect(jsonPath("$.content").value(postDTO.content()));
    }

    @WithMockUser
    @DisplayName("게시글수정을 @WithMockUser을 통해 성공한다")
    @Test
    void update() throws Exception {
        // given
        String title = "title1";
        String content = "content1";
        PostDTO postDTO = PostDTO.of(title, content);

        long postId = 1L;
        String author = "aaa@bbb.com";
        PostDTO updatedPostDTO = new PostDTO(postId, title, content, author);

        String requestBody = objectMapper.writeValueAsString(postDTO);
        when(postService.update(postId, postDTO)).thenReturn(updatedPostDTO);

        // when // then
        mockMvc.perform(
                        patch("/posts/{id}", postId)
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.id").value(postId))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.author").value(author));

    }

    @DisplayName("게시글수정을 로그인 없이 요청하여 401 unauthorized 반환받는다")
    @Test
    void update_Login_Exception() throws Exception {
        // given
        String title = "title1";
        String content = "content1";
        PostDTO postDTO = PostDTO.of(title, content);

        long postId = 1L;
        String author = "aaa@bbb.com";
        PostDTO updatedPostDTO = new PostDTO(postId, title, content, author);

        String requestBody = objectMapper.writeValueAsString(postDTO);
        when(postService.update(postId, postDTO)).thenReturn(updatedPostDTO);

        // when // then
        mockMvc.perform(
                        patch("/posts/{id}", postId)
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @DisplayName("게시글수정을 해당 id 의 게시글이 존재하지 않아 404 NotFound 반환한다")
    @Test
    void update_Post_Exception() throws Exception {
        // given
        long postId = 1L;

        String title = "title1";
        String content = "content1";
        PostDTO postDTO = PostDTO.of(title, content);
        String requestBody = objectMapper.writeValueAsString(postDTO);

        PostNotFoundException postNotFoundException = new PostNotFoundException();
        doThrow(postNotFoundException).when(postService).update(postId, postDTO);

        // when // then
        mockMvc.perform(
                        patch("/posts/{id}", postId)
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ErrorCode.POST_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.code").value(ErrorCode.POST_NOT_FOUND.getCode()));

    }

    @WithMockUser
    @DisplayName("게시글수정을 게시글 작성자가 아닌 유저가 요청하여 403 forbidden 반환받는다")
    @Test
    void update_Author_Exception() throws Exception {
        // given
        long postId = 1L;

        String title = "title1";
        String content = "content1";
        PostDTO postDTO = PostDTO.of(title, content);

        String requestBody = objectMapper.writeValueAsString(postDTO);

        when(postService.update(postId, postDTO)).thenThrow(NotAuthorizedException.class);

        // when // then
        mockMvc.perform(
                        patch("/posts/{id}", postId)
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(ErrorCode.NOT_AUTHORIZED.getMessage()))
                .andExpect(jsonPath("$.code").value(ErrorCode.NOT_AUTHORIZED.getCode()));

    }

    @WithMockUser
    @DisplayName("게시글삭제를 @WithMockUser을 통해 성공한다")
    @Test
    void deletePost() throws Exception {
        // given
        long postId = 1L;

        doNothing().when(postService).delete(postId);

        // when // then
        mockMvc.perform(
                        delete("/posts/{id}", postId)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName("게시글삭제를 로그인하지 않아 401 UnAuthorized 받는다")
    @Test
    void deletePost_Login_Exception () throws Exception {
        // given
        long postId = 1L;

        doNothing().when(postService).delete(postId);

        // when // then
        mockMvc.perform(
                        delete("/posts/{id}", postId)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @DisplayName("게시글삭제를 해당 id 의 게시글이 없어서 404 NotFound 받는다")
    @Test
    void deletePost_Post_Exception() throws Exception {
        // given
        long postId = 1L;
        PostNotFoundException postNotFoundException = new PostNotFoundException();

        doThrow(postNotFoundException).when(postService).delete(postId);

        // when // then
        mockMvc.perform(
                        delete("/posts/{id}", postId)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ErrorCode.POST_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.code").value(ErrorCode.POST_NOT_FOUND.getCode()));
    }

    @WithMockUser
    @DisplayName("게시글삭제를 해당 사용자가 아니어서 403 Forbidden 받는다")
    @Test
    void deletePost_Author_Exception() throws Exception {
        // given
        long postId = 1L;

        doThrow(new NotAuthorizedException()).when(postService).delete(postId);

        // when // then
        mockMvc.perform(
                        delete("/posts/{id}", postId)
                )
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(ErrorCode.NOT_AUTHORIZED.getMessage()))
                .andExpect(jsonPath("$.code").value(ErrorCode.NOT_AUTHORIZED.getCode()));
    }
}