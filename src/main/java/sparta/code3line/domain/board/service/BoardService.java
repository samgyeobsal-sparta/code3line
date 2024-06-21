package sparta.code3line.domain.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sparta.code3line.domain.board.dto.BoardRequestDto;
import sparta.code3line.domain.board.dto.BoardResponseDto;
import sparta.code3line.domain.board.entity.Board;
import sparta.code3line.domain.board.repository.BoardRepository;
import sparta.code3line.domain.user.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "BoardService")
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    // 게시글 추가 메서드.
    public BoardResponseDto addBoard(
            User user,
            BoardRequestDto requestDto) {
        log.info("addBoard 메서드 실행");

        Board board = Board.builder()
                .user(user)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();

        Board addBoard = boardRepository.save(board);

        BoardResponseDto responseDto = new BoardResponseDto(
                addBoard.getId(),
                addBoard.getTitle(),
                addBoard.getContent(),
                addBoard.getCreatedAt()
        );

        log.info("addBoard 메서드 성공");
        return responseDto;
    }

    public List<BoardResponseDto> getAllBoards(User user) {
        log.info("getAllBoards 메서드 실행");
        List<Board> boards = boardRepository.findAllByUserId(user.getId());

        return boards.stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }

    public BoardResponseDto getOneBoard(User user, Long boardId) {
        log.info("getOneBoard 메서드 실행");
        Board board = boardRepository.findByUserIdAndId(user.getId(),boardId);

        return new BoardResponseDto(board);
    }
}
