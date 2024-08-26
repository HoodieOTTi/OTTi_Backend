package com.hoodie.otti.dto.community;

import com.hoodie.otti.dto.profile.UserResponseDto;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostDetailResponseDto {

    private Long id;
    private String title;
    private String content;
    private Integer viewCount;
    private List<ImageResponseDto> images;
    private UserResponseDto userInfo;
    private Long potId;
    private Date createdDate;

    @Builder
    public PostDetailResponseDto(Long id, String title, String content, Integer viewCount, List<ImageResponseDto> images,
                                 UserResponseDto userInfo, Long potId, Date createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.images = images;
        this.userInfo = userInfo;
        this.potId = potId;
        this.createdDate = createdDate;
    }
}
