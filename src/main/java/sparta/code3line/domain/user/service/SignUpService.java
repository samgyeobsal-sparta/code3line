package sparta.code3line.domain.user.service;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sparta.code3line.common.exception.CustomException;
import sparta.code3line.common.exception.ErrorCode;
import sparta.code3line.domain.user.dto.SignUpRequestDto;
import sparta.code3line.domain.user.entity.User;
import sparta.code3line.domain.user.repository.UserRepository;

import java.util.Optional;

@Service
@Getter
@RequiredArgsConstructor
public class SignUpService {

    private final static String ADMIN_CODE = "zhemtpwnfdpsmseoajflrkdlTek";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(SignUpRequestDto signUpRequestDto) {

        User.Role role = User.Role.NORMAL;

        // 존재 여부 확인
        if (isExist(signUpRequestDto.getUsername())) {
            throw new CustomException(ErrorCode.ALREADY_EXISTING_USER);
        }

        // 관리자 권한 부여 여부 확인
        if (isAdmin(signUpRequestDto.getAdmin())) {
            role = User.Role.ADMIN;
        }

        User user = User.builder()
                .username(signUpRequestDto.getUsername())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .email(signUpRequestDto.getEmail())
                .nickname(signUpRequestDto.getNickname())
                .role(role)
                .status(User.Status.INACTIVE)
                .build();

        return userRepository.save(user);

    }

    @Transactional
    public Void deleteUser(SignUpRequestDto signUpRequestDto) {

        User user = userRepository.findByUsername(signUpRequestDto.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.USERNAME_NOT_FOUND)
        );

        // 아이디와 비밀번호 검증 로직
        if (!passwordEncoder.matches(signUpRequestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        if (user.getStatus().equals(User.Status.DELETED)) {
            throw new CustomException(ErrorCode.ALREADY_DELETED);
        }

        // 사용자 상태를 탈퇴로 설정
        user.updateStatus(User.Status.DELETED);

        return null;

    }

    private boolean isExist(String username) {

        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent();

    }

    private boolean isAdmin(String adminCode) {

        if (adminCode != null) {
            return adminCode.equals(ADMIN_CODE);
        } else {
            return false;
        }

    }

}
