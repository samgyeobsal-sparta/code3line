package sparta.code3line.domain.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import sparta.code3line.domain.user.repository.UserRepository;
import sparta.code3line.jwt.JwtService;

import java.util.List;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "BoardService")
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final JwtService jwtService;

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
        List<Follow> followingList = followRepository.findAllByFollowerId(user.getId());
        List<Long> followingUserIds = new ArrayList<>();
        for (Follow follow : followingList) {
            followingUserIds.add(follow.getFollowing().getId());
        }

        List<Board> boards = boardRepository.findAllByUserIdInOrderByCreatedAtDesc(followingUserIds);
        List<BoardResponseDto> BoardResponseDto = new ArrayList<>();
        for (Board board : boards) {
            BoardResponseDto.add(new BoardResponseDto(board));
        }

        return BoardResponseDto;
    }

    // 게시글 전체 조회
    public List<BoardResponseDto> getAllBoards(User user) {
        log.info("getAllBoards 메서드 실행");
        List<Board> boards = boardRepository.findAllByUserId(user.getId());

        if (boards.isEmpty()) {
            log.error("해당 사용자의 게시글이 하나도 없음.");
            throw new CustomException(ErrorCode.BOARD_NOT_FOUND);
        }

        log.info("getAllBoards 메서드 성공");
        return boards.stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }

    // 게시글 단건 조회
    public BoardResponseDto getOneBoard(User user, Long boardId) {
        log.info("getOneBoard 메서드 실행");
        Board board = getBoard(user,boardId);

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
