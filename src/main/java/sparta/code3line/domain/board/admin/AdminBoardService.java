package sparta.code3line.domain.board.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sparta.code3line.common.exception.CustomException;
import sparta.code3line.common.exception.ErrorCode;
import sparta.code3line.domain.board.dto.BoardRequestDto;
import sparta.code3line.domain.board.dto.BoardResponseDto;
import sparta.code3line.domain.board.dto.BoardUpdateRequestDto;
import sparta.code3line.domain.board.entity.Board;
import sparta.code3line.domain.board.repository.BoardRepository;
import sparta.code3line.domain.user.entity.User;

@Slf4j(topic = "AdminBoardService")
@Service
@RequiredArgsConstructor
public class AdminBoardService {

    private final BoardRepository boardRepository;

    // Admin : NOTICE 게시글 생성
    public BoardResponseDto adminAddBoard(
            User user,
            BoardRequestDto requestDto) {
        log.info("admin : adminAddBoard 메서드 실행");

        Board board = Board.builder()
                .user(user)
                .title(requestDto.getTitle())
                .contents(requestDto.getContents())
                .type(Board.BoardType.NOTICE) // adminAddBoard 메서드를 사용해서 게시글을 생성하면 NOTICE 게시글로 고정
                .build();

        Board adminAddBoard = boardRepository.save(board);

        BoardResponseDto responseDto = new BoardResponseDto(
                adminAddBoard.getUser().getNickname(),
                adminAddBoard.getId(),
                adminAddBoard.getTitle(),
                adminAddBoard.getContents(),
                adminAddBoard.getCreatedAt(),
                adminAddBoard.getModifiedAt()
        );

        log.info("admin : adminAddBoard 메서드 실행");
        return responseDto;
    }

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
