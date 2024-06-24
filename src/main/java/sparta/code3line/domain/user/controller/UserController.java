package sparta.code3line.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sparta.code3line.common.CommonResponse;
import sparta.code3line.domain.user.dto.UserRequestDto;
import sparta.code3line.domain.user.entity.User;
import sparta.code3line.domain.user.service.PasswordVerification;
import sparta.code3line.domain.user.service.UserService;
import sparta.code3line.security.UserPrincipal;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin/users")
public class UserController {

    private final UserService userService;
    private final PasswordVerification passwordVeriFication;

    // admin - 특정 회원 삭제
    @PatchMapping("{userId}/delete")
    public ResponseEntity<CommonResponse<Void>> deleteUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userPrincipal.getUser();
        userService.deleteUser(userId, user);
        CommonResponse<Void> response = new CommonResponse<>("회원 삭제 성공 🎉", HttpStatus.OK.value(), null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // admin - 특정 회원 차단
    @PatchMapping("{userId}/block")
    public ResponseEntity<CommonResponse<Void>> blockUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userPrincipal.getUser();
        userService.blockUser(userId, user);
        CommonResponse<Void> response = new CommonResponse<>("회원 차단 성공 🎉", HttpStatus.OK.value(), null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // admin - 특정 회원 관리자로 권한 변경
    @PatchMapping("{userId}/verification")
    public ResponseEntity<CommonResponse<Void>> adminUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userPrincipal.getUser();
        userService.adminUser(userId, user);
        CommonResponse<Void> response = new CommonResponse<>("관리자로 권한 변경 성공 🎉", HttpStatus.OK.value(), null);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

//    // 유저 프로필 가져오기
//    @GetMapping("/profiles")
//    public UserResponseDto getUserProfiles(UserRequestDto userRequestDto) {
//        return userService.getUserProfiles(userRequestDto);
//    }

    // 유저 프로필 닉네임 수정
    @PatchMapping("/profiles/{username}")
    public String updateProfilesNickname(@RequestBody UserRequestDto userRequestDto) {
        return userService.updateProfilesNickname(userRequestDto);
    }

    // 유저 비밀번호 수정
    @PatchMapping("/profiles/{username}/pw")
    public String updatePassword(@RequestBody UserRequestDto userRequestDto) {
        return passwordVeriFication.updatePassword(userRequestDto);
    }

}
