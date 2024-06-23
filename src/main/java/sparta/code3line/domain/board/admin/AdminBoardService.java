package sparta.code3line.domain.board.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sparta.code3line.common.exception.CustomException;
import sparta.code3line.common.exception.ErrorCode;
import sparta.code3line.domain.board.dto.BoardResponseDto;
import sparta.code3line.domain.board.dto.BoardUpdateRequestDto;
import sparta.code3line.domain.board.entity.Board;
import sparta.code3line.domain.board.repository.BoardRepository;

@Slf4j(topic = "AdminBoardService")
@Service
@RequiredArgsConstructor
public class AdminBoardService {

    private final BoardRepository boardRepository;

    // Admin : 게시글 수정
    public BoardResponseDto adminUpdateBoard(Long boardId, BoardUpdateRequestDto requestDto) {
        log.info("admin : adminUpdateBoard 메서드 실행");
        Board board = boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        if (requestDto.getTitle() != null) {
            if (requestDto.getTitle().trim().isEmpty()) {
                log.error("admin : 게시글 제목이 형식에 맞지 않음");
                throw new CustomException(ErrorCode.BAD_REQUEST);
            }
            board.updateTitle(requestDto.getTitle());
        }

        if (requestDto.getContent() != null) {
            if (requestDto.getContent().trim().isEmpty()) {
                log.error("admin : 게시물 내용이 형식에 맞지 않음");
                throw new CustomException(ErrorCode.BAD_REQUEST);
            }
            board.updateContents(requestDto.getContent());
        }

        boardRepository.save(board);

        log.info("admin : adminUpdateBoard 메서드 성공");
        return new BoardResponseDto(board);
    }

    // Admin : 게시글 삭제
    public void adminDeleteBoard(Long boardId) {
        log.info("admin : adminDeleteBoard 메서드 실행");
        Board board = boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        log.info("admin : adminDeleteBoard 메서드 성공");
        boardRepository.delete(board);
    }
}
