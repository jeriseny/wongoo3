package org.wongoo.wongoo3.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wongoo.wongoo3.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByNickname(String nickname);
}
