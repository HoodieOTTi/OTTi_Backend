package com.hoodie.otti.controller.user;

import com.hoodie.otti.service.user.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;

@Controller
public class KakaoOAuth2Controller {

    private final UserService userService;
    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userInfoUri;

    public KakaoOAuth2Controller(UserService userService) {
        this.userService = userService;
        this.restTemplate = new RestTemplate();
    }

    @GetMapping("/login/kakaologin")
    public String kakaoLogin() {
        String authUri = UriComponentsBuilder.fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .build().toUriString();
        return "redirect:" + authUri;
    }

    @RequestMapping(value = "/oauth/kakao/callback", method = RequestMethod.GET)
    public ResponseEntity<String> kakaoCallback(String code) {
        String tokenUri = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "YOUR_CLIENT_ID");
        params.add("redirect_uri", "YOUR_REDIRECT_URI");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<HashMap> response = restTemplate.exchange(
                tokenUri, HttpMethod.POST, request, HashMap.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(response.getStatusCode()).body("Error fetching token");
        }

        HashMap<String, Object> responseBody = response.getBody();
        String accessToken = (String) responseBody.get("access_token");

        return ResponseEntity.ok("Access Token: " + accessToken);
    }


    @PostMapping("/api/logout")
    public ResponseEntity<?> logout() {
        // 세션 무효화
        // 세션에 관련된 처리 로직을 추가해야 합니다 (예: 세션 삭제, 인증 정보 클리어 등)
        return ResponseEntity.ok("Successfully logged out");
    }

    @PostMapping("/api/delete-user")
    public ResponseEntity<?> deleteUser(@RequestParam("kakaoUserId") String kakaoUserId) {
        // 계정 삭제 처리
        userService.deleteUserByKakaoUserId(kakaoUserId);
        return ResponseEntity.ok("User successfully deleted");
    }
}
