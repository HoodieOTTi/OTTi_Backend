package com.hoodie.otti.repository.community;

import com.hoodie.otti.model.community.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByUserId_Id(Long userId, Pageable pageable);
    Page<Post> findByTitleContaining(String title, Pageable pageable);
    @Query("SELECT p FROM Post p JOIN p.pot pot JOIN pot.ottId o WHERE o.name = :ottName")
    Page<Post> findByOttName(@Param("ottName") String ottName, Pageable pageable);
}
