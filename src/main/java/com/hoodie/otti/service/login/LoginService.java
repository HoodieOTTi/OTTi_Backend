package com.hoodie.otti.service.login;

import com.hoodie.otti.dto.login.UserKakaoLoginResponseDto;
import com.hoodie.otti.dto.login.UserResponseDto;
import com.hoodie.otti.dto.login.UserKakaoSignupRequestDto;
import com.hoodie.otti.util.login.JwtTokenProvider;
import com.hoodie.otti.repository.login.UserRepository;
import com.hoodie.otti.entity.login.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Optional;

@Service
public class LoginService {

    @Value("${kakao.client_id}")
    private String kakaoClientId;

    @Value("${kakao.redirect_uri}")
    private String kakaoRedirectUri;

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate;

    public LoginService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.restTemplate = restTemplate;
    }

    /**
     * 카카오 로그인 처리 메서드
     *
     * @param access_Token 카카오 Access Token
     * @return UserKakaoLoginResponseDto 객체
     * @throws BaseException 사용자 저장에 실패할 경우 예외 처리
     */
    @Transactional
    public UserKakaoLoginResponseDto kakaoLogin(String access_Token) throws BaseException {
        try {
            HashMap<String, String> userInfo = getUserKaKaoAccessInfo(access_Token);
            UserResponseDto userResponseDto = findByUserKakaoIdentifier(userInfo.get("id"));

            if (userResponseDto == null) {
                UserKakaoSignupRequestDto userKakaoSignupRequestDto = new UserKakaoSignupRequestDto(
                        userInfo.get("id"),
                        userInfo.get("nickname"),
                        userInfo.get("email")
                );
                signUp(userKakaoSignupRequestDto);
                userResponseDto = findByUserKakaoIdentifier(userKakaoSignupRequestDto.getUserKakaoIdentifier());
            }

            String token = jwtTokenProvider.createToken(userResponseDto.getUserEmail());
            return new UserKakaoLoginResponseDto(HttpStatus.OK, token, userResponseDto.getUserEmail());
        } catch (Exception e) {
            throw new BaseException(BaseResponseCode.FAILED_TO_SAVE_USER);
        }
    }

    /**
     * Kakao OAuth 인증 코드를 사용하여 Kakao Access Token을 얻는 메서드
     *
     * @param code Kakao OAuth 인증 코드
     * @return Kakao Access Token
     */
    public String getKaKaoAccessToken(String code) {
        String accessToken = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", kakaoClientId);
            params.add("redirect_uri", kakaoRedirectUri);
            params.add("code", code);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(reqURL, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonElement element = JsonParser.parseString(response.getBody());
                accessToken = element.getAsJsonObject().get("access_token").getAsString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    /**
     * Kakao API를 사용하여 사용자 정보를 가져오는 메서드
     *
     * @param accessToken Kakao Access Token
     * @return 사용자 정보를 담은 HashMap
     */
    public HashMap<String, String> getUserKaKaoAccessInfo(String accessToken) {
        HashMap<String, String> userInfo = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity<String> request = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(reqURL, HttpMethod.GET, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonElement element = JsonParser.parseString(response.getBody());
                String id = element.getAsJsonObject().get("id").getAsString();
                JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
                String nickname = properties.getAsJsonObject().get("nickname").getAsString();

                JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
                if (kakaoAccount.has("email")) {
                    String email = kakaoAccount.getAsJsonObject().get("email").getAsString();
                    userInfo.put("email", email);
                }

                userInfo.put("nickname", nickname);
                userInfo.put("id", id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userInfo;
    }

    /**
     * Kakao 사용자 식별자로 사용자 조회하는 메서드
     *
     * @param kakaoUserId Kakao 사용자 식별자
     * @return 사용자 정보를 담은 UserResponseDto 객체
     */
    public UserResponseDto findByUserKakaoIdentifier(String kakaoUserId) {
        Optional<User> userOptional = userRepository.findByKakaoUserId(kakaoUserId);
        return userOptional.map(UserResponseDto::new).orElse(null);
    }

    /**
     * Kakao 회원가입 처리하는 메서드
     *
     * @param userKakaoSignupRequestDto Kakao 회원가입 요청 DTO
     * @return 저장된 회원의 ID
     * @throws BaseException 사용자 저장에 실패할 경우 예외 처리
     */
    @Transactional
    public Long signUp(UserKakaoSignupRequestDto userKakaoSignupRequestDto) throws BaseException {
        try {
            return userRepository.save(userKakaoSignupRequestDto.toEntity()).getUserId();
        } catch (Exception e) {
            throw new BaseException(BaseResponseCode.FAILED_TO_SAVE_USER);
        }
    }

    // 예외 처리를 위한 BaseException 클래스
    public static class BaseException extends Exception {
        private static final long serialVersionUID = 1L;
        private final BaseResponseCode responseCode;

        public BaseException(BaseResponseCode responseCode) {
            super(responseCode.getMessage());
            this.responseCode = responseCode;
        }

        public BaseResponseCode getResponseCode() {
            return responseCode;
        }
    }

    // 예외 코드 정의
    public enum BaseResponseCode {
        FAILED_TO_SAVE_USER("사용자 저장에 실패했습니다.");

        private final String message;

        BaseResponseCode(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
