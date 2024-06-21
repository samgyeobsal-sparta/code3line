package sparta.code3line.domain.board.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sparta.code3line.common.CommonResponse;
import sparta.code3line.domain.board.dto.BoardRequestDto;
import sparta.code3line.domain.board.dto.BoardResponseDto;
import sparta.code3line.domain.board.service.BoardService;
import sparta.code3line.security.UserPrincipal;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // 게시글 추가.
    @PostMapping("/boards")
    public ResponseEntity<CommonResponse<BoardResponseDto>> addBoard(
            @RequestBody BoardRequestDto requestDto,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        BoardResponseDto responseDto = boardService.addBoard(userPrincipal.getUser(), requestDto);
        CommonResponse<BoardResponseDto> commonResponse = new CommonResponse<>(
                "게시글 등록 성공",
                201,
                responseDto
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
    }

    // 게시글 전체 조회
    @GetMapping("/boards")
    public ResponseEntity<CommonResponse<Page<BoardResponseDto>>> getAllBoards(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        Page<BoardResponseDto> responseDto = boardService.getAllBoards(page, size);
        CommonResponse<Page<BoardResponseDto>> commonResponse = new CommonResponse<>(
                "게시글 " + page + "번 페이지 조회 완료",
                200,
                responseDto
        );
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    // 게시글 단건 조회
    @GetMapping("/boards/{boardId}")
    public ResponseEntity<CommonResponse<BoardResponseDto>> getOneBoard(
            @PathVariable Long boardId
    ) {
        BoardResponseDto responseDto = boardService.getOneBoard(boardId);
        CommonResponse<BoardResponseDto> commonResponse = new CommonResponse<>(
                "게시글 단건 조회 완료.",
                200,
                responseDto
        );
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    // 게시글 수정
    @PutMapping("/boards/{boardId}")
    public ResponseEntity<CommonResponse<BoardResponseDto>> updateBoard(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long boardId,
            @RequestBody BoardRequestDto requestDto
    ) {
        BoardResponseDto responseDto = boardService.updateBoard(userPrincipal.getUser(), boardId, requestDto);
        CommonResponse<BoardResponseDto> commonResponse = new CommonResponse<>(
                "게시글 수정 완료",
                200,
                responseDto
        );
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    // 게시글 삭제
    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<CommonResponse<Void>> deleteBoard(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long boardId
    ) {
        boardService.deleteBoard(userPrincipal.getUser(),boardId);
        CommonResponse<Void> commonResponse = new CommonResponse<>(
                "게시글 삭제 완료",
                204,
                null
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(commonResponse);
    }

    // 팔로우하는 사용자의 게시물 조회
    @GetMapping("/boards/follows")
    public ResponseEntity<CommonResponse<List<BoardResponseDto>>> getFollowBoard(@AuthenticationPrincipal UserPrincipal userPrincipal)
    {
        if (userPrincipal == null || userPrincipal.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<BoardResponseDto> followBoardList = boardService.getFollowBoard(userPrincipal.getUser());
        CommonResponse<List<BoardResponseDto>> response = new CommonResponse<>("게시글 조회 성공 🎉", HttpStatus.OK.value(), followBoardList);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

