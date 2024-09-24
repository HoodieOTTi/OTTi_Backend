package com.hoodie.otti.dto.community;

import com.hoodie.otti.dto.profile.UserResponseDto;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private Long id;
    private String text;
    private String createdDate;
    private String modifiedDate;
    private UserResponseDto userInfo;

    public CommentResponseDto(Long id, String text, UserResponseDto userInfo, String createdDate, String modifiedDate) {
        this.id = id;
        this.text = text;
        this.userInfo = userInfo;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate; // 형식 변환
    }
}
