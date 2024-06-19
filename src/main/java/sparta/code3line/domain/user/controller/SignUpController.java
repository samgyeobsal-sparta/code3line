package sparta.code3line.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sparta.code3line.domain.user.dto.SignUpRequestDto;
import sparta.code3line.domain.user.service.SignUpService;
import sparta.code3line.domain.user.service.UserService;

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;
    private final UserService userService;

//    @PostMapping("/user/sign")
//    public String addUser(@Valid @RequestBody SignUpRequestDto requestDto){
//
//        return signUpService.addUser(requestDto);
//    }

}
