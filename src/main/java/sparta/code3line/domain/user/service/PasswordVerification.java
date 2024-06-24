package sparta.code3line.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.code3line.domain.user.dto.UserRequestDto;
import sparta.code3line.domain.user.entity.User;
import sparta.code3line.domain.user.repository.UserRepository;

import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class PasswordVerification {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = Logger.getLogger(PasswordVerification.class.getName());

    // 비밀번호 변경
    @Transactional
    public void updatePassword(Long userId, UserRequestDto userRequestDto) {
        Optional<User> existingUserOptional = userRepository.findById(userId);
        if (!existingUserOptional.isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        User user = existingUserOptional.get();

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
        }

        // 새 비밀번호가 현재 비밀번호 및 최근 사용한 세 개의 비밀번호와 다른지 확인
        String newpassword = userRequestDto.getNewpassword();
        if (passwordEncoder.matches(newpassword, user.getPassword())) {
            throw new IllegalArgumentException("새 비밀번호는 현재 비밀번호와 동일할 수 없습니다.");
        }

        for (String previousPassword : user.getPreviousPasswords()) {
            if (passwordEncoder.matches(newpassword, previousPassword)) {
                throw new IllegalArgumentException("새 비밀번호는 최근 사용한 비밀번호와 동일할 수 없습니다.");
            }
        }

        // 이전 비밀번호를 저장
        user.addPreviousPassword(user.getPassword());

        // 새 비밀번호를 설정
        user.setPassword(passwordEncoder.encode(newpassword));

        userRepository.save(user);
        logger.info("비밀번호 변경 완료");
    }

}
