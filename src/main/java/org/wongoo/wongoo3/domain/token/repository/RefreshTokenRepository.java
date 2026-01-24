package org.wongoo.wongoo3.domain.token.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wongoo.wongoo3.domain.token.RefreshToken;
import org.wongoo.wongoo3.domain.user.User;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByUser(User user);

    Optional<RefreshToken> findByUser(User user);
}
