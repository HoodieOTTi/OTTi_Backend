package com.hoodie.otti.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hoodie.otti.dto.login.KakaoTokenDto;
import com.hoodie.otti.dto.login.ServiceTokenDto;
import com.hoodie.otti.dto.login.UserDto;
import com.hoodie.otti.model.profile.User;
import com.hoodie.otti.repository.profile.UserRepository;
import com.hoodie.otti.util.login.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

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

    public KakaoTokenDto getKakaoToken(String code) {
        String reqURL = "https://kauth.kakao.com/oauth/token";

        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("grant_type", "authorization_code");
        requestParams.add("client_id", KAKAO_CLIENT_ID);
        requestParams.add("redirect_uri", REDIRECT_URI);
        requestParams.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestParams, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(reqURL, HttpMethod.POST, requestEntity, String.class);
        JsonElement element = JsonParser.parseString(Objects.requireNonNull(responseEntity.getBody())).getAsJsonObject();

        String accessToken = element.getAsJsonObject().get("access_token").getAsString();
        String refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();
        String tokenType = element.getAsJsonObject().get("token_type").getAsString();
        int expiresIn = element.getAsJsonObject().get("expires_in").getAsInt();
        String scope = element.getAsJsonObject().get("scope").getAsString();

        return new KakaoTokenDto(accessToken, refreshToken, tokenType, expiresIn, scope);
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
                System.out.println("Successfully unlinked user: " + response.getBody());
            } else {
                throw new RuntimeException("Failed to unlink user: " + response.getBody());
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Failed to unlink user: " + e.getMessage(), e);
        }
    }
}
