package sparta.code3line.domain.user.repository;

import org.springframework.data.repository.CrudRepository;
import sparta.code3line.domain.user.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
