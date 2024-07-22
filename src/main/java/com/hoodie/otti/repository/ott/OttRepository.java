package com.hoodie.otti.repository.ott;

import com.hoodie.otti.entity.ott.Ott;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OttRepository extends JpaRepository<Ott, Long> {
}
