package com.hoodie.otti.dto.login;

import com.hoodie.otti.entity.login.User;

/**
 * 카카오 회원가입 요청 DTO 클래스
 */
public class UserKakaoSignupRequestDto {

    private String kakaoUserId; // 카카오 사용자 식별자
    private String nickname; // 닉네임
    private String userEmail; // 이메일
    private String userPassword; // 비밀번호

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
     * 추가 매개변수를 받는 생성자를 필요에 따라 추가할 수 있습니다.
     *
     * @param kakaoUserId 카카오 사용자 식별자
     * @param nickname 닉네임
     * @param userEmail 이메일
     * @param extraParam 추가 매개변수 예시
     */
    public UserKakaoSignupRequestDto(String kakaoUserId, String nickname, String userEmail, String extraParam) {
        this.kakaoUserId = kakaoUserId;
        this.nickname = nickname;
        this.userEmail = userEmail;
        // this.extraParam = extraParam; // 추가 매개변수 처리
    }

    public String getKakaoUserId() { return kakaoUserId; }

    public void setKakaoUserId(String kakaoUserId) { this.kakaoUserId = kakaoUserId; }

    public String getNickname() { return nickname; }

    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getUserEmail() { return userEmail; }

    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    /**
     * User 엔티티로 변환하는 메서드
     *
     * @return User 엔티티 객체
     */
    public User toEntity() {
        return new User(userEmail, kakaoUserId, nickname);
    }

    /**
     * Kakao 사용자 식별자를 반환하는 메서드
     *
     * @return Kakao 사용자 식별자
     */
    public String getUserKakaoIdentifier() {
        return kakaoUserId; // 예시로 kakaoUserId 필드를 리턴하도록 구현했습니다.
    }
}
