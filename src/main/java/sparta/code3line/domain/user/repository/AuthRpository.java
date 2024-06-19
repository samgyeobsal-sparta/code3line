package sparta.code3line.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.code3line.domain.user.entity.User;

public interface AuthRpository extends JpaRepository<User, Long> {
}
