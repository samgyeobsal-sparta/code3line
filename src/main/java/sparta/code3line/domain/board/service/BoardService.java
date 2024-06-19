package sparta.code3line.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.code3line.domain.board.repository.BoardRepository;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
}
