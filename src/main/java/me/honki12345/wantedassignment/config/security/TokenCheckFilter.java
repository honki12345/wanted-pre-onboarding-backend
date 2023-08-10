package me.honki12345.wantedassignment.config.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.honki12345.wantedassignment.exception.AccessTokenException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String method = request.getMethod();
        boolean path = request.getRequestURI().matches("/posts/\\d+");

        if (path && (method.equalsIgnoreCase("PATCH") || method.equalsIgnoreCase("DELETE"))) {
            log.info("PATH & method: {} & {}", path, method);
            try {
                validateAccessToken(request);
                log.info("---tokenCheckFilter success");
                filterChain.doFilter(request, response);
            } catch (AccessTokenException e) {
                log.error("error");
                e.sendResponseError(response);
            } catch (Exception e) {
                log.error("error");
            }
        }

        filterChain.doFilter(request, response);
    }

    private Map<String, Object> validateAccessToken(HttpServletRequest request) {
        String headerString = request.getHeader("Authorization");

        if (headerString == null || headerString.length() < 8) {
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);
        }

        // Bearer 생략
        String tokenType = headerString.substring(0, 6);
        String tokenString = headerString.substring(7);

        log.info("header tokenType: {}", tokenType);
        log.info("header tokenString: {}", tokenString);

        if (tokenType.equalsIgnoreCase("Bearer") == false) {
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
        }

        try {
            Map<String, Object> values = jwtUtil.validateToken(tokenString);
            return values;
        } catch (MalformedJwtException e) {
            log.error("MalformedJwtException");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.MALFORM);
        } catch (SignatureException signatureException) {
            log.error("SignatureException");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADSIGN);
        } catch (ExpiredJwtException e) {
            log.error("ExpiredJwtException");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.EXPIRED);
        }

    }
}
