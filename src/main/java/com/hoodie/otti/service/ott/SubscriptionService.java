package com.hoodie.otti.service.ott;

import com.hoodie.otti.dto.ott.SubscriptionSaveRequestDto;
import com.hoodie.otti.entity.ott.Ott;
import com.hoodie.otti.entity.subscripition.Subscription;
import com.hoodie.otti.entity.profile.UserProfile;
import com.hoodie.otti.repository.ott.OttRepository;
import com.hoodie.otti.repository.subscription.SubscriptionRepository;
import com.hoodie.otti.repository.profile.UserProfileRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserProfileRepository userProfileRepository;
    private final OttRepository ottRepository;

    @Transactional
    public Long save(SubscriptionSaveRequestDto requestDto) {
        UserProfile user = userProfileRepository.findById(requestDto.getUserProfileId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        Ott ott = ottRepository.findById(requestDto.getOttId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid ott ID"));

        Subscription subscription = Subscription.builder()
                .payment(requestDto.getPayment())
                .memo(requestDto.getMemo())
                .paymentDate(requestDto.getPaymentDate())
                .userProfileId(user)
                .ottId(ott)
                .build();

        return subscriptionRepository.save(subscription).getId();
    }

    public List<Subscription> findAll() {
        return subscriptionRepository.findAll();
    }

    public Subscription findById(Long id) throws IllegalArgumentException {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 구독 정보가 없습니다. id=" + id));
    }
}
