package sparta.code3line.domain.board.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.code3line.common.exception.CustomException;
import sparta.code3line.common.exception.ErrorCode;
import sparta.code3line.domain.board.dto.BoardRequestDto;
import sparta.code3line.domain.board.dto.BoardResponseDto;
import sparta.code3line.domain.board.dto.BoardUpdateRequestDto;
import sparta.code3line.domain.board.entity.Board;
import sparta.code3line.domain.board.repository.BoardRepository;
import sparta.code3line.domain.user.entity.User;

import java.util.Optional;

@Slf4j(topic = "AdminBoardService")
@Service
@RequiredArgsConstructor
public class AdminBoardService {

    private final BoardRepository boardRepository;

    // ADMIN : NOTICE 게시글 생성
    public BoardResponseDto adminAddBoard(
            User user,
            BoardRequestDto requestDto) {

        Board board = Board.builder()
                .user(user)
                .title(requestDto.getTitle())
                .contents(requestDto.getContents())
                .type(Board.BoardType.NOTICE)
                .build();

        Board adminAddBoard = boardRepository.save(board);

        return new BoardResponseDto(adminAddBoard);

    }

    // ADMIN : 게시글 수정
    public BoardResponseDto adminUpdateBoard(Long boardId, BoardUpdateRequestDto requestDto) {

        Board board = boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        board.updateTitle(requestDto.getTitle());
        board.updateContents(requestDto.getContent());

        boardRepository.save(board);

        return new BoardResponseDto(board);

    }

    // ADMIN : 게시글 삭제
    public void adminDeleteBoard(Long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        boardRepository.delete(board);

    }

    // ADMIN : 특정 게시글 타입 PICK으로 변경
    @Transactional
    public void adminPickBoard(Long boardId, User currentUser) {

        Optional<Board> currentPickBoardOptional = boardRepository.findByType(Board.BoardType.PICK);

        if (currentPickBoardOptional.isPresent()) {
            Board currentPickBoard = currentPickBoardOptional.get();
            currentPickBoard.updateType(Board.BoardType.NORMAL);
            boardRepository.save(currentPickBoard);

        }

        Board board = boardRepository.findById(boardId).orElseThrow((
                ) -> { return new CustomException(ErrorCode.BOARD_NOT_FOUND);}
        );

        board.updateType(Board.BoardType.PICK);

        boardRepository.save(board);

    }
}
