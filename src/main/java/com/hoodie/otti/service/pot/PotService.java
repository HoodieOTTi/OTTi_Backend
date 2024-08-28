package com.hoodie.otti.service.pot;


import com.hoodie.otti.dto.pot.PotSaveRequestDto;
import com.hoodie.otti.model.ott.Ott;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class PotService {


    private static final Logger log = LoggerFactory.getLogger(PotService.class);

    private final PotRepository potRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final OttRepository ottRepository;

    @Autowired
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

            // OTT 조회 (이름과 요금제에 따라)
            Ott ott = ottRepository.findOttByNameAndRatePlan(requestDto.getOttName(), requestDto.getOttRatePlan())
                    .orElseThrow(() -> new EntityNotFoundException("해당 OTT 정보를 찾을 수 없습니다."));

            // Pot 엔티티 생성
            Pot pot = requestDto.toEntity(user, ott, user);

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

    public void updatePot(Principal principal, PotSaveRequestDto requestDto){
        Optional<User> user = userRepository.findByKakaoId(Long.parseLong(principal.getName()));
        Optional<Pot> pot = potRepository.findById(Long.parseLong(principal.getName()));

        if (user.isEmpty()) {
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }

        if (pot.isEmpty()) {
            throw new IllegalArgumentException("해당 팟이 존재하지 않습니다.");
        }

        Ott ott = ottRepository.findOttByNameAndRatePlan(requestDto.getName(), requestDto.getOttRatePlan())
                .orElseThrow(() -> new EntityNotFoundException("해당 OTT 정보를 찾을 수 없습니다."));


        pot.get().setName(requestDto.getName());
        pot.get().setOttId(ott);
        pot.get().setDepositAccount(requestDto.getDepositAccount());
        pot.get().setRatePlan(requestDto.getRatePlan());
        potRepository.save(pot.get());
    }

    public void deletePot(Long id){
        Pot pot = potRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 팟이 없습니다. id" + id));
        potRepository.delete(pot);
    }
}
