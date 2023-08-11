package me.honki12345.wantedassignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.honki12345.wantedassignment.config.SecurityConfig;
import me.honki12345.wantedassignment.config.jwt.JwtAccessDeniedHandler;
import me.honki12345.wantedassignment.config.jwt.JwtAuthenticationEntryPoint;
import me.honki12345.wantedassignment.config.jwt.TokenProvider;
import me.honki12345.wantedassignment.controller.dto.LoginRequestDTO;
import me.honki12345.wantedassignment.service.SessionService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {SessionController.class})
@Import({
        SecurityConfig.class,
        TokenProvider.class,
        JwtAuthenticationEntryPoint.class,
        JwtAccessDeniedHandler.class
})
class SessionControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    SessionService sessionService;

    final String EMAIL_VALID_MSG = "이메일 형식이 올바르지 않습니다";
    final String PWD_VALID_MSG = "비밀번호 형식이 올바르지 않습니다";

    @DisplayName("로그인에 성공한다")
    @Test
    void signup() throws Exception {
        // given
        String email = "aaaa@naver.com";
        String pwd = "1234888888";
        LoginRequestDTO requestDTO = LoginRequestDTO.builder()
                .email(email)
                .pwd(pwd)
                .build();
        String requestBody = objectMapper.writeValueAsString(requestDTO);

        String jwt = "jwtTokenExample";
        when(sessionService.createJWTToken(requestDTO)).thenReturn(jwt);

        // when // then
        mockMvc.perform(
                        post("/session")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value(jwt));

    }

    @DisplayName("로그인 요청의 이메일 입력값이 올바르지 않아 예외가 발생한다")
    @Test
    void login_Email_Exception() throws Exception {
        // given
        String email = "aaaanaver.com";
        String pwd = "1234888888";
        LoginRequestDTO requestDTO = LoginRequestDTO.builder()
                .email(email)
                .pwd(pwd)
                .build();
        String requestBody = objectMapper.writeValueAsString(requestDTO);

        String jwt = "jwtTokenExample";
        when(sessionService.createJWTToken(requestDTO)).thenReturn(jwt);

        // when // then
        mockMvc.perform(
                        post("/session")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(EMAIL_VALID_MSG))
                .andExpect(jsonPath("$.code").value("COMMON-001"));
    }

    @DisplayName("로그인 요청의 비밀번호 입력값이 올바르지 않아 예외가 발생한다")
    @Test
    void login_Pwd_Exception() throws Exception {
        // given
        String email = "aaaa@naver.com";
        String pwd = "1234";
        LoginRequestDTO requestDTO = LoginRequestDTO.builder()
                .email(email)
                .pwd(pwd)
                .build();
        String requestBody = objectMapper.writeValueAsString(requestDTO);

        String jwt = "jwtTokenExample";
        when(sessionService.createJWTToken(requestDTO)).thenReturn(jwt);

        // when // then
        mockMvc.perform(
                        post("/session")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(PWD_VALID_MSG))
                .andExpect(jsonPath("$.code").value("COMMON-001"));
    }
    @DisplayName("로그인 요청의 이메일과 비밀번호 둘 다 값이 올바르지 않아 예외가 발생한다")
    @Test
    void login_Email_Pwd_Exception() throws Exception {
        // given
        String email = "aaaaavercom";
        String pwd = "123";
        LoginRequestDTO requestDTO = LoginRequestDTO.builder()
                .email(email)
                .pwd(pwd)
                .build();
        String requestBody = objectMapper.writeValueAsString(requestDTO);

        String jwt = "jwtTokenExample";
        when(sessionService.createJWTToken(requestDTO)).thenReturn(jwt);

        // when // then
        mockMvc.perform(
                        post("/session")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(Matchers.containsString(EMAIL_VALID_MSG)))
                .andExpect(jsonPath("$.message").value(Matchers.containsString(PWD_VALID_MSG)))
                .andExpect(jsonPath("$.code").value("COMMON-001"));
    }
}