package me.honki12345.wantedassignment.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // TODO
/*
        // AuthenticationManager 설정
        AuthenticationManagerBuilder authenticationManagerBuilder
                = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder());

        // AuthenticationManager
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
*/


        AuthenticationManager authenticationManager = authorizationManager(http, bCryptPasswordEncoder(), userDetailsService);
        http.authenticationManager(authenticationManager);

        // LoginFilter
        LoginFilter loginFilter = new LoginFilter(antMatcher(HttpMethod.POST, "/session"));
        loginFilter.setAuthenticationManager(authenticationManager);
        http.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);


        // authorizeHttpRequests
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()

                // TODO hasRole() 통과하는지 테스트해보기
//                                .requestMatchers(antMatcher(HttpMethod.PATCH, "posts/*")).hasRole("USER")
                .requestMatchers(antMatcher(HttpMethod.PATCH, "/posts/*")).authenticated()
                .requestMatchers(antMatcher(HttpMethod.DELETE, "/posts/*")).authenticated()
                .anyRequest().permitAll());

        http.httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .headers().frameOptions().disable();
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public AuthenticationManager authorizationManager(HttpSecurity http,
                                                      BCryptPasswordEncoder bCryptPasswordEncoder,
                                                      UserDetailsService userDetailsService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
