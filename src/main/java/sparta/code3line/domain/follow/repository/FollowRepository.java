package sparta.code3line.domain.follow.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sparta.code3line.domain.follow.entity.Follow;

@Repository
public interface FollowRepository extends CrudRepository<Follow, Long> {
}
