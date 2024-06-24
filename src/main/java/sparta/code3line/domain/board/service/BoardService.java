package sparta.code3line.domain.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sparta.code3line.common.exception.CustomException;
import sparta.code3line.common.exception.ErrorCode;
import sparta.code3line.domain.board.dto.BoardRequestDto;
import sparta.code3line.domain.board.dto.BoardResponseDto;
import sparta.code3line.domain.board.dto.BoardUpdateRequestDto;
import sparta.code3line.domain.board.entity.Board;
import sparta.code3line.domain.board.entity.BoardFiles;
import sparta.code3line.domain.board.repository.BoardFilesRepository;
import sparta.code3line.domain.board.repository.BoardRepository;
import sparta.code3line.domain.file.FileService;
import sparta.code3line.domain.follow.entity.Follow;
import sparta.code3line.domain.follow.repository.FollowRepository;
import sparta.code3line.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j(topic = "BoardService")
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final FollowRepository followRepository;
    private final FileService fileService;
    private final BoardFilesRepository boardFilesRepository;

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
        if (board.getType() != Board.BoardType.NORMAL) {
            log.error("게시물에 대한 권한 없음");
            throw new CustomException(ErrorCode.NOT_AUTHORIZED);
        }
        log.info("getBoard 메서드 성공");
        return board;
    }

    // 게시글 추가 메서드.
    public BoardResponseDto addBoard(
            User user,
            BoardRequestDto requestDto,
            List<MultipartFile> fileList) {

        log.info("addBoard 메서드 실행");

        Board board = Board.builder()
                .user(user)
                .title(requestDto.getTitle())
                .contents(requestDto.getContents())
                .type(Board.BoardType.NORMAL) // addBoard 메서드를 사용해서 게시글을 생성하면 NORMAL 게시글로 고정
                .build();
        if (fileList != null) {
            addFileonBoard(board, fileList);
        }

        Board addBoard = boardRepository.save(board);

        BoardResponseDto responseDto = new BoardResponseDto(
                addBoard.getUser().getNickname(),
                addBoard.getId(),
                addBoard.getTitle(),
                addBoard.getContents(),
                addBoard.getCreatedAt(),
                addBoard.getModifiedAt()
        );

        log.info("addBoard 메서드 성공");
        return responseDto;
    }

    private void addFileonBoard(Board board, List<MultipartFile> fileList) {
        if (!fileList.isEmpty()) {
            List<BoardFiles> boardFiles = new ArrayList<>();
            List<String> urls = fileService.uploadFile(fileList);

            for (String url : urls) {
                BoardFiles boardFile = BoardFiles.builder()
                        .file(url)
                        .board(board)
                        .build();
                boardFiles.add(boardFile);
            }
            boardFilesRepository.saveAll(boardFiles);
        }
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
    // 일반 + 공지 게시글 전체 조회
    public Page<BoardResponseDto> getAllBoards(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        Optional<Board> pickBoardOptional = boardRepository.findByType(Board.BoardType.PICK);

        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();
        pickBoardOptional.ifPresent(pickBoard -> boardResponseDtoList.add(new BoardResponseDto(pickBoard)));

        Page<Board> boardPage = boardRepository.findAllByTypeNot(Board.BoardType.PICK, (PageRequest) pageable);
        for (Board board : boardPage.getContent()) {
            boardResponseDtoList.add(new BoardResponseDto(board));
        }
        return new PageImpl<>(boardResponseDtoList, pageable, boardPage.getTotalElements());
    }

    // 공지 게시글 전체 조회
    public Page<BoardResponseDto> getAllNoticeBoards(int page, int size) {
        log.info("getAllNoticeBoards 메서드 실행");

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Board> boardPage = boardRepository.findAllByType(Board.BoardType.NOTICE, pageable);

        log.info("getAllNoticeBoards 메서드 성공");
        return boardPage.map(BoardResponseDto::new);
    }

    // 일반 게시글 전체 조회
    public Page<BoardResponseDto> getAllNormalBoards(int page, int size) {
        log.info("getAllNormalBoards 메서드 실행");

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Board> boardPage = boardRepository.findAllByType(Board.BoardType.NORMAL, pageable);

        log.info("getAllNormalBoards 메서드 성공");
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
            BoardUpdateRequestDto requestDto,
            List<MultipartFile> fileList) {

        log.info("updateBoard 메서드 실행");
        Board board = getBoard(user, boardId);

        if (requestDto.getTitle() != null) {
            board.updateTitle(requestDto.getTitle());
        }

        if (requestDto.getContent() != null) {
            board.updateContents(requestDto.getContent());
        }

        if (fileList != null) {
            addFileonBoard(board, fileList);
        }

        boardRepository.save(board);

        log.info("updateBoard 메서드 성공");
        return new BoardResponseDto(board);
    }

    // 게시물 삭제
    public void deleteBoard(User user, Long boardId) {
        log.info("deleteBoard 메서드 실행");
        Board board = getBoard(user, boardId);

        log.info("deleteBoard 메서드 성공");
        boardRepository.delete(board);
    }


}

