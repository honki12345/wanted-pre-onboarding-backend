package me.honki12345.wantedassignment.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {
    private final String refreshPath;
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (!path.equals(refreshPath)) {
            log.info("skip refresh token filter...");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Refresh Token Filter...run....");
    }
}
