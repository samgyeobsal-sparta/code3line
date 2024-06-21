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
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = Logger.getLogger(UserService.class.getName());

    // 닉네임 변경
    @Transactional
    public String updateProfilesNickname(UserRequestDto userRequestDto) {
        Optional<User> existingUserOptional = userRepository.findByUsername(userRequestDto.getUsername());
        if (!existingUserOptional.isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        User user = existingUserOptional.get();

        // 닉네임 변경 여부 확인 및 처리
        if (userRequestDto.getNickname() != null && !userRequestDto.getNickname().isEmpty()) {
            user.setNickname(userRequestDto.getNickname());
            userRepository.save(user);
            logger.info("닉네임 변경 완료");
        }

        return "닉네임 수정 완료";
    }
}
