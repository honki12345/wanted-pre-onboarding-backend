package me.honki12345.wantedassignment.repository;

import me.honki12345.wantedassignment.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMemberId(Long userId);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
