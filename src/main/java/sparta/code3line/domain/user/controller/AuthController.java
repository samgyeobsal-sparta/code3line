package sparta.code3line.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sparta.code3line.common.CommonResponse;
import sparta.code3line.domain.user.dto.LoginRequestDto;
import sparta.code3line.domain.user.dto.LoginResponseDto;
import sparta.code3line.domain.user.dto.ReIssueAccessTokenRequestDto;
import sparta.code3line.domain.user.service.AuthService;
import sparta.code3line.jwt.JwtService;
import sparta.code3line.security.UserPrincipal;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponseDto>> login(
            @RequestBody LoginRequestDto requestDto) {

        LoginResponseDto responseDto = authService.login(requestDto);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", responseDto.getAccessToken());

        CommonResponse<LoginResponseDto> response = new CommonResponse<LoginResponseDto>(
                "로그인 성공 🎉",
                HttpStatus.OK.value(),
                responseDto);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(response);

    }

    @PatchMapping("/logout")
    public ResponseEntity<CommonResponse<Void>> logout(
            @AuthenticationPrincipal UserPrincipal principal) {

        CommonResponse<Void> response = new CommonResponse<Void>(
                "로그아웃 성공 🎉",
                HttpStatus.OK.value(),
                authService.logout(principal.getUser()));

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PostMapping("/reissue")
    public ResponseEntity<CommonResponse<String>> reissueAccessToken(
            @RequestBody ReIssueAccessTokenRequestDto requestDto) {

        String token = authService.reissueAccessToken(requestDto);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);

        CommonResponse<String> response = new CommonResponse<>(
                "토큰 발급 성공 🎉",
                HttpStatus.OK.value(),
                token);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(response);

    }

}
