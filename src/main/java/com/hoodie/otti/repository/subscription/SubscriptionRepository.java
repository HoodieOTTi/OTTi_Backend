package com.hoodie.otti.repository.subscription;

import com.hoodie.otti.entity.subscripition.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}
