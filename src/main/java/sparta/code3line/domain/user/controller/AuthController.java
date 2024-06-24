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
import sparta.code3line.domain.user.service.AuthService;
import sparta.code3line.security.UserPrincipal;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponseDto>> login(
            @RequestBody LoginRequestDto requestDto) throws IOException {

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

}
