package me.honki12345.wantedassignment.controller;

import lombok.RequiredArgsConstructor;
import me.honki12345.wantedassignment.controller.dto.LoginRequestDTO;
import me.honki12345.wantedassignment.controller.dto.LoginResponseDTO;
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
    public ResponseEntity<LoginResponseDTO> login(@Validated @RequestBody LoginRequestDTO requestDTO) {
        String jwt = sessionService.createJWTToken(requestDTO);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION_HEADER, AUTHORIZATION_PREFIX + jwt);


        return new ResponseEntity<>(new LoginResponseDTO(jwt), httpHeaders, HttpStatus.CREATED);
    }
}
