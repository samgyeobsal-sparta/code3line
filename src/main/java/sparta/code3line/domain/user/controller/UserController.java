package sparta.code3line.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import sparta.code3line.domain.user.dto.UserRequestDto;
import sparta.code3line.domain.user.service.PasswordVerification;
import sparta.code3line.domain.user.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordVerification passwordVeriFication;

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
