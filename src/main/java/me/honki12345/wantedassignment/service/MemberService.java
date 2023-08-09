package me.honki12345.wantedassignment.service;

import lombok.RequiredArgsConstructor;
import me.honki12345.wantedassignment.domain.Member;
import me.honki12345.wantedassignment.dto.MemberDTO;
import me.honki12345.wantedassignment.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void join(MemberDTO memberDTO) {
        // TODO 중복검사
        memberRepository.save(Member.builder()
                .email(memberDTO.email())
                .pwd(passwordEncoder.encode(memberDTO.pwd()))
                .build());
    }
}
