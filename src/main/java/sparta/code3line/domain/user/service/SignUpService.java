package sparta.code3line.domain.user.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sparta.code3line.domain.user.dto.SignUpRequestDto;
import sparta.code3line.domain.user.entity.User;
import sparta.code3line.domain.user.repository.UserRepository;

import java.util.Optional;

@Service
@Getter
@RequiredArgsConstructor
public class SignUpService {

     private final UserRepository userRepository;
     private final PasswordEncoder passwordEncoder;
     private SignUpRequestDto signUpRequestDto;


    public String addUser(SignUpRequestDto signUpRequestDto) {
        Optional<User> existingUserOptional = userRepository.findByUsername(signUpRequestDto.getUsername());

        // 사용자가 이미 존재하면 예외 발생
        if (existingUserOptional.isPresent()) {
            throw new RuntimeException("이미 존재하는 사용자입니다.");
        }

        User user = User.builder()
                .username(signUpRequestDto.getUsername())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .status(User.Status.ACTIVE)
                .email("test123")
                .nickname("test1234")
                .role(User.Role.NORMAL)
                .build();

        userRepository.save(user);
        return "회원가입이 완료되었습니다.";
    }




}
