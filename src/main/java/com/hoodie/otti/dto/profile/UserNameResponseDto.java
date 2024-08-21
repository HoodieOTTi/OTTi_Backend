package com.hoodie.otti.dto.profile;

import lombok.Getter;

@Getter
public class UserNameResponseDto {

    private Long id;
    private String userName;

    public UserNameResponseDto(Long id, String userName) {
        this.id = id;
        this.userName = userName;
    }
}
