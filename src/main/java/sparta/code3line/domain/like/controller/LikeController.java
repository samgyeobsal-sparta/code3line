package sparta.code3line.domain.like.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;
import sparta.code3line.common.CommonResponse;
import sparta.code3line.domain.like.dto.LikeResponseDto;
import sparta.code3line.domain.like.service.LikeService;
import sparta.code3line.security.UserPrincipal;

@RestController
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/boards/{id}/likes")
    public ResponseEntity<CommonResponse<LikeResponseDto>> createLikeBoard(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {

        LikeResponseDto responseDto = likeService.createLikeBoard(id, principal.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<>(
                "게시글 좋아요 성공 🎉",
                HttpStatus.OK.value(),
                responseDto));

    }

    @DeleteMapping("/boards/{id}/likes")
    public ResponseEntity<CommonResponse<LikeResponseDto>> deleteLikeBoard(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {

        LikeResponseDto responseDto = likeService.deleteLikeBoard(id, principal.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<>(
                "게시글 좋아요 취소 성공 🎉",
                HttpStatus.OK.value(),
                responseDto));

    }

    @PostMapping("/comments/{id}/likes")
    public ResponseEntity<CommonResponse<LikeResponseDto>> createLikeComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {

        LikeResponseDto responseDto = likeService.createLikeComment(id, principal.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<>(
                "댓글 좋아요 성공 🎉",
                HttpStatus.OK.value(),
                responseDto));

    }

    @DeleteMapping("/comments/{id}/likes")
    public ResponseEntity<CommonResponse<LikeResponseDto>> deleteLikeComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {

        LikeResponseDto responseDto = likeService.deleteLikeComment(id, principal.getUser());
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<>(
                "댓글 좋아요 취소 성공 🎉",
                HttpStatus.OK.value(),
                responseDto));

    }

}