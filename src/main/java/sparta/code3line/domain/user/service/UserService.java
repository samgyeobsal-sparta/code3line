package sparta.code3line.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.code3line.common.exception.CustomException;
import sparta.code3line.common.exception.ErrorCode;
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


    @Transactional
    public void deleteUser(Long userId, User currentUser) {
        User userToDelete = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND)
        );

        if (!currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new CustomException(ErrorCode.NOT_DELETED);
        }

        if (userToDelete.isDeleted()) {
            throw new CustomException(ErrorCode.ALREADY_DELETED);
        }

        userToDelete.updateStatus(User.Status.DELETED);
    }

    @Transactional
    public void blockUser(Long userId, User currentUser) {
        User userToBlock = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND)
        );

        if (!currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new CustomException(ErrorCode.NOT_BLOCK);
        }

        if(userToBlock.isBlock())
        {
            throw new CustomException(ErrorCode.ALREADY_BLOCK);
        }
        userToBlock.updateStatus(User.Status.BLOCK);

    }

    @Transactional
    public void adminUser(Long userId, User currentUser) {
        User userToAdmin = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND)
        );
        if (!currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new CustomException(ErrorCode.NOT_ADMIN);
        }

        if(userToAdmin.isUserToAdmin())
        {
            throw new CustomException(ErrorCode.ALREADY_ADMIN);
        }
        userToAdmin.updateRole(User.Role.ADMIN);

    }

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
