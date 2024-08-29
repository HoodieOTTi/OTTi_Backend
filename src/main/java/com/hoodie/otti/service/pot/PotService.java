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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public void updatePot(Long potId, PotSaveRequestDto requestDto){
        try {
            Pot pot = potRepository.findById(potId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 팟이 존재하지 않습니다."));

            System.out.println("Pot ID: " + pot.getId());
            System.out.println("Pot 이름: " + pot.getName());
            System.out.println("requestDto Pot 이름: " + requestDto.getName());
            System.out.println("requestDto OttName 이름: " + requestDto.getOttName());
            System.out.println("requestDto OttRatePlan 이름: " + requestDto.getOttRatePlan());

            if (requestDto.getOttName() != null && requestDto.getOttRatePlan() != null) {
                // OTT 조회 (이름과 요금제에 따라)
                Ott ott = ottRepository.findOttByNameAndRatePlan(requestDto.getOttName(), requestDto.getOttRatePlan())
                        .orElseThrow(() -> new EntityNotFoundException("해당 OTT 정보를 찾을 수 없습니다."));
                pot.setOttId(ott);
            } else {
                // OTT 정보는 변경하지 않음
                System.out.println("OTT 이름과 요금제가 null로 설정되어 있어, 기존 OTT 정보를 유지합니다.");
            }

            if (requestDto.getName() != null) {
                pot.setName(requestDto.getName());
            }

            if (requestDto.getDepositAccount() != null) {
                pot.setDepositAccount(requestDto.getDepositAccount());
            }

            if (requestDto.getRatePlan() != null) {
                pot.setRatePlan(requestDto.getRatePlan());
            }

            potRepository.save(pot);
        } catch (Exception e) {
            log.error("Error updating pot: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "팟 업데이트에 실패했습니다", e);
        }
    }

    public void deletePot(Long id){
        Pot pot = potRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 팟이 없습니다. id" + id));
        potRepository.delete(pot);
    }
}
