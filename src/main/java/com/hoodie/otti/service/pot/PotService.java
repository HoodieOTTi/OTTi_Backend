package com.hoodie.otti.service.pot;


import com.hoodie.otti.dto.pot.PotSaveRequestDto;
import com.hoodie.otti.model.pot.Pot;
import com.hoodie.otti.model.profile.User;
import com.hoodie.otti.repository.ott.OttRepository;
import com.hoodie.otti.repository.ott.SubscriptionRepository;
import com.hoodie.otti.repository.pot.PotRepository;
import com.hoodie.otti.repository.profile.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PotService {

    private static final Logger log = LoggerFactory.getLogger(PotService.class);

    private final PotRepository potRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final OttRepository ottRepository;

    public PotService(PotRepository potRepository,
                      SubscriptionRepository subscriptionRepository,
                      UserRepository userRepository,
                      OttRepository ottRepository) {
        this.potRepository = potRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.ottRepository = ottRepository;
    }

    public Pot findById(Long potId) {
        return potRepository.findById(potId).orElseThrow(() -> new EntityNotFoundException("Pot not found"));
    }

    @Transactional
    public Long save(PotSaveRequestDto requestDto) {
        try {
            // 사용자 조회
            User user = userRepository.findById(requestDto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("유효한 user ID가 아닙니다."));

            // Pot 엔티티 생성
//            Pot pot = requestDto.toEntity(user, ott);
            Pot pot = Pot.builder()
                    .name(requestDto.getName())
                    .user(user)
                    .ott(requestDto.getOttId())
                    .depositAccount(requestDto.getDepositAccount())
                    .ratePlan(requestDto.getRatePlan())
                    .creator(user)
                    .build();

            // 팟 저장
            potRepository.save(pot);

            return pot.getId();
        } catch (Exception e) {
            log.error("Error saving pot: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public Pot findPotById(Long potId) {
        return potRepository.findById(potId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 POT이 존재하지 않습니다.: " + potId));
    }



}
