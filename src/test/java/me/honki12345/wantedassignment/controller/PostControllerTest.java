package me.honki12345.wantedassignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.honki12345.wantedassignment.dto.PostDTO;
import me.honki12345.wantedassignment.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        String title = "title1";
        String content = "content1";
        PostDTO postDTO = new PostDTO(title, content);
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
}