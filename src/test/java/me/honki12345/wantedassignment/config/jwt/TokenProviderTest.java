package me.honki12345.wantedassignment.config.jwt;

import io.jsonwebtoken.Jwts;
import me.honki12345.wantedassignment.domain.Member;
import me.honki12345.wantedassignment.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import javax.xml.bind.helpers.AbstractUnmarshallerImpl;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class TokenProviderTest {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtProperties jwtProperties;

    // generateToken() 검증 테스트
    @DisplayName("유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다")
    @Test
    void generateToken() {
        // Given
        Member testMember = memberRepository.save(Member.builder()
                .email("user@gmail.com")
                .pwd("test")
                .build());

        // When
        String token = tokenProvider.generateToken(testMember, Duration.ofDays(14));

        // Then
        Long memberId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        assertThat(memberId).isEqualTo(testMember.getId());
    }

    // validToken() 검증 테스트
    @DisplayName("만료된 토크인 때에 유효성 검증에 실패한다")
    @Test
    void validToken_invalidToken() {
        // Given
        String token = JwtFactory.builder()
                .expiration(
                        new Date(
                                (new Date().getTime() - Duration.ofDays(7).toMillis())))
                .build().createToken(jwtProperties);

        // When
        boolean result = tokenProvider.validToken(token);

        // Then
        assertThat(result).isFalse();
    }

    @DisplayName("유효인 토크인 때에 유효성 검증에 성공한다")
    @Test
    void validToken_validToken() {
        // Given
        String token = JwtFactory.withDefaultValues()
                .createToken(jwtProperties);

        // When
        boolean result = tokenProvider.validToken(token);

        // Then
        assertThat(result).isTrue();
    }

    // getAuthentication() 검증 테스트
    @DisplayName("토큰 기반으로 인증 정보를 가져올 수 있다")
    @Test
    void getAuthentication() {
        // Given
        String userEmail = "user@email.com";
        String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);

        // When
        Authentication authentication = tokenProvider.getAuthentication(token);

        // Then
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        assertThat(principal.getUsername()).isEqualTo(userEmail);
    }

    // getUserId() 검증 테스트
    @DisplayName("토큰으로 유저 ID를 가져올 수 있다")
    @Test
    void getUserId() {
        // Given
        long userId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("id", userId))
                .build()
                .createToken(jwtProperties);

        // When
        Long userIdByToken = tokenProvider.getUserId(token);

        // Then
        assertThat(userIdByToken).isEqualTo(userId);
    }
}
