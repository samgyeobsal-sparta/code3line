package sparta.code3line.domain.follow.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sparta.code3line.common.CommonResponse;
import sparta.code3line.common.exception.CustomException;
import sparta.code3line.common.exception.ErrorCode;
import sparta.code3line.domain.follow.dto.FollowRequestDto;
import sparta.code3line.domain.follow.dto.FollowResponseDto;
import sparta.code3line.domain.follow.service.FollowService;
import sparta.code3line.domain.user.entity.User;
import sparta.code3line.security.UserPrincipal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/follows")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{followId}")
    public ResponseEntity<CommonResponse<FollowResponseDto>> createFollow(@PathVariable Long followId, @RequestBody FollowRequestDto followRequestDto) {
        followService.followUser(followRequestDto.getFollowingUserId(), getCurrentUser());
        FollowResponseDto followResponseDto = new FollowResponseDto(followRequestDto.getFollowingUserId(), getCurrentUser().getId());
        CommonResponse<FollowResponseDto> response = new CommonResponse<>("팔로우 성공 🎉", HttpStatus.OK.value(), followResponseDto);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{followId}")
    public ResponseEntity<CommonResponse<FollowResponseDto>> deleteFollow(@PathVariable Long followId, @RequestBody FollowRequestDto followRequestDto) {
        followService.unfollowUser(followRequestDto.getFollowingUserId(), getCurrentUser());
        FollowResponseDto followResponseDto = new FollowResponseDto(followRequestDto.getFollowingUserId(), getCurrentUser().getId());
        CommonResponse<FollowResponseDto> response = new CommonResponse<>("언팔로우 성공 🎉", HttpStatus.OK.value(), followResponseDto);
        return ResponseEntity.ok().body(response);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException(ErrorCode.USERNAME_NOT_FOUND.getStatus(), "인증되지 않은 사용자입니다.");
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.getUser();
    }
}
