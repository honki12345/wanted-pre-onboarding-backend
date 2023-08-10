package me.honki12345.wantedassignment.config.security;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application.yaml")
@ActiveProfiles("test")
class JWTUtilTest {
    @Autowired
    private JWTUtil jwtUtil;

    @Value("${me.honki12345.jwt.secret}")
    private String key;

    @DisplayName("JWT 을 생성하면 특정 로그가 출력된다")
    @Test
    void generateToken(CapturedOutput output) {
        // Given
        Map<String, Object> claimMap = Map.of("email", "pwd");
        String message = "generateKey..." + key;

        // When
        String jwtString = jwtUtil.generateToken(claimMap, 1);

        // Then
        assertTrue(output.getOut().contains(message));
        log.info(jwtString);

    }

    @DisplayName("JWT 을 검증한다")
    @Test
    void validateToken(CapturedOutput output) {
        // Given
        String jwtString = jwtUtil.generateToken(
                Map.of("mid", "aaaa",
                        "email", "aaaa@bbbb.com"),
                1);

        // When
        Map<String, Object> claim = jwtUtil.validateToken(jwtString);

        // Then
        Assertions.assertThat(claim.get("mid")).isEqualTo("aaaa");
        Assertions.assertThat(claim.get("email")).isEqualTo("aaaa@bbbb.com");
    }

}