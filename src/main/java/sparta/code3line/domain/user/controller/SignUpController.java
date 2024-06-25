package sparta.code3line.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sparta.code3line.common.CommonResponse;
import sparta.code3line.domain.user.dto.SignUpRequestDto;
import sparta.code3line.domain.user.entity.User;
import sparta.code3line.domain.user.service.SignUpService;
import sparta.code3line.security.UserPrincipal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<User>> createUser(
            @Valid @RequestBody SignUpRequestDto requestDto) {

        CommonResponse<User> response = new CommonResponse<>(
                "회원가입 성공 🎉",
                HttpStatus.CREATED.value(),
                signUpService.createUser(requestDto));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PutMapping("/signout")
    public ResponseEntity<CommonResponse<Void>> deleteUser(
            @AuthenticationPrincipal UserPrincipal principal) {

        CommonResponse<Void> response = new CommonResponse<>(
                "회원탈퇴 성공 (┬┬﹏┬┬) 😱😢😭",
                HttpStatus.OK.value(),
                signUpService.deleteUser(principal.getUser()));

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
}
