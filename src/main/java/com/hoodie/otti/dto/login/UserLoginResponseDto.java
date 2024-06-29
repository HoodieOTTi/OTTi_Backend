package com.hoodie.otti.dto.login;

import org.springframework.http.HttpStatus;

/**
 * 사용자 로그인 응답 DTO 클래스
 */
public class UserLoginResponseDto {

    private HttpStatus status; // HTTP 상태 코드
    private String token; // JWT 토큰
    private String userEmail; // 사용자 이메일

    /**
     * 기본 생성자
     */
    public UserLoginResponseDto() {
    }

    /**
     * 모든 필드를 매개변수로 받는 생성자
     *
     * @param status HTTP 상태 코드
     * @param token JWT 토큰
     * @param userEmail 사용자 이메일
     */
    public UserLoginResponseDto(HttpStatus status, String token, String userEmail) {
        this.status = status;
        this.token = token;
        this.userEmail = userEmail;
    }

    // getter와 setter 메서드
    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
