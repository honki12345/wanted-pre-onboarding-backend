package me.honki12345.wantedassignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.honki12345.wantedassignment.common.DuplicateEmailException;
import me.honki12345.wantedassignment.common.ErrorCode;
import me.honki12345.wantedassignment.config.SecurityConfig;
import me.honki12345.wantedassignment.config.jwt.JwtAccessDeniedHandler;
import me.honki12345.wantedassignment.config.jwt.JwtAuthenticationEntryPoint;
import me.honki12345.wantedassignment.config.jwt.TokenProvider;
import me.honki12345.wantedassignment.dto.MemberDTO;
import me.honki12345.wantedassignment.service.MemberService;
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

@WebMvcTest(controllers = {MemberController.class})
@Import({
        SecurityConfig.class,
        TokenProvider.class,
        JwtAuthenticationEntryPoint.class,
        JwtAccessDeniedHandler.class
})
class MemberControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MemberService memberService;

    String emailErrorMessage = "이메일 형식이 올바르지 않습니다";
    String pwdErrorMessage = "비밀번호 형식이 올바르지 않습니다";

    @DisplayName("회원가입을 성공한다")
    @Test
    void signup() throws Exception {
        // given
        String email = "aaaa@naver.com";
        String pwd = "1234888888";
        MemberDTO memberDTO = MemberDTO.of(email, pwd);
        String requestBody = objectMapper.writeValueAsString(memberDTO);
        when(memberService.signup(memberDTO)).thenReturn(memberDTO);

        // when // then
        mockMvc.perform(
                        post("/members")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("회원가입 요청의 이메일 입력값이 올바르지 않아 예외가 발생한다")
    @Test
    void signupException() throws Exception {
        // given
        String wrongEmail = "google.com";
        String pwd = "1234888888";
        MemberDTO memberDTO = MemberDTO.of(wrongEmail, pwd);
        String requestBody = objectMapper.writeValueAsString(memberDTO);
//        doNothing().when(memberService).signup(memberDTO);
        when(memberService.signup(memberDTO)).thenReturn(memberDTO);
//        String errorMessage = "이메일 형식이 올바르지 않습니다";

        // when // then
        mockMvc.perform(
                        post("/members")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(emailErrorMessage))
                .andExpect(jsonPath("$.code").value("COMMON-001"));
    }

    @DisplayName("회원가입 요청의 비밀번호 입력값이 올바르지 않아 예외가 발생한다")
    @Test
    void signupException2() throws Exception {
        // given
        String email = "ef@google.com";
        String wrongPwd = "188";
        MemberDTO memberDTO = MemberDTO.of(email, wrongPwd);
        String requestBody = objectMapper.writeValueAsString(memberDTO);
//        doNothing().when(memberService).signup(memberDTO);
        when(memberService.signup(memberDTO)).thenReturn(memberDTO);

        // when // then
        mockMvc.perform(
                        post("/members")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(pwdErrorMessage))
                .andExpect(jsonPath("$.code").value("COMMON-001"));
    }
    @DisplayName("회원가입 요청의 이메일과 비밀번호 둘 다 값이 올바르지 않아 예외가 발생한다")
    @Test
    void signupException3() throws Exception {
        // given
        String wrongEmail = "efgoogle.com";
        String wrongPwd = "188";
        MemberDTO memberDTO = MemberDTO.of(wrongEmail, wrongPwd);
        String requestBody = objectMapper.writeValueAsString(memberDTO);
//        doNothing().when(memberService).signup(memberDTO);
        when(memberService.signup(memberDTO)).thenReturn(memberDTO);

        // when // then
        mockMvc.perform(
                        post("/members")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(Matchers.containsString(emailErrorMessage)))
                .andExpect(jsonPath("$.message").value(Matchers.containsString(pwdErrorMessage)))
                .andExpect(jsonPath("$.code").value("COMMON-001"));
    }

    @DisplayName("회원가입시 중복된 이메일이 있어 400 BadRequest 반환한다")
    @Test
    void signup_Duplicate_Email_Exception() throws Exception {
        // Given
        String email = "aaaa@naver.com";
        String pwd = "1234888888";
        MemberDTO memberDTO = MemberDTO.of(email, pwd);
        String requestBody = objectMapper.writeValueAsString(memberDTO);
        when(memberService.signup(memberDTO)).thenThrow(DuplicateEmailException.class);

        // When // Then
        mockMvc.perform(
                        post("/members")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.EMAIL_DUPLICATE.getMessage()))
                .andExpect(jsonPath("$.code").value(ErrorCode.EMAIL_DUPLICATE.getCode()));
    }
}