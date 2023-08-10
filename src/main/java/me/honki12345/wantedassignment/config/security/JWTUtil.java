package me.honki12345.wantedassignment.config.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JWTUtil {
    @Value("${me.honki12345.jwt.secret}")
    private String key;

    @Value("${me.honki12345.jwt.issuer}")
    private String issuer;

    // JWT 문자열 생성
    public String generateToken(Map<String, Object> valueMap, int days) {
        log.info("generateKey..." + key);

        // header
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        // payload
        HashMap<String, Object> payloads = new HashMap<>();
        payloads.putAll(valueMap);

        // time
        int time = (60 * 24) * days;

        String jwtString = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuer(issuer)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                // TODO 테스트용으로 분단위(plusMinuts로 해줌) 추후에는 plusDays() 로 변경
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(time).toInstant()))
                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .compact();

        return jwtString;
    }

    // 토큰 검증
    public Map<String, Object> validateToken(String token) throws JwtException {
        Map<String, Object> claim = null;

        claim = Jwts.parser()
                // key 설정
                .setSigningKey(key.getBytes())
                // 파싱 및 검증, 실패시 예외
                .parseClaimsJws(token)
                .getBody();

        log.info("---claim: {}", claim);

        return claim;
    }
}
