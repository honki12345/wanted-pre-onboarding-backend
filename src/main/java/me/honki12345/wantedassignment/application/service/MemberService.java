package me.honki12345.wantedassignment.application.service;

import lombok.RequiredArgsConstructor;
import me.honki12345.wantedassignment.common.exception.DuplicateEmailException;
import me.honki12345.wantedassignment.web.dto.SignupRequestDTO;
import me.honki12345.wantedassignment.web.dto.SignupResponseDTO;
import me.honki12345.wantedassignment.application.domain.Authority;
import me.honki12345.wantedassignment.application.domain.Member;
import me.honki12345.wantedassignment.application.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignupResponseDTO signup(SignupRequestDTO requestDTO) {
        if (memberRepository.findOneWithAuthoritiesByEmail(requestDTO.email())
                .orElse(null) != null) {
            throw new DuplicateEmailException();
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        Member member = Member.builder()
                .email(requestDTO.email())
                .pwd(passwordEncoder.encode(requestDTO.pwd()))
                .authorities(Collections.singleton(authority))
                .build();

        return SignupResponseDTO.from(memberRepository.save(member));
    }
}
