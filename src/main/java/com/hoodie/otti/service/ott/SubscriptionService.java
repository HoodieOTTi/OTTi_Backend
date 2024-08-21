package com.hoodie.otti.service.ott;

import com.hoodie.otti.dto.ott.SubscriptionRequestDto;
import com.hoodie.otti.model.ott.Ott;
import com.hoodie.otti.model.ott.Subscription;
import com.hoodie.otti.model.profile.User;
import com.hoodie.otti.repository.ott.OttRepository;
import com.hoodie.otti.repository.ott.SubscriptionRepository;
import com.hoodie.otti.repository.profile.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
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
    public Long save(SubscriptionRequestDto requestDto, Principal principal) {
        Optional<User> user = userRepository.findByKakaoId(Long.parseLong(principal.getName()));

        if (user.isEmpty()) {
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }

        Ott ott = ottRepository.findOttByNameAndAndRatePlan(requestDto.getOttName(), requestDto.getOttRatePlan())
                .orElseThrow(() -> new EntityNotFoundException("해당 OTT 정보를 찾을 수 없습니다."));

        Subscription subscription = Subscription.builder().name(requestDto.getName())
                .payment(requestDto.getPayment())
                .memo(requestDto.getMemo())
                .paymentDate(requestDto.getPaymentDate())
                .userId(user.get())
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

    public List<Subscription> findAllByUserId(Principal principal) {
        Optional<User> user = userRepository.findByKakaoId(Long.parseLong(principal.getName()));

        if (user.isEmpty()) {
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }

        return subscriptionRepository.findByUserId_Id(user.get().getId());
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
    public Long update(Long id, SubscriptionRequestDto requestDto) {
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
