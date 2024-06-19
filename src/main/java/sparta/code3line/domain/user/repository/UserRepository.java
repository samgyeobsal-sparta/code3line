package sparta.code3line.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.code3line.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String email);
}
