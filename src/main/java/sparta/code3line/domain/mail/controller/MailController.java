package sparta.code3line.domain.mail.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparta.code3line.common.CommonResponse;
import sparta.code3line.domain.mail.dto.MailRequestDto;
import sparta.code3line.domain.mail.dto.VerifyRequestDto;
import sparta.code3line.domain.mail.service.MailService;

@Slf4j(topic = "mailController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/email")
public class MailController {

    private final MailService mailService;

    @PostMapping
    public ResponseEntity<CommonResponse<Void>> sendMail(@RequestBody MailRequestDto requestDto) {
        CommonResponse<Void> response = new CommonResponse<>(
                requestDto.getEmail() + "로 인증 메일이 전송되었습니다.",
                204,
                mailService.sendMail(requestDto));

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @PutMapping("/verification")
    public ResponseEntity<CommonResponse<Void>> verifyMail(@RequestBody VerifyRequestDto requestDto) {
        CommonResponse<Void> response = new CommonResponse<>(
                requestDto.getEmail() + "이 인증되었습니다.",
                204,
                mailService.verifyMail(requestDto));

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
