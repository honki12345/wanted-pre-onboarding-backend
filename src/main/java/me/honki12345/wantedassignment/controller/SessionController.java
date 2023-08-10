package me.honki12345.wantedassignment.controller;

import lombok.RequiredArgsConstructor;
import me.honki12345.wantedassignment.config.jwt.TokenProvider;
import me.honki12345.wantedassignment.dto.MemberDTO;
import me.honki12345.wantedassignment.dto.TokenDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static me.honki12345.wantedassignment.config.jwt.JwtFilter.*;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping
    public ResponseEntity<TokenDTO> login(@RequestBody MemberDTO memberDTO) {
        // TODO 로그인 로직 & jwt 토큰값 반환 & 예외처리 & 검증

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(memberDTO.email(), memberDTO.pwd());

        Authentication authentication
                = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION_HEADER, AUTHORIZATION_PREFIX + jwt);


        return new ResponseEntity<>(new TokenDTO(jwt), httpHeaders, HttpStatus.OK);
    }
}
