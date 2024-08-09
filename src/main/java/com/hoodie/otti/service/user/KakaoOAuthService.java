package com.hoodie.otti.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.hoodie.otti.dto.login.KakaoTokenDto;
import com.hoodie.otti.dto.login.ServiceTokenDto;
import com.hoodie.otti.dto.login.UserDto;
import com.hoodie.otti.model.profile.User;
import com.hoodie.otti.repository.profile.UserRepository;
import com.hoodie.otti.util.login.JwtTokenProvider;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class KakaoOAuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate;
    private final String KAKAO_CLIENT_ID;
    private final String REDIRECT_URI;
    private final String CLIENT_ID;
    private final String MY_LOGOUT_REDIRECT_URI;


    public KakaoOAuthService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider,
                             @Value("${spring.security.oauth2.client.registration.kakao.client-id}") String kakaoClientId,
                             @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}") String redirectUri,
                             @Value("${spring.security.oauth2.client.registration.kakao.client-id}") String clientId,
                             @Value("${kakao.logout-redirect-uri}") String logoutRedirectUri,
                             RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.restTemplate = restTemplate;
        this.KAKAO_CLIENT_ID = kakaoClientId;
        this.REDIRECT_URI = redirectUri;
        this.CLIENT_ID = clientId;
        this.MY_LOGOUT_REDIRECT_URI = logoutRedirectUri;
    }

    public String getKakaoToken(String code) {
        String reqURL = "https://kauth.kakao.com/oauth/token";

        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> params = Map.of(
                "code", code,
                "client_id", KAKAO_CLIENT_ID
                ,"redirect_uri", REDIRECT_URI,
                "grant_type", "authorization_code"
        );

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(reqURL, params, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String json = responseEntity.getBody();
            Gson gson = new Gson();

            return gson.fromJson(json, KakaoTokenDto.class)
                    .getAccessToken();
        }

        throw new RuntimeException("구글 엑세스 토큰을 가져오는데 실패했습니다.");
    }


    public ServiceTokenDto joinAndLogin(UserDto tokenDto) throws JsonProcessingException {
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.set("Authorization", "Bearer " + tokenDto.getKakaoToken());
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(reqURL, HttpMethod.POST, requestEntity, String.class);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) throw new IllegalArgumentException("응답 실패");

        // 응답 본문을 JSON으로 파싱
        String responseBody = responseEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        Long id = jsonNode.get("id").asLong();
        String userName = jsonNode.path("properties").path("nickname").asText();
        String userEmail = jsonNode.path("kakao_account").path("email").asText();


        if (userRepository.findByKakaoId(id).isEmpty()) {
            User user = new User(id, userName, userEmail);
            userRepository.save(user);
        }
        ServiceTokenDto tokenDTO = jwtTokenProvider.createToken(id);

        return tokenDTO;
    }


    public String buildKakaoLogoutUrl() {
        return "https://kauth.kakao.com/oauth/logout?client_id=" + CLIENT_ID +
                "&logout_redirect_uri=" + URLEncoder.encode(MY_LOGOUT_REDIRECT_URI, StandardCharsets.UTF_8);
    }

    public void unlinkUser(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange("https://kapi.kakao.com/v1/user/unlink", HttpMethod.POST, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                // Success
                System.out.println("Successfully unlinked user: " + response.getBody());
            } else {
                throw new RuntimeException("Failed to unlink user: " + response.getBody());
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Failed to unlink user: " + e.getMessage(), e);
        }
    }
}
