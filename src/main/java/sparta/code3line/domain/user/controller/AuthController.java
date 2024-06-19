package sparta.code3line.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sparta.code3line.common.CommonResponse;
import sparta.code3line.domain.user.dto.LoginRequestDto;
import sparta.code3line.domain.user.dto.LoginResponseDto;
import sparta.code3line.domain.user.service.AuthService;
import sparta.code3line.security.UserPrincipal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    AuthService authService;

    @PostMapping("/login")
    public CommonResponse<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        return new CommonResponse<LoginResponseDto>("로그인 성공", 200, authService.login(requestDto));
    }

    @PatchMapping("/logout")
    public CommonResponse<Void> logout(@AuthenticationPrincipal UserPrincipal principal) {
        return new CommonResponse<Void>("로그아웃 성공", 204, authService.logout(principal));
    }

}
