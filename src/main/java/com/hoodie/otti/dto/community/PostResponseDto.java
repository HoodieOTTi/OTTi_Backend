package com.hoodie.otti.dto.community;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer commentCount;
    private String userName;
    private String ottImage;
    private String createdDate;

    @Builder
    public PostResponseDto(Long id, String title, String content, Integer viewCount, Integer commentCount,
                           String userName, String ottImage, String createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.userName = userName;
        this.ottImage = ottImage;
        this.createdDate = createdDate;
    }
}
