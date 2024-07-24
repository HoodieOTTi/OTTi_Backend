package com.hoodie.otti.repository.ott;

import com.hoodie.otti.model.ott.Subscription;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByUserId_Id(Long userId);
}
