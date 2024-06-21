package sparta.code3line.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.code3line.domain.user.dto.UserRequestDto;
import sparta.code3line.domain.user.entity.User;
import sparta.code3line.domain.user.repository.UserRepository;
import sparta.code3line.domain.user.dto.UserResponseDto;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = Logger.getLogger(UserService.class.getName());

    // 프로필 변경
    @Transactional
    public String updateUserProfiles(UserRequestDto userRequestDto) {
        Optional<User> existingUserOptional = userRepository.findByUsername(userRequestDto.getUsername());
        if (!existingUserOptional.isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        User user = existingUserOptional.get();

        // 닉네임 변경 여부 확인 및 처리
        if (userRequestDto.getNickname() != null && !userRequestDto.getNickname().isEmpty()) {
            user.setNickname(userRequestDto.getNickname());
            logger.info("닉네임 변경 완료");
        }

        // 비밀번호 변경 여부 확인 및 처리
        if (userRequestDto.getPassword() != null && !userRequestDto.getPassword().isEmpty()) {
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

        return "수정 완료";
    }

    // 비밀번호 형식이 올바른지 확인하는 메서드
    private boolean isValidPasswordFormat(String password) {
        // 비밀번호가 8자 이상 15자리 이하, 숫자와 영문자 및 특수문자를 혼합하여 구성되어 있는지 확인
        String regex = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,15}$";
        return password.matches(regex);
    }
}
