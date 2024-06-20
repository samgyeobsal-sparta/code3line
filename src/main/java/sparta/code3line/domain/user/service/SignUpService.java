package sparta.code3line.domain.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sparta.code3line.domain.user.dto.LoginUpRequestDto;
import sparta.code3line.domain.user.dto.SignUpRequestDto;
import sparta.code3line.domain.user.entity.User;
import sparta.code3line.domain.user.repository.UserRepository;

import java.util.Optional;
import java.util.logging.Logger;

@Service
@Getter
@RequiredArgsConstructor
public class SignUpService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = Logger.getLogger(SignUpService.class.getName());
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
                .nickname(signUpRequestDto.getNickname())
                .role(User.Role.NORMAL)
                .build();

        userRepository.save(user);
        return "회원가입이 완료되었습니다.";
    }


    public String deleteUser(SignUpRequestDto signUpRequestDto) {
        Optional<User> existingUserOptional = userRepository.findByUsername(signUpRequestDto.getUsername());

        // 사용자가 존재하지 않으면 예외 발생
        if (!existingUserOptional.isPresent()) {
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }

        User user = existingUserOptional.get();

        // 아이디와 비밀번호 검증 로직
        if (!passwordEncoder.matches(signUpRequestDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        if (user.getStatus() == User.Status.DELETED) {
            throw new IllegalArgumentException("이미 탈퇴한 사용자입니다.");
        }

        // 사용자 상태를 탈퇴로 설정
        User updatedUser = User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .status(User.Status.DELETED)
                .email(user.getEmail())
                .nickname(user.getNickname())
                .role(user.getRole())
                .build();

        userRepository.save(updatedUser);
        logger.info("사용자 " + updatedUser.getUsername() + "가 성공적으로 탈퇴되었습니다.");
        return "회원탈퇴가 완료되었습니다 " + updatedUser.getUsername();
    }

}
