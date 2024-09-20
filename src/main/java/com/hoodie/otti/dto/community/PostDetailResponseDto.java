package com.hoodie.otti.dto.community;

import com.hoodie.otti.dto.profile.UserResponseDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostDetailResponseDto {

    private Long id;
    private String title;
    private String content;
    private List<ImageResponseDto> images;
    private List<CommentResponseDto> comments;
    private UserResponseDto userInfo;
    private Long potId;
    private String createdDate;

    @Builder
    public PostDetailResponseDto(Long id, String title, String content, List<ImageResponseDto> images,
                                 List<CommentResponseDto> comments, UserResponseDto userInfo, Long potId, String createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.images = images;
        this.comments = comments;
        this.userInfo = userInfo;
        this.potId = potId;
        this.createdDate = createdDate;
    }
}
