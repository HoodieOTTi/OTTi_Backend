package com.hoodie.otti.dto.community;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UploadImageRequestDto {

    private MultipartFile image;

    public UploadImageRequestDto(MultipartFile image) {
        this.image = image;
    }
}
