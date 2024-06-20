package sparta.code3line.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sparta.code3line.common.CommonResponse;
import sparta.code3line.domain.user.dto.LoginRequestDto;
import sparta.code3line.domain.user.dto.LoginResponseDto;
import sparta.code3line.domain.user.service.AuthService;
import sparta.code3line.security.UserPrincipal;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public CommonResponse<LoginResponseDto> login(LoginRequestDto requestDto) throws IOException {

        LoginResponseDto responseDto = authService.login(requestDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", responseDto.getAccessToken());
        return new CommonResponse<LoginResponseDto>("로그인 성공", 200, responseDto);
    }

    @PatchMapping("/logout")
    public CommonResponse<Void> logout(@AuthenticationPrincipal UserPrincipal principal) {
        return new CommonResponse<Void>("로그아웃 성공", 204, authService.logout(principal));
    }

}
