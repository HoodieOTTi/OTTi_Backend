package com.hoodie.otti.controller.user;

import com.hoodie.otti.dto.login.KakaoTokenResponse;
import com.hoodie.otti.dto.login.KakaoProfileResponse;
import com.hoodie.otti.entity.login.User;
import com.hoodie.otti.service.user.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

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

    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam("code") String code) {
        String tokenUri = UriComponentsBuilder.fromUriString("https://kauth.kakao.com/oauth/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("code", code)
                .queryParam("client_secret", clientSecret)
                .build().toUriString();

        // Token request
        ResponseEntity<KakaoTokenResponse> tokenResponseEntity = restTemplate.exchange(
                tokenUri,
                HttpMethod.POST,
                null,
                KakaoTokenResponse.class
        );

        if (tokenResponseEntity.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(tokenResponseEntity.getStatusCode()).body("Error fetching token");
        }

        KakaoTokenResponse tokenResponse = tokenResponseEntity.getBody();
        String accessToken = tokenResponse != null ? tokenResponse.getAccessToken() : null;

        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No access token received");
        }

        // User info request
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoProfileResponse> userInfoResponseEntity = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                entity,
                KakaoProfileResponse.class
        );

        if (userInfoResponseEntity.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(userInfoResponseEntity.getStatusCode()).body("Error fetching user info");
        }

        KakaoProfileResponse userInfoResponse = userInfoResponseEntity.getBody();
        KakaoProfileResponse.KakaoAccount kakaoAccount = userInfoResponse.getKakaoAccount();
        KakaoProfileResponse.KakaoAccount.Profile profile = kakaoAccount.getProfile();

        String kakaoUserId = String.valueOf(userInfoResponse.getId());
        String email = kakaoAccount.getEmail();
        String nickname = profile.getNickname();

        User user = userService.findByKakaoUserId(kakaoUserId).orElse(
                new User(email, kakaoUserId, nickname)
        );
        userService.saveUser(user);

        return ResponseEntity.ok(user);
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
