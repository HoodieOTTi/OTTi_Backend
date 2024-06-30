package com.hoodie.otti.service.ott;

import com.hoodie.otti.dto.ott.SubscriptionSaveRequestDto;
import com.hoodie.otti.model.Ott;
import com.hoodie.otti.model.Subscription;
import com.hoodie.otti.model.User;
import com.hoodie.otti.repository.UserRepository;
import com.hoodie.otti.repository.ott.OttRepository;
import com.hoodie.otti.repository.ott.SubscriptionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final OttRepository ottRepository;

    @Transactional
    public Long save(SubscriptionSaveRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        Ott ott = ottRepository.findById(requestDto.getOttId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid ott ID"));

        Subscription subscription = Subscription.builder()
                .payment(requestDto.getPayment())
                .memo(requestDto.getMemo())
                .paymentDate(requestDto.getPaymentDate())
                .userId(user)
                .ottId(ott)
                .build();

        return subscriptionRepository.save(subscription).getId();
    }

    public List<Subscription> findAll() {
        return subscriptionRepository.findAll();
    }

    public Subscription findById(Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 구독 정보가 없습니다. id=" + id));
    }
}
