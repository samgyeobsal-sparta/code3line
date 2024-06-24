package sparta.code3line.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.code3line.domain.board.entity.BoardFiles;


public interface BoardFilesRepository extends JpaRepository<BoardFiles, Long> {

}
