package me.honki12345.wantedassignment.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import me.honki12345.wantedassignment.domain.Member;

import java.util.Set;
import java.util.stream.Collectors;

public record MemberDTO(
        Long id,

        @Pattern(regexp = ".+@.+", message = "{member.email}")
        String email,

        @Size(min = 8, message = "비밀번호 형식이 올바르지 않습니다")
        String pwd,

        Set<AuthorityDTO> authorities

) {
    public static MemberDTO of(String email, String pwd) {
        return new MemberDTO(null, email, pwd, null);
    }

    public static MemberDTO from(Member member) {
        if (member == null) {
            return null;
        }

        return new MemberDTO(
                member.getId(),
                member.getEmail(),
                member.getPwd(),
                member.getAuthorities().stream()
                        .map(authority -> new AuthorityDTO(authority.getAuthorityName()))
                        .collect(Collectors.toSet())
        );
    }

}
