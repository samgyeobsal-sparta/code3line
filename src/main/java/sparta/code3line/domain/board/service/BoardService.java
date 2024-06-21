package sparta.code3line.domain.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sparta.code3line.common.exception.CustomException;
import sparta.code3line.common.exception.ErrorCode;
import sparta.code3line.domain.board.dto.BoardRequestDto;
import sparta.code3line.domain.board.dto.BoardResponseDto;
import sparta.code3line.domain.board.entity.Board;
import sparta.code3line.domain.board.repository.BoardRepository;
import sparta.code3line.domain.follow.entity.Follow;
import sparta.code3line.domain.follow.repository.FollowRepository;
import sparta.code3line.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "BoardService")
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final FollowRepository followRepository;

    // user 에 해당하는 게시물 찾아오기.
    public Board getBoard(User user, Long boardId) {
        log.info("getBoard 메서드 실행");
        Board board = boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(ErrorCode.BOARD_NOT_FOUND)
        );
        if (!board.getUser().getId().equals(user.getId())) {
            log.error("다른 사용자의 게시물 침범.");
            throw new CustomException(ErrorCode.USER_DIFFERENT);
        }
        log.info("getBoard 메서드 성공");
        return board;
    }


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
                addBoard.getUser().getNickname(),
                addBoard.getId(),
                addBoard.getTitle(),
                addBoard.getContent(),
                addBoard.getCreatedAt()
        );

        log.info("addBoard 메서드 성공");
        return responseDto;
    }


    // 팔로우 조회
    public List<BoardResponseDto> getFollowBoard(User user) {
        if (user == null) {
            throw new CustomException(ErrorCode.USERNAME_NOT_FOUND);
        }

        List<Follow> followingList = followRepository.findAllByFollowerId(user.getId());
        if (followingList.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOLLOW_POST);
        }

        List<Long> followingUserIds = new ArrayList<>();
        for (Follow follow : followingList) {
            followingUserIds.add(follow.getFollowing().getId());
        }

        List<Board> boards = boardRepository.findAllByUserIdInOrderByCreatedAtDesc(followingUserIds);
        List<BoardResponseDto> boardResponseDto = new ArrayList<>();
        for (Board board : boards) {
            boardResponseDto.add(new BoardResponseDto(board));
        }

        return boardResponseDto;
    }

    // 게시글 전체 조회
    public Page<BoardResponseDto> getAllBoards(int page, int size) {
        log.info("getAllBoards 메서드 실행");

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Board> boardPage = boardRepository.findAll(pageable);

        log.info("getAllBoards 메서드 성공");
        return boardPage.map(BoardResponseDto::new);
    }

    // 게시글 단건 조회
    public BoardResponseDto getOneBoard(Long boardId) {
        log.info("getOneBoard 메서드 실행");

        Board board = boardRepository.findById(boardId).orElseThrow(()
                -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        log.info("getOneBoard 메서드 성공");
        return new BoardResponseDto(board);
    }

    // 게시글 수정
    public BoardResponseDto updateBoard(
            User user,
            Long boardId,
            BoardRequestDto requestDto) {

        log.info("updateBoard 메서드 실행");
        Board board = getBoard(user,boardId);

        board.updateBoard(requestDto.getTitle(), requestDto.getContent());
        boardRepository.save(board);

        log.info("updateBoard 메서드 성공");
        return new BoardResponseDto(board);
    }

    // 게시물 삭제
    public void deleteBoard(User user, Long boardId) {
        log.info("deleteBoard 메서드 실행");
        Board board = getBoard(user,boardId);

        log.info("deleteBoard 메서드 성공");
        boardRepository.delete(board);
    }


}

