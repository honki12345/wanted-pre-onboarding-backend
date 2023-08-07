package me.honki12345.wantedassignment.controller;

import me.honki12345.wantedassignment.dto.MemberDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {
    @PostMapping
    public ResponseEntity<Object> login(@RequestBody MemberDTO memberDTO) {
        // TODO 로그인 로직 & jwt 토큰값 반환 & 예외처리
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
