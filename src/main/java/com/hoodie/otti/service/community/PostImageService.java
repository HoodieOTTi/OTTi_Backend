package com.hoodie.otti.service.community;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.hoodie.otti.dto.community.ImageResponseDto;
import com.hoodie.otti.dto.community.UploadImageRequestDto;
import com.hoodie.otti.model.community.Image;
import com.hoodie.otti.repository.community.ImageRepository;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class PostImageService {

    private final AmazonS3 amazonS3;
    private final ImageRepository imageRepository;
    private final String bucket;

    public PostImageService(AmazonS3 amazonS3, ImageRepository imageRepository,
                            @Value("${cloud.aws.s3.bucket}") String bucket) {
        this.amazonS3 = amazonS3;
        this.imageRepository = imageRepository;
        this.bucket = bucket;
    }

    @Transactional
    public ImageResponseDto saveImage(final UploadImageRequestDto requestDto) throws IOException {
        final String originName = requestDto.getImage().getOriginalFilename();
        final String ext = originName.substring(originName.lastIndexOf("."));
        final String changedImageName = changeImageName(ext);

        final String storeImagePath = uploadImage(requestDto.getImage(), ext, changedImageName);

        Image image = Image.builder()
                .imageName(changedImageName)
                .imageUrl(storeImagePath)
                .build();
        imageRepository.save(image);

        return new ImageResponseDto(image.getId(), image.getImageUrl());
    }

    private String uploadImage(final MultipartFile image,
                               final String ext,
                               final String changedImageName) throws IOException {
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + ext.substring(1));

        try {
            amazonS3.putObject(new PutObjectRequest(
                    bucket, changedImageName, image.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (final IOException e) {
            throw e;
        }

        return amazonS3.getUrl(bucket, changedImageName).toString();
    }

    private String changeImageName(final String ext) {
        final String uuid = UUID.randomUUID().toString();
        return uuid + ext;
    }
}
