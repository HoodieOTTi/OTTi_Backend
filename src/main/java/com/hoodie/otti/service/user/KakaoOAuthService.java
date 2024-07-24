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
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoOAuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final String KAKAO_CLIENT_ID;
    private final String REDIRECT_URI;


    public KakaoOAuthService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider,
                             @Value("${spring.security.oauth2.client.registration.kakao.client-id}") String kakaoClientId,
                             @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}") String redirectUri) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.KAKAO_CLIENT_ID = kakaoClientId;
        this.REDIRECT_URI = redirectUri;
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
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestParams, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(reqURL, HttpMethod.POST, requestEntity, String.class);
        JsonElement element = JsonParser.parseString(Objects.requireNonNull(responseEntity.getBody())).getAsJsonObject();

        String accessToken = element.getAsJsonObject().get("access_token").getAsString();
        String refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

        return new KakaoTokenDto(accessToken, refreshToken);
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
}
