package sparta.code3line.domain.board.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sparta.code3line.common.CommonResponse;
import sparta.code3line.domain.board.dto.BoardRequestDto;
import sparta.code3line.domain.board.dto.BoardResponseDto;
import sparta.code3line.domain.board.dto.BoardUpdateRequestDto;
import sparta.code3line.domain.board.entity.Board;
import sparta.code3line.domain.user.entity.User;
import sparta.code3line.domain.user.service.UserService;
import sparta.code3line.security.UserPrincipal;

@RestController
@RequiredArgsConstructor
public class AdminBoardController {
    private final AdminBoardService adminBoardService;
    private final UserService userService;

    // 어드민 : 공지 게시물 생성
    @PostMapping("/admin/boards")
    public ResponseEntity<CommonResponse<BoardResponseDto>> adminAddBoard(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody BoardRequestDto requestDto
    ) {

        BoardResponseDto responseDto = adminBoardService.adminAddBoard(userPrincipal.getUser(), requestDto);
        CommonResponse<BoardResponseDto> commonResponse = new CommonResponse<>(
                "관리자 : 공지 게시글 생성",
                201,
                responseDto
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
    }

    // 어드민 : 모든 게시물 수정.
    @PutMapping("/admin/boards/{boardId}")
    public ResponseEntity<CommonResponse<BoardResponseDto>> adminUpdateBoard(
            @PathVariable Long boardId,
            @RequestBody BoardUpdateRequestDto requestDto
    ) {
        BoardResponseDto responseDto = adminBoardService.adminUpdateBoard(boardId, requestDto);
        CommonResponse<BoardResponseDto> commonResponse = new CommonResponse<>(
                "관리자 : 게시글 수정 완료",
                200,
                responseDto
        );
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    // 어드민 : 모든 게시물에 대한 삭제
    @DeleteMapping("/admin/boards/{boardId}")
    public ResponseEntity<CommonResponse<Void>> adminDeleteBoard(
            @PathVariable Long boardId
    ) {
        adminBoardService.adminDeleteBoard(boardId);
        CommonResponse<Void> commonResponse = new CommonResponse<>(
                "관리자 : 게시글 삭제 완료",
                204,
                null
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(commonResponse);
    }

    @PatchMapping("/admin/boards/{boardId}/pick")
    public ResponseEntity<CommonResponse<Void>> adminPickBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        User user = userPrincipal.getUser();
        adminBoardService.adminPickBoard(boardId, user);
        CommonResponse<Void> response = new CommonResponse<>("게시글 상태 변경", HttpStatus.OK.value(), null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
