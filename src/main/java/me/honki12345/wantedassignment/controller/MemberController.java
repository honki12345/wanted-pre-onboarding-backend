package me.honki12345.wantedassignment.controller;

import lombok.RequiredArgsConstructor;
import me.honki12345.wantedassignment.dto.MemberDTO;
import me.honki12345.wantedassignment.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<Object> join(@Validated @RequestBody MemberDTO memberDTO) {
        memberService.join(memberDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
