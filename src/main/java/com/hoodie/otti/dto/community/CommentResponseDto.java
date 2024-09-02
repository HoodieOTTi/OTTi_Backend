package com.hoodie.otti.dto.community;

import com.hoodie.otti.dto.profile.UserResponseDto;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private Long id;
    private String text;
    private String createdDate;
    private String modifiedDate;
    private UserResponseDto userName;

    public CommentResponseDto(Long id, String text, UserResponseDto userName, String createdDate, String modifiedDate) {
        this.id = id;
        this.text = text;
        this.userName = userName;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate; // 형식 변환
    }
}
