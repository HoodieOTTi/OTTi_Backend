package com.hoodie.otti.entity.login;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * 사용자 엔티티 클래스
 * 이 클래스는 데이터베이스에서 사용자 정보를 관리하기 위한 엔티티입니다.
 * 사용자의 식별자(userId), 이메일(userEmail), 카카오 사용자 ID(kakaoUserId), 닉네임(nickname) 등의 정보를 저장합니다.
 */
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;        // 사용자 식별자

    @NotEmpty(message = "이메일은 필수 입력 사항입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String userEmail;   // 사용자 이메일

    private String kakaoUserId; // 카카오 사용자 ID

    @NotEmpty(message = "닉네임은 필수 입력 사항입니다.")
    @Size(min = 3, max = 50, message = "닉네임은 최소 3자에서 최대 50자여야 합니다.")
    private String nickname;    // 사용자 닉네임

    // 기본 생성자
    public User() {}

    /**
     * 생성자
     *
     * @param userEmail   사용자 이메일
     * @param kakaoUserId 카카오 사용자 ID
     * @param nickname    사용자 닉네임
     */
    public User(String userEmail, String kakaoUserId, String nickname) {
        this.userEmail = userEmail;
        this.kakaoUserId = kakaoUserId;
        this.nickname = nickname;
    }

    // userId 필드의 getter 메서드
    public Long getUserId() {
        return userId;
    }

    // userId 필드의 setter 메서드
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // userEmail 필드의 getter 메서드
    public String getUserEmail() {
        return userEmail;
    }

    // userEmail 필드의 setter 메서드
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    // kakaoUserId 필드의 getter 메서드
    public String getKakaoUserId() {
        return kakaoUserId;
    }

    // kakaoUserId 필드의 setter 메서드
    public void setKakaoUserId(String kakaoUserId) {
        this.kakaoUserId = kakaoUserId;
    }

    // nickname 필드의 getter 메서드
    public String getNickname() {
        return nickname;
    }

    // nickname 필드의 setter 메서드
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    // 추가적인 도메인 로직이 필요한 경우 여기에 추가할 수 있습니다.
}
