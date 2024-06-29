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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;

@Service
public class LoginService {

    private final String clientId = "여기에_클라이언트_ID_입력";
    private final String redirectUri = "여기에_리다이렉트_URI_입력";

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public LoginService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 카카오 로그인 처리 메서드
     *
     * @param access_Token 카카오 Access Token
     * @return UserKakaoLoginResponseDto 객체
     */
    public UserKakaoLoginResponseDto kakaoLogin(String access_Token) throws BaseException {
        try {
            // 카카오 로그인 로직 구현
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
     * Kakao Access Token을 얻는 메서드
     *
     * @param code Kakao 인증 코드
     * @return Kakao Access Token
     */
    public String getKaKaoAccessToken(String code) {
        String accessToken = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // POST 요청 설정 및 출력 사용 가능하도록 설정
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // POST 파라미터 작성
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=").append(clientId);
            sb.append("&redirect_uri=").append(redirectUri);
            sb.append("&code=").append(code);
            bw.write(sb.toString());
            bw.flush();

            // 응답 코드가 200인 경우 (성공)
            int responseCode = conn.getResponseCode();
            System.out.println("응답 코드 : " + responseCode);

            // JSON 응답 읽기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            System.out.println("응답 본문 : " + result);

            // Gson 라이브러리의 JsonParser 클래스로 JSON 파싱
            JsonElement element = JsonParser.parseString(result.toString());

            // JSON에서 access_token 추출
            accessToken = element.getAsJsonObject().get("access_token").getAsString();

            System.out.println("access_token : " + accessToken);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    /**
     * Kakao에서 사용자 정보를 얻는 메서드
     *
     * @param accessToken Kakao Access Token
     * @return 사용자 정보를 담은 HashMap
     */
    public HashMap<String, String> getUserKaKaoAccessInfo(String accessToken) {
        HashMap<String, String> userInfo = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // GET 요청 설정
            conn.setRequestMethod("GET");

            // 요청에 필요한 헤더 설정
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            // 응답 코드 확인
            int responseCode = conn.getResponseCode();
            System.out.println("응답 코드 : " + responseCode);

            // 응답 본문 읽기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            System.out.println("응답 본문 : " + result);

            // Gson 라이브러리의 JsonParser 클래스로 JSON 파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result.toString());

            // JSON에서 필요한 사용자 정보 추출
            String id = element.getAsJsonObject().get("id").getAsString();
            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            String nickname = properties.getAsJsonObject().get("nickname").getAsString();

            // kakao_account JSON에서 이메일 유무 확인
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
            if (kakaoAccount.has("email")) {
                String email = kakaoAccount.getAsJsonObject().get("email").getAsString();
                userInfo.put("email", email);
            }

            // 추출한 정보를 userInfo HashMap에 저장
            userInfo.put("nickname", nickname);
            userInfo.put("id", id);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return userInfo;
    }

    /**
     * 회원가입 메서드
     *
     * @param userKakaoSignupRequestDto Kakao 회원가입 요청 DTO
     * @return 저장된 회원의 ID
     */
    @Transactional
    public Long signUp(UserKakaoSignupRequestDto userKakaoSignupRequestDto) throws BaseException {
        try {
            return userRepository.save(userKakaoSignupRequestDto.toEntity()).getUserId();
        } catch (Exception e) {
            throw new BaseException(BaseResponseCode.FAILED_TO_SAVE_USER);
        }
    }


    /**
     * 사용자 식별자로 사용자 조회하는 메서드
     *
     * @param kakaoUserId Kakao 사용자 식별자
     * @return 사용자 정보를 담은 UserResponseDto 객체
     */
    public UserResponseDto findByUserKakaoIdentifier(String kakaoUserId) {
        Optional<User> userOptional = userRepository.findByKakaoUserId(kakaoUserId);
        return userOptional.map(UserResponseDto::new).orElse(null);
    }

    /**
     * JWT 토큰 생성하는 메서드
     *
     * @param userEmail 사용자 이메일
     * @return 생성된 JWT 토큰
     */
    public String generateToken(String userEmail) {
        return jwtTokenProvider.createToken(userEmail);
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

    // 개인 정보를 기반으로 Kakao 회원 가입 요청 DTO 반환하는 메서드
    private UserKakaoSignupRequestDto getUserKakaoSignupRequestDto(HashMap<String, Object> userInfo) {
        String userPassword = "-1";
        String kakaoUserId = userInfo.get("id") != null ? userInfo.get("id").toString() : null;
        String nickname = userInfo.get("nickname") != null ? userInfo.get("nickname").toString() : null;
        String userEmail = userInfo.get("email") != null ? userInfo.get("email").toString() : null;

        return new UserKakaoSignupRequestDto(userEmail, nickname, userPassword, kakaoUserId);
    }

    // 응답 DTO 생성하는 메서드
    private UserKakaoLoginResponseDto createUserKakaoLoginResponseDto(HashMap<String, String> userInfo) {
        String userEmail = userInfo.get("email"); // 사용자 이메일을 가져옵니다. userInfo에 이메일이 있다고 가정합니다.
        String token = generateToken(userEmail); // 토큰을 생성합니다. 이 부분은 토큰을 생성하는 방법에 맞게 구현되어야 합니다.
        HttpStatus status = HttpStatus.OK; // 상태는 OK로 가정합니다.

        return new UserKakaoLoginResponseDto(status, token, userEmail);
    }
}
