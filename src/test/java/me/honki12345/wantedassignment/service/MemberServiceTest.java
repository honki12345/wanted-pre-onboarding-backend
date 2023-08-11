package me.honki12345.wantedassignment.service;

import me.honki12345.wantedassignment.common.DuplicateEmailException;
import me.honki12345.wantedassignment.domain.Member;
import me.honki12345.wantedassignment.dto.AuthorityDTO;
import me.honki12345.wantedassignment.dto.MemberDTO;
import me.honki12345.wantedassignment.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }


    @DisplayName("회원가입에 성공한다")
    @Test
    void signup() {
        // given
        String email = "aaaa@naver.com";
        String pwd = "1234888888";
        MemberDTO memberDTO = MemberDTO.of(email, pwd);

        Set<AuthorityDTO> authorityDTO = Collections.singleton(
                new AuthorityDTO("ROLE_USER"));

        // when
        MemberDTO savedMemberDTO = memberService.signup(memberDTO);

        // then
        Assertions.assertThat(savedMemberDTO)
                .hasFieldOrPropertyWithValue("email", email)
                .hasFieldOrPropertyWithValue("authorities", authorityDTO);
        assertTrue(passwordEncoder.matches(pwd, savedMemberDTO.pwd()));


    }

    @DisplayName("회원가입시 중복된 이메일이 있어 DuplicateEmailException 예외가 발생한다")
    @Test
    void signup_Duplicate_Exception() {
        // given
        String email = "aaaa@naver.com";
        String pwd = "1234888888";
        MemberDTO memberDTO = MemberDTO.of(email, pwd);

        Member member = Member.builder()
                .email(email)
                .pwd(pwd)
                .build();

        memberRepository.save(member);

        // when // then
        assertThrows(DuplicateEmailException.class, () -> memberService.signup(memberDTO));


    }

}