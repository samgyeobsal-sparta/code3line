package sparta.code3line.domain.board.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparta.code3line.common.CommonResponse;
import sparta.code3line.domain.board.dto.BoardResponseDto;
import sparta.code3line.domain.board.dto.BoardUpdateRequestDto;

@RestController
@RequiredArgsConstructor
public class AdminBoardController {
    private final AdminBoardService adminBoardService;

    // 어드민 : 게시물 수정.
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

    // 어드민 : 게시물 삭제
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

}
