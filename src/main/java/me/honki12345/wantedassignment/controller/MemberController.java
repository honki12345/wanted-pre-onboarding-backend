package me.honki12345.wantedassignment.controller;

import lombok.RequiredArgsConstructor;
import me.honki12345.wantedassignment.dto.MemberDTO;
import me.honki12345.wantedassignment.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Object> join(@RequestBody MemberDTO memberDTO) {
        // TODO 유효성 검사 및 암호화로직
        memberService.join(memberDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}