package com.hoodie.otti.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hoodie.otti.dto.login.KakaoTokenDto;
import com.hoodie.otti.dto.login.ServiceTokenDto;
import com.hoodie.otti.dto.login.UserDto;
import com.hoodie.otti.service.user.KakaoOAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
public class KakaoOAuthController {

    private final KakaoOAuthService kakaoOAuthService;

    @ResponseBody
    @GetMapping("/kakao")
    public KakaoTokenDto kakaoCallback(@RequestParam String code) {
        return kakaoOAuthService.getKakaoToken(code);
    }


    @PostMapping("/login")
    public ResponseEntity<ServiceTokenDto> login(@RequestBody UserDto kakaoToken) throws JsonProcessingException {
        ServiceTokenDto serviceToken = kakaoOAuthService.joinAndLogin(kakaoToken);
        return ResponseEntity.ok(serviceToken);
    }
}
