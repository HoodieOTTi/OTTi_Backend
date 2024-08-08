package com.hoodie.otti.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hoodie.otti.dto.login.KakaoTokenDto;
import com.hoodie.otti.dto.login.ServiceTokenDto;
import com.hoodie.otti.dto.login.UserDto;
import com.hoodie.otti.service.user.KakaoOAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
public class KakaoOAuthController {

    private final KakaoOAuthService kakaoOAuthService;

    @PostMapping("/kakao")
    public KakaoTokenDto kakaoCallback(@RequestParam String code) {
        return kakaoOAuthService.getKakaoToken(code);
    }


    @PostMapping("/login")
    public ResponseEntity<ServiceTokenDto> login(@RequestBody UserDto kakaoToken) throws JsonProcessingException {
        ServiceTokenDto serviceToken = kakaoOAuthService.joinAndLogin(kakaoToken);
        return ResponseEntity.ok(serviceToken);
    }

    @GetMapping("/logout")
    public void logout(HttpServletResponse response) throws IOException {
        String logoutUrl = kakaoOAuthService.buildKakaoLogoutUrl();
        response.sendRedirect(logoutUrl);
    }

    @GetMapping("/unlink")
    public ResponseEntity<String> unlink(HttpServletRequest request) {
        // Authorization 헤더에서 액세스 토큰을 추출
        String accessToken = request.getHeader("Authorization");
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7); // "Bearer "를 제거하여 토큰만 추출
        } else {
            return ResponseEntity.badRequest().body("Error: access_token is required");
        }

        try {
            kakaoOAuthService.unlinkUser(accessToken);
            request.getSession().invalidate();
            return ResponseEntity.ok("User successfully unlinked");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

}
