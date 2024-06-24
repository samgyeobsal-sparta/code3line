package sparta.code3line.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.code3line.domain.user.entity.Token;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByUserId(Long userId);

    Optional<Token> findByToken(String token);
}
