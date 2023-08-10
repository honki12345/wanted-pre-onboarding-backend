package me.honki12345.wantedassignment.service;

import lombok.RequiredArgsConstructor;
import me.honki12345.wantedassignment.common.SecurityUtil;
import me.honki12345.wantedassignment.domain.Authority;
import me.honki12345.wantedassignment.domain.Member;
import me.honki12345.wantedassignment.dto.MemberDTO;
import me.honki12345.wantedassignment.repository.MemberRepository;
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
    public MemberDTO signup(MemberDTO memberDTO) {
        // TODO
        if (memberRepository.findOneWithAuthoritiesByEmail(memberDTO.email())
                .orElse(null) != null) {
            throw new RuntimeException();
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        Member member = Member.builder()
                .email(memberDTO.email())
                .pwd(passwordEncoder.encode(memberDTO.pwd()))
                .authorities(Collections.singleton(authority))
                .build();

        return MemberDTO.from(memberRepository.save(member));
    }
}
