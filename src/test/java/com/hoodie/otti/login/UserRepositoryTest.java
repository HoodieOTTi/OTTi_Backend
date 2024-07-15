package com.hoodie.otti.login;

import com.hoodie.otti.entity.login.User;
import com.hoodie.otti.repository.login.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UserRepositoryTest는 UserRepository 인터페이스의 메서드를 테스트하는 JPA 테스트 클래스입니다.
 */
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    /**
     * findByKakaoUserId 메서드를 테스트합니다.
     * 주어진 카카오 사용자 ID에 해당하는 User 엔티티를 찾는지 검증합니다.
     */
    @Test
    public void findByKakaoUserId_thenReturnUser() {
        // Given
        User user = new User();
        user.setKakaoUserId("test-kakao-user-id");
        user.setUserEmail("test@example.com");

        // User 엔티티를 영속화합니다.
        entityManager.persist(user);
        entityManager.flush();

        // When
        Optional<User> found = userRepository.findByKakaoUserId("test-kakao-user-id");

        // Then
        // 찾은 결과가 존재하는지 확인합니다.
        assertThat(found.isPresent()).isTrue();

        // 찾은 User 엔티티의 userEmail이 기대한 값과 일치하는지 검증합니다.
        assertThat(found.get().getUserEmail()).isEqualTo(user.getUserEmail());
    }
}
