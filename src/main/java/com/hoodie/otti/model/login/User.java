package com.hoodie.otti.model.login;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;        // 사용자 식별자

    @NotEmpty(message = "이메일은 필수 입력 사항입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String userEmail;   // 사용자 이메일

    @NotEmpty(message = "닉네임은 필수 입력 사항입니다.")
    @Size(min = 3, max = 50, message = "닉네임은 최소 3자에서 최대 50자여야 합니다.")
    private String nickname;    // 사용자 닉네임

    // 기본 생성자
    public User() {}

    public User(Long userId){
        this.userId = userId;
    }

    public User(String userEmail){
        this.userEmail = userEmail;
    }

}
