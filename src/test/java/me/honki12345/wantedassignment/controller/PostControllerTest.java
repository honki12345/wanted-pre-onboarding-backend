package me.honki12345.wantedassignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.honki12345.wantedassignment.config.security.WebSecurityConfig;
import me.honki12345.wantedassignment.dto.PostDTO;
import me.honki12345.wantedassignment.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {PostController.class})
@Import(WebSecurityConfig.class)
class PostControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PostService postService;

    @DisplayName("게시글 생성을 성공한다")
    @Test
    void create() throws Exception {
        // given
        String title = "title1";
        String content = "content1";
        PostDTO postDTO = PostDTO.of(title, content);

        String requestBody = objectMapper.writeValueAsString(postDTO);
        doNothing().when(postService).create(postDTO);

        // when // then
        mockMvc.perform(
                        post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("게시글 목록조회를 성공한다")
    @Test
    void test() throws Exception {
        // given
        String title1 = "title1";
        String content1 = "content1";
        PostDTO postDTO1 = PostDTO.of(title1, content1);

        String title2 = "title2";
        String content2 = "content2";
        PostDTO postDTO2 = PostDTO.of(title2, content2);

        when(postService.list()).thenReturn(List.of(postDTO1, postDTO2));

        // when // then
        // TODO 페이지네이션
        mockMvc.perform(get("/posts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(postDTO1.title()))
                .andExpect(jsonPath("$[0].content").value(postDTO1.content()))
                .andExpect(jsonPath("$[1].title").value(postDTO2.title()))
                .andExpect(jsonPath("$[1].title").value(postDTO2.title()));
    }

    @DisplayName("게시글 조회를 성공한다")
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

    @DisplayName("게시글 수정을 성공한다")
    @Test
    void update() throws Exception {
        // TODO 사용자인증
        // given
        long postId = 1L;

        String title = "title1";
        String content = "content1";
        PostDTO postDTO = PostDTO.of(title, content);

        String requestBody = objectMapper.writeValueAsString(postDTO);

        doNothing().when(postService).update(postId, postDTO);

        // when // then
        mockMvc.perform(
                        patch("/posts/{id}", postId)
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName("게시글 삭제에 성공한다")
    @Test
    void deletePost() throws Exception {
        // TODO 사용자인증
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
}