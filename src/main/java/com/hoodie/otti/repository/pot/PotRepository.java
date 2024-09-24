package com.hoodie.otti.repository.pot;

import com.hoodie.otti.model.pot.Pot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PotRepository extends JpaRepository<Pot, Long> {
    Optional<Pot> findById(Long id);
}
