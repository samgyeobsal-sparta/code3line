package sparta.code3line.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sparta.code3line.common.CommonResponse;
import sparta.code3line.domain.user.dto.PasswordRequestDto;
import sparta.code3line.domain.user.dto.ProfileRequestDto;
import sparta.code3line.domain.user.dto.UserResponseDto;
import sparta.code3line.domain.user.entity.User;
import sparta.code3line.domain.user.service.PasswordVerification;
import sparta.code3line.domain.user.service.UserService;
import sparta.code3line.security.UserPrincipal;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordVerification passwordVeriFication;

    // ADMIN - 특정 회원 삭제
    @PatchMapping("admin/users/{userId}/delete")
    public ResponseEntity<CommonResponse<Void>> deleteUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        User user = userPrincipal.getUser();
        userService.deleteUser(userId, user);
        CommonResponse<Void> response = new CommonResponse<>(
                "회원 삭제 성공 🎉",
                HttpStatus.OK.value(),
                null);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    // ADMIN - 특정 회원 차단
    @PatchMapping("admin/users/{userId}/block")
    public ResponseEntity<CommonResponse<Void>> blockUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        User user = userPrincipal.getUser();
        userService.blockUser(userId, user);
        CommonResponse<Void> response = new CommonResponse<>(
                "회원 차단 성공 🎉",
                HttpStatus.OK.value(),
                null);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    // ADMIN - 특정 회원 관리자로 권한 변경
    @PatchMapping("admin/users/{userId}/verification")
    public ResponseEntity<CommonResponse<Void>> adminUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        User user = userPrincipal.getUser();
        userService.adminUser(userId, user);
        CommonResponse<Void> response = new CommonResponse<>(
                "관리자로 권한 변경 성공 🎉",
                HttpStatus.OK.value(),
                null);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    // USER : 프로필 가져오기
    @GetMapping("profiles")
    public ResponseEntity<CommonResponse<List<UserResponseDto>>> getUserProfiles(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null || userPrincipal.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<UserResponseDto> userList = userService.getUserProfiles(userPrincipal.getUser());
        CommonResponse<List<UserResponseDto>> response = new CommonResponse<>(
                "프로필 조회 성공 🎉",
                HttpStatus.OK.value(),
                userList);

        return ResponseEntity.ok(response);

    }

    // 유저 프로필 수정
    @PatchMapping("/profiles/{userId}")
    public ResponseEntity<CommonResponse<UserResponseDto>> updateProfilesNickname(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long userId,
            @RequestPart(value = "profile") ProfileRequestDto ProfileRequestDto,
            @RequestPart(value = "file", required = false) List<MultipartFile> fileList)
    {

        User currentUser = userPrincipal.getUser();

        if (currentUser.getRole() != User.Role.ADMIN && !currentUser.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        CommonResponse<UserResponseDto> commonResponse = new CommonResponse<>(
                "프로필 변경 성공 🎉",
                HttpStatus.OK.value(),
                userService.updateProfiles(userId, ProfileRequestDto, fileList));

        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);

    }

    // USER : 비밀번호 수정
    @PatchMapping("/profiles/{userId}/pw")
    public ResponseEntity<CommonResponse<?>> updatePassword(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long userId,
            @RequestBody PasswordRequestDto passwordRequestDto) {

        User currentUser = userPrincipal.getUser();

        if (!currentUser.getRole().equals(User.Role.ADMIN) && !currentUser.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            passwordVeriFication.updatePassword(userId, passwordRequestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResponse<>(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST.value(),
                    null));

        }

        UserResponseDto updatedUser = userService.getUserProfile(userId);

        CommonResponse<UserResponseDto> response = new CommonResponse<>(
                "비밀번호 변경 성공 🎉",
                HttpStatus.OK.value(),
                updatedUser);

        return ResponseEntity.ok(response);

    }

}
