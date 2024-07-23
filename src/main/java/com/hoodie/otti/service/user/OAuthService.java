package com.hoodie.otti.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoodie.otti.dto.login.KakaoInfo;
import com.hoodie.otti.dto.login.MemberResponse;
import com.hoodie.otti.dto.login.RegisterRequest;
import com.hoodie.otti.model.login.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Service
public class OAuthService {

    private final UserService userService;

    @Autowired
    public OAuthService(UserService userService) {
        this.userService = userService;
    }


    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;


    // 토큰 받기
    public String getAccessToken(String code) throws JsonProcessingException {
        String tokenUri = "https://kauth.kakao.com/oauth/token";

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 설정
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("client_secret", clientSecret);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                tokenUri, HttpMethod.POST, kakaoTokenRequest, String.class);

        // HTTP 응답(JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }

    // 액세스 토큰으로 사용자 정보 가져오기
    public KakaoInfo getKakaoInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        // responseBody에 있는 정보 꺼내기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        Long userId = jsonNode.get("id").asLong();
        String userEmail = jsonNode.get("kakao_account").get("email").asText();
        String nickname = jsonNode.get("properties").get("nickname").asText();

        return new KakaoInfo(userId, nickname, userEmail);
    }


    // 카카오 사용자 정보 확인
    public MemberResponse ifNeedKakaoInfo (KakaoInfo kakaoInfo) {
        // DB에 중복되는 email이 있는지 확인
        String kakaoEmail = kakaoInfo.getEmail();

        Optional<User> kakaoUserOptional = userService.findByEmail(kakaoEmail);

        MemberResponse kakaoMember = kakaoUserOptional.map(user -> new MemberResponse(user.getUserId(), user.getNickname(), user.getUserEmail()))
                .orElse(null);

        // 회원가입
        if (kakaoMember == null) {
            String kakaoNickname = kakaoInfo.getNickname();
            // 이메일로 임시 id 발급
//            int idx= kakaoEmail.indexOf("@");
//            Long kakaoId = kakaoEmail.substring(0, idx);
            Long kakaoId = kakaoInfo.getId(); // 카카오에서 제공하는 고유 ID를 문자열로 변환
            // 임시 password 발급 - random UUID
            String tempPassword = UUID.randomUUID().toString();

            RegisterRequest registerMember = new RegisterRequest();
            registerMember.setUserId(kakaoId);
            registerMember.setPassword(tempPassword);
            registerMember.setNickname(kakaoNickname);
            registerMember.setEmail(kakaoEmail);

            userService.saveUser(registerMember);
            // DB 재조회
            kakaoUserOptional = userService.findByEmail(kakaoEmail);
            kakaoMember = kakaoUserOptional.map(user -> new MemberResponse(user.getUserId(), user.getNickname(), user.getUserEmail()))
                    .orElse(null);
        }

        return kakaoMember;
    }

    // 카카오 로그아웃
    public void kakaoDisconnect(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded");

        // HTTP 요청 보내기
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        HttpEntity<String> request = new HttpEntity<>(headers); // 빈 본문으로 수정
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v1/user/logout",
                HttpMethod.POST,
                request,
                String.class
        );

        // responseBody에 있는 정보를 꺼냄
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        Long id = jsonNode.get("id").asLong();
        System.out.println("반환된 id: "+id);
    }

}
