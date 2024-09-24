package com.hoodie.otti.service.community;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.hoodie.otti.dto.community.ImageResponseDto;
import com.hoodie.otti.dto.community.ProfileImageResponseDto;
import com.hoodie.otti.dto.community.UploadImageRequestDto;
import com.hoodie.otti.model.community.Image;
import com.hoodie.otti.repository.community.ImageRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ImageService {

    private final AmazonS3 amazonS3;
    private final ImageRepository imageRepository;
    private final String bucket;

    public ImageService(AmazonS3 amazonS3, ImageRepository imageRepository,
                        @Value("${cloud.aws.s3.bucket}") String bucket) {
        this.amazonS3 = amazonS3;
        this.imageRepository = imageRepository;
        this.bucket = bucket;
    }

    @Transactional
    public ImageResponseDto savePostImage(final UploadImageRequestDto requestDto) throws IOException {
        final String originName = requestDto.getImage().getOriginalFilename();
        final String ext = originName.substring(originName.lastIndexOf("."));
        final String changedImageName = changeImageName(ext);

        final String storeImagePath = uploadImage(requestDto.getImage(), ext, "post/" + changedImageName);

        Image image = Image.builder()
                .imageName(changedImageName)
                .imageUrl(storeImagePath)
                .build();
        imageRepository.save(image);

        return new ImageResponseDto(image.getId(), image.getImageUrl());
    }

    @Transactional
    public ProfileImageResponseDto saveProfileImage(final UploadImageRequestDto requestDto) throws IOException {
        final String originName = requestDto.getImage().getOriginalFilename();
        final String ext = originName.substring(originName.lastIndexOf("."));
        final String changedImageName = changeImageName(ext);

        final String storeImagePath = uploadImage(requestDto.getImage(), ext, "profile/" + changedImageName);

        return new ProfileImageResponseDto(storeImagePath);
    }

    @Scheduled(cron = "${cloud.aws.cron}")
    @Transactional
    public void deleteUnNecessaryImage() {

        List<Image> images = imageRepository.findAllByPostIsNull();

        images.stream()
                .filter(image -> image.getCreatedDate().plusHours(24).isBefore(LocalDateTime.now()))
                .forEach(image -> {
                    deletePostImage(image.getImageName());
                    imageRepository.delete(image);
                });
    }

    private void deletePostImage(String imageName) {
        String fullPath = "post/" + imageName;
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fullPath));
    }

    public void deleteProfileImage(String imageUrl) {
        String defaultImage ="profile/otti.png";
        String filePath = imageUrl.substring(imageUrl.indexOf("profile/"));

        if (!defaultImage.equals(filePath)) {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, filePath));
        }
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
