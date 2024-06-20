package sparta.code3line.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sparta.code3line.common.CommonResponse;
import sparta.code3line.domain.comment.dto.CommentRequestDto;
import sparta.code3line.domain.comment.dto.CommentResponseDto;
import sparta.code3line.domain.comment.service.CommentService;
import sparta.code3line.security.UserPrincipal;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{boardId}")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<CommonResponse<CommentResponseDto>> createComment(@PathVariable Long boardId,
                                                                            @AuthenticationPrincipal UserPrincipal principal,
                                                                            @RequestBody CommentRequestDto requestDto) {

        CommonResponse<CommentResponseDto> response = new CommonResponse<>(
                "댓글 생성 완료",
                201,
                commentService.createComment(boardId, principal.getUser(), requestDto)
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/comments")
    public ResponseEntity<CommonResponse<List<CommentResponseDto>>> readComments (@PathVariable Long boardId) {

        CommonResponse<List<CommentResponseDto>> response = new CommonResponse<>(
                "댓글 전체 조회 완료",
                200,
                commentService.readComments(boardId)
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/comment/{commentId}")
    public ResponseEntity<CommonResponse<CommentResponseDto>> updateComment (@PathVariable Long boardId,
                                                                             @PathVariable Long commentId,
                                                                             @AuthenticationPrincipal UserPrincipal principal,
                                                                             @RequestBody CommentRequestDto requestDto) {

        CommonResponse<CommentResponseDto> response = new CommonResponse<>(
                "댓글 수정 완료",
                200,
                commentService.updateComment(boardId, commentId, principal.getUser(), requestDto)
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<CommonResponse<Void>> deleteComment(@PathVariable Long boardId,
                                                              @PathVariable Long commentId,
                                                              @AuthenticationPrincipal UserPrincipal principal) {
        CommonResponse<Void> response = new CommonResponse<>(
                "댓글 삭제 완료",
                204,
                commentService.deleteComment(boardId, commentId, principal.getUser())
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
