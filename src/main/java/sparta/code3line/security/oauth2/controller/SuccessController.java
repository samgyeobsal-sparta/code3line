package sparta.code3line.security.oauth2.controller;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sparta.code3line.common.CommonResponse;
import sparta.code3line.domain.user.dto.LoginResponseDto;
import sparta.code3line.security.UserPrincipal;

@RestController
public class SuccessController {


}
