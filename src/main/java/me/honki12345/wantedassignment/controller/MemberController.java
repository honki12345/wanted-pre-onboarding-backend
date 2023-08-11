package me.honki12345.wantedassignment.controller;

import lombok.RequiredArgsConstructor;
import me.honki12345.wantedassignment.controller.dto.SignupRequestDTO;
import me.honki12345.wantedassignment.controller.dto.SignupResponseDTO;
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
    public ResponseEntity<SignupResponseDTO> signup(@Validated @RequestBody SignupRequestDTO requestDTO) {
        SignupResponseDTO responseDTO
                = memberService.signup(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
}
