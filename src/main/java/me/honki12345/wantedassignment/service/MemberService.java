package me.honki12345.wantedassignment.service;

import lombok.RequiredArgsConstructor;
import me.honki12345.wantedassignment.domain.Member;
import me.honki12345.wantedassignment.dto.MemberDTO;
import me.honki12345.wantedassignment.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void join(MemberDTO memberDTO) {
        memberRepository.save(Member.builder()
                .email(memberDTO.email())
                .pwd(bCryptPasswordEncoder.encode(memberDTO.pwd()))
                .build());
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}
