package com.hoodie.otti.dto.login;

import com.hoodie.otti.entity.login.User;

/**
 * 사용자 응답 DTO 클래스
 */
public class UserResponseDto {

    private String userEmail;
    private String kakaoUserId;
    private String nickname;

    // 기본 생성자
    public UserResponseDto() {}

    // 모든 필드를 매개변수로 받는 생성자
    public UserResponseDto(String userEmail, String kakaoUserId, String nickname) {
        this.userEmail = userEmail;
        this.kakaoUserId = kakaoUserId;
        this.nickname = nickname;
    }

    // User 엔티티를 받아서 UserResponseDto로 변환하는 생성자
    public UserResponseDto(User user) {
        this.userEmail = user.getUserEmail();
        this.kakaoUserId = user.getKakaoUserId();
        this.nickname = user.getNickname();
    }

    // userEmail 필드의 getter 메서드
    public String getUserEmail() {
        return userEmail;
    }

    // kakaoUserId 필드의 getter 메서드
    public String getKakaoUserId() {
        return kakaoUserId;
    }

    // nickname 필드의 getter 메서드
    public String getNickname() {
        return nickname;
    }
}
