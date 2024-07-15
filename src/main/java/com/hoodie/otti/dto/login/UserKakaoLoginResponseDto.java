package com.hoodie.otti.dto.login;

import org.springframework.http.HttpStatus;

import java.util.Objects;

/**
 * 카카오 로그인 응답 DTO 클래스
 */
public class UserKakaoLoginResponseDto {

    private final HttpStatus status; // HTTP 상태 코드
    private final String token; // JWT 토큰
    private final String userEmail; // 사용자 이메일

    /**
     * 생성자
     *
     * @param status    HTTP 상태 코드
     * @param token     JWT 토큰
     * @param userEmail 사용자 이메일
     */
    public UserKakaoLoginResponseDto(HttpStatus status, String token, String userEmail) {
        this.status = status;
        this.token = token;
        this.userEmail = userEmail;
    }

    /**
     * HTTP 상태 코드 반환
     *
     * @return HTTP 상태 코드
     */
    public HttpStatus getStatus() {
        return status;
    }

    /**
     * JWT 토큰 반환
     *
     * @return JWT 토큰
     */
    public String getToken() {
        return token;
    }

    /**
     * 사용자 이메일 반환
     *
     * @return 사용자 이메일
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * 객체 정보를 문자열로 반환
     *
     * @return 객체 정보 문자열
     */
    @Override
    public String toString() {
        return "UserKakaoLoginResponseDto{" +
                "status=" + status +
                ", token='" + token + '\'' +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }

    /**
     * 객체 동등성 비교
     *
     * @param o 비교할 객체
     * @return 동등성 여부
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserKakaoLoginResponseDto that = (UserKakaoLoginResponseDto) o;
        return status == that.status &&
                token.equals(that.token) &&
                userEmail.equals(that.userEmail);
    }

    /**
     * 객체 해시코드 생성
     *
     * @return 해시코드
     */
    @Override
    public int hashCode() {
        return Objects.hash(status, token, userEmail);
    }
}
