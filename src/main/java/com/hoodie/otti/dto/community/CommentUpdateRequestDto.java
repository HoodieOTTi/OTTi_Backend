package com.hoodie.otti.dto.community;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentUpdateRequestDto {

    private String text;

    public CommentUpdateRequestDto(String text) {
        this.text = text;
    }
}
