package com.hoodie.otti.dto.community;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequestDto {

    private String title;
    private String content;
    private List<Long> images;
    private Long potId;

    @Builder
    public PostRequestDto(String title, String content, List<Long> images, Long potId) {
        this.title = title;
        this.content = content;
        this.images = images;
        this.potId = potId;
    }
}
