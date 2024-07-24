package com.hoodie.otti.dto.login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberResponse {
    private Long id;
    private String nickname;
    private String email;

    public MemberResponse(Long id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }
}

