package com.hoodie.otti.dto.community;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateRequestDto {

    private String title;
    private String content;
    private List<Long> images;
    private Long ottId;

    @Builder
    public PostUpdateRequestDto(String title, String content, List<Long> images, Long ottId) {
        this.title = title;
        this.content = content;
        this.images = images;
        this.ottId = ottId;
    }
}
