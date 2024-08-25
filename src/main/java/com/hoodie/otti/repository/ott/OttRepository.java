package com.hoodie.otti.repository.ott;

import com.hoodie.otti.model.ott.Ott;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OttRepository extends JpaRepository<Ott, Long> {

    Optional<Ott> findOttByNameAndAndRatePlan(String ottName, String ottRatePlan);
    Optional<Ott> findById(Long id); // ID로 OTT 조회// 이름으로 OTT 조회
}
