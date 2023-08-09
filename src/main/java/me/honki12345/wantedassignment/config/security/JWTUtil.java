package me.honki12345.wantedassignment.config.security;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class JWTUtil {
    @Value("${me.honki12345.jwt.secret}")
    private String key;

    // JWT 문자열 생성
    public String generateToken(Map<String, Object> valueMap, int days) {
        log.info("generateKey..." + key);

        return null;
    }

    // 토큰 검증
    public Map<String, Object> validateToken(String token) throws JwtException {
        Map<String, Object> claim = null;

        return claim;
    }
}
