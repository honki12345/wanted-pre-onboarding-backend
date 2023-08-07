package me.honki12345.wantedassignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.honki12345.wantedassignment.domain.Member;
import me.honki12345.wantedassignment.dto.PostDTO;
import me.honki12345.wantedassignment.service.PostService;
import org.hibernate.dialect.TiDBDialect;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {PostController.class})
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
        String email = "aaaa@naver.com";
        String pwd = "1234";
        Member member = Member.builder().email(email).pwd(pwd).build();

        String title = "title1";
        String content = "content1";
        PostDTO postDTO = PostDTO.of(title, content, member);

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
        String email = "aaaa@naver.com";
        String pwd = "1234";
        Member member = Member.builder().email(email).pwd(pwd).build();

        String title1 = "title1";
        String content1 = "content1";
        PostDTO postDTO1 = PostDTO.of(title1, content1, member);

        String title2 = "title2";
        String content2 = "content2";
        PostDTO postDTO2 = PostDTO.of(title2, content2, member);

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
}