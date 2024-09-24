package com.hoodie.otti.repository.community;

import com.hoodie.otti.model.community.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Integer countByPost_Id(Long postId);
}
