package com.hoodie.otti.dto.community;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponsePageDto {
    private List<PostResponseDto> contents;
    private Integer currentPage;
    private Integer size;
    private Integer totalPages;
    private Long totalElements;

    @Builder
    public PostResponsePageDto(List<PostResponseDto> contents, Integer currentPage, Integer size,
                               Integer totalPages, Long totalElements) {
        this.contents = contents;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.size = size;
    }
}
