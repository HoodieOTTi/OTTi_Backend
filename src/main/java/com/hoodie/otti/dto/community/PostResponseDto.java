package com.hoodie.otti.dto.community;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private Integer viewCount;
    private String userName;
    private String ottImage;
    private Date createdDate;

    @Builder
    public PostResponseDto(Long id, String title, String content, Integer viewCount,
                           String userName, String ottImage, Date createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.userName = userName;
        this.ottImage = ottImage;
        this.createdDate = createdDate;
    }
}
