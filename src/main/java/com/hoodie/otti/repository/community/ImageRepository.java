package com.hoodie.otti.repository.community;

import com.hoodie.otti.model.community.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByPostId_Id(Long postId);
    List<Image> findAllByPostIsNull();
}
