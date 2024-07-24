package com.hoodie.otti.dto.login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private Long userId;
    private String password;
    private String nickname;
    private String email;

    // Constructors
    public RegisterRequest() {
    }

    public RegisterRequest(Long userId, String password, String nickname, String email) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
    }

    public RegisterRequest(Long userId, String nickname, String email) {
        this.userId = userId;
        this.nickname = nickname;
        this.email = email;
    }
}

