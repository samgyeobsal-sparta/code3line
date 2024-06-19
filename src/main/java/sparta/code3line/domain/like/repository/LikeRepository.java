package sparta.code3line.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.code3line.domain.like.entity.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
