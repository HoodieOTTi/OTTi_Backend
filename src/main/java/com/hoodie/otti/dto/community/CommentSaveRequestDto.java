package com.hoodie.otti.dto.community;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

@Getter
@NoArgsConstructor
public class CommentSaveRequestDto {

    private String text;
    private String createdDate;
    private String modifiedDate;
    private Long post;

    public CommentSaveRequestDto(String text, Long post) {
        this.text = text;
        this.post = post;
        this.createdDate = LocalDateTime.now().toString(DateTimeFormat.forPattern("yyyy.MM.dd HH:mm"));
        this.modifiedDate = LocalDateTime.now().toString(DateTimeFormat.forPattern("yyyy.MM.dd HH:mm"));
    }
}
