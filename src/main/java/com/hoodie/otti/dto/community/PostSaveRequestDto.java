package com.hoodie.otti.dto.community;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto {

    private String title;
    private String content;
    private List<Long> images;
    private Long userId;
    private Long ottId;

    @Builder
    public PostSaveRequestDto(String title, String content, List<Long> images, Long userId, Long ottId) {
        this.title = title;
        this.content = content;
        this.images = images;
        this.userId = userId;
        this.ottId = ottId;
    }
}
