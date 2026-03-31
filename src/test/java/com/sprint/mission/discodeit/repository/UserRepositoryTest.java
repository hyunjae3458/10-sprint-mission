package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EnableJpaAuditing
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("유저 이름으로 검색하면 해당 유저 이름의 객체를 반환해야 한다.")
    void find_by_username_success() {
        // given
        String targetName = "김현재";
        User user = new User(targetName, "fred@naver.com", "123123");

        userRepository.save(user);
        // when
        Optional<User> foundUser = userRepository.findByUsername(targetName);

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo(targetName);
        assertThat(foundUser.get().getEmail()).isEqualTo("fred@naver.com");

    }

    @Test
    @DisplayName("이메일로 유저를 검색하면 해당 이메일의 유저가 있는지 반환해야한다")
    void existsByEmail() {
        // given
        String targetEmail1 = "fred@naver.com";
        String targetEmail2 = "sonata@naver.com";
        User user = new User("김현재",targetEmail1,"123123");

        userRepository.save(user);
        // when
        boolean result1 = userRepository.existsByEmail(targetEmail1);
        boolean result2 = userRepository.existsByEmail(targetEmail2);

        // then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }
}