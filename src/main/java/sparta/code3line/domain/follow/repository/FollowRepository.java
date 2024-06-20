package sparta.code3line.domain.follow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sparta.code3line.domain.follow.entity.Follow;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowingIdAndFollowerId(Long followingId, Long id);
}
