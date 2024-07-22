package com.hoodie.otti.service.ott;

import com.hoodie.otti.dto.ott.SubscriptionSaveRequestDto;
import com.hoodie.otti.dto.ott.SubscriptionUpdateRequestDto;
import com.hoodie.otti.model.ott.Ott;
import com.hoodie.otti.model.ott.Subscription;
import com.hoodie.otti.model.profile.UserProfile;
import com.hoodie.otti.repository.ott.OttRepository;
import com.hoodie.otti.repository.ott.SubscriptionRepository;
import com.hoodie.otti.repository.profile.UserProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

        Ott ott = ottRepository.findOttByNameAndAndRatePlan(requestDto.getOttName(), requestDto.getOttRatePlan())
                .orElseThrow(() -> new EntityNotFoundException("해당 OTT 정보를 찾을 수 없습니다."));

        Subscription subscription = Subscription.builder().name(requestDto.getName())
                .payment(requestDto.getPayment())
                .memo(requestDto.getMemo())
                .paymentDate(requestDto.getPaymentDate())
                .userProfile(user)
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

    public List<Subscription> findAllByUserId(Long userId) {
        return subscriptionRepository.findByUserProfile_Id(userId);
    }

    public Integer calculateDDay(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 구독 정보가 없습니다. id=" + id));

        Integer paymentDate = subscription.getPaymentDate();

        LocalDate now = LocalDate.now();
        int currentDayOfMonth = now.getDayOfMonth();

        if (paymentDate >= currentDayOfMonth) {
            return paymentDate - currentDayOfMonth;
        }

        LocalDate nextPaymentDate = now.withDayOfMonth(paymentDate).plusMonths(1);
        return (int) ChronoUnit.DAYS.between(now, nextPaymentDate);
    }

    @Transactional
    public Long update(Long id, SubscriptionUpdateRequestDto requestDto) {
        Ott replaceOtt = null;

        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 구독 정보가 없습니다. id=" + id));

        if (requestDto.getOttName() != null && requestDto.getOttRatePlan() != null) {
            replaceOtt = ottRepository.findOttByNameAndAndRatePlan(requestDto.getOttName(), requestDto.getOttRatePlan())
                    .orElseThrow(() -> new EntityNotFoundException("해당 OTT 정보를 찾을 수 없습니다."));
        }

        subscription.update(
                requestDto.getName(),
                requestDto.getPayment(),
                requestDto.getMemo(),
                requestDto.getPaymentDate(),
                replaceOtt);

        return id;
    }

    @Transactional
    public void delete(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 구독 정보가 없습니다. id" + id));
        subscriptionRepository.delete(subscription);
    }
}
