package com.hoodie.otti.dto.community;

import com.hoodie.otti.dto.ott.OttBaseResponseDto;
import com.hoodie.otti.dto.profile.UserNameResponseDto;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private Integer viewCount;
    private List<ImageResponseDto> images;
    private UserNameResponseDto userInfo;
    private OttBaseResponseDto ottInfo;
    private Date createdDate;
    private Date modifiedDate;

    @Builder
    public PostResponseDto(Long id, String title, String content, Integer viewCount, List<ImageResponseDto> images,
                           UserNameResponseDto userInfo, OttBaseResponseDto ottInfo, Date createdDate, Date modifiedDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.images = images;
        this.userInfo = userInfo;
        this.ottInfo = ottInfo;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
