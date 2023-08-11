package me.honki12345.wantedassignment.config;

import lombok.RequiredArgsConstructor;
import me.honki12345.wantedassignment.config.jwt.JwtAccessDeniedHandler;
import me.honki12345.wantedassignment.config.jwt.JwtAuthenticationEntryPoint;
import me.honki12345.wantedassignment.config.jwt.JwtFilter;
import me.honki12345.wantedassignment.config.jwt.TokenProvider;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.*;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                ));
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .headers().frameOptions().disable();

        http.sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(ex -> ex
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntryPoint));

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(antMatcher("/h2-console/**")).permitAll()
                .requestMatchers(antMatcher("/favicon.ico")).permitAll()
                .requestMatchers(antMatcher(HttpMethod.POST, "/posts")).authenticated()
                .requestMatchers(antMatcher(HttpMethod.PATCH, "/posts/*")).authenticated()
                .requestMatchers(antMatcher(HttpMethod.DELETE, "/posts/*")).authenticated()
                .anyRequest().permitAll()
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
