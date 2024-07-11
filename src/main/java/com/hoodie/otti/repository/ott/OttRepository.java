package com.hoodie.otti.repository.ott;

import com.hoodie.otti.model.ott.Ott;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OttRepository extends JpaRepository<Ott, Long> {

    Optional<Ott> findOttByNameAndAndRatePlan(String ottName, String ottRatePlan);
}
