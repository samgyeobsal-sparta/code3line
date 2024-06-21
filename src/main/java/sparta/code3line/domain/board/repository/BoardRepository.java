package sparta.code3line.domain.board.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sparta.code3line.domain.board.entity.Board;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findById(Long id);

    List<Board> findAllByUserIdInOrderByCreatedAtDesc(List<Long> followingUserId);

    Page<Board> findAll(Pageable pageable);
}
