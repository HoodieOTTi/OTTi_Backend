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

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByKakaoUserId_thenReturnUser() {
        // Given
        User user = new User();
        user.setKakaoUserId("test-kakao-user-id");
        user.setUserEmail("test@example.com");
        entityManager.persist(user);
        entityManager.flush();

        // When
        Optional<User> found = userRepository.findByKakaoUserId("test-kakao-user-id");

        // Then
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getUserEmail()).isEqualTo(user.getUserEmail());
    }
}
