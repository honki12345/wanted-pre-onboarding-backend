package me.honki12345.wantedassignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.honki12345.wantedassignment.dto.MemberDTO;
import me.honki12345.wantedassignment.service.MemberService;
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

@WebMvcTest
class MemberControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MemberService memberService;

    @DisplayName("회원가입을 성공한다")
    @Test
    void join() throws Exception {
        // given
        String email = "aaaa@naver.com";
        String pwd = "1234";
        MemberDTO memberDTO = new MemberDTO(email, pwd);
        String requestBody = objectMapper.writeValueAsString(memberDTO);
        doNothing().when(memberService).join(memberDTO);

        // when // then
        mockMvc.perform(
                        post("/members")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

}