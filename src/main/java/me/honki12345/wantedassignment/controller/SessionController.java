package me.honki12345.wantedassignment.controller;

import lombok.RequiredArgsConstructor;
import me.honki12345.wantedassignment.dto.MemberDTO;
import me.honki12345.wantedassignment.dto.TokenDTO;
import me.honki12345.wantedassignment.service.SessionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static me.honki12345.wantedassignment.config.jwt.JwtFilter.*;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionController {
    private final SessionService sessionService;
    
    @PostMapping
    public ResponseEntity<TokenDTO> login(@Validated @RequestBody MemberDTO memberDTO) {
        // TODO 로그인 로직 & jwt 토큰값 반환 & 예외처리 & 검증

        String jwt = sessionService.createJWTToken(memberDTO);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION_HEADER, AUTHORIZATION_PREFIX + jwt);


        return new ResponseEntity<>(new TokenDTO(jwt), httpHeaders, HttpStatus.CREATED);
    }
}
