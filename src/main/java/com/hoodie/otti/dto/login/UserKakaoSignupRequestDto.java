package com.hoodie.otti.dto.login;

import com.hoodie.otti.entity.login.User;

/**
 * 카카오 회원가입 요청 DTO 클래스
 */
public class UserKakaoSignupRequestDto {

    private String kakaoUserId; // 카카오 사용자 식별자
    private String nickname; // 닉네임
    private String userEmail; // 이메일

    /**
     * 기본 생성자
     */
    public UserKakaoSignupRequestDto() {
    }

    /**
     * 필수 매개변수를 받는 생성자
     *
     * @param kakaoUserId 카카오 사용자 식별자
     * @param nickname 닉네임
     * @param userEmail 이메일
     */
    public UserKakaoSignupRequestDto(String kakaoUserId, String nickname, String userEmail) {
        this.kakaoUserId = kakaoUserId;
        this.nickname = nickname;
        this.userEmail = userEmail;
    }

    /**
     * 사용자 이메일이나 닉네임이 null이거나 빈 문자열인 경우 예외를 발생시킵니다.
     */
    private void validateFields() {
        if (userEmail == null || userEmail.isEmpty()) {
            throw new IllegalArgumentException("사용자 이메일은 필수 입력 항목입니다.");
        }
        if (nickname == null || nickname.isEmpty()) {
            throw new IllegalArgumentException("사용자 닉네임은 필수 입력 항목입니다.");
        }
    }

    public String getKakaoUserId() {
        return kakaoUserId;
    }

    public void setKakaoUserId(String kakaoUserId) {
        this.kakaoUserId = kakaoUserId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * User 엔티티로 변환하는 메서드
     *
     * @return User 엔티티 객체
     */
    public User toEntity() {
        validateFields(); // 필드 유효성 검사

        return new User(userEmail, kakaoUserId, nickname);
    }

    /**
     * Kakao 사용자 식별자를 반환하는 메서드
     *
     * @return Kakao 사용자 식별자
     */
    public String getUserKakaoIdentifier() {
        return kakaoUserId;
    }
}
