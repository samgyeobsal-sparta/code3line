package sparta.code3line.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.code3line.common.exception.CustomException;
import sparta.code3line.common.exception.ErrorCode;
import sparta.code3line.domain.user.dto.UserRequestDto;
import sparta.code3line.domain.user.dto.UserResponseDto;
import sparta.code3line.domain.user.entity.User;
import sparta.code3line.domain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
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

        if (userToBlock.isBlock()) {
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

        if (userToAdmin.isUserToAdmin()) {
            throw new CustomException(ErrorCode.ALREADY_ADMIN);
        }

        if (userToAdmin.getStatus() == User.Status.DELETED) {
            throw new CustomException(ErrorCode.ALREADY_DELETED);
        }

        if (userToAdmin.getStatus() == User.Status.BLOCK) {
            throw new CustomException(ErrorCode.ALREADY_BLOCK);
        }

        userToAdmin.updateRole(User.Role.ADMIN);
    }

    // 닉네임 변경
    @Transactional
    public UserResponseDto updateProfilesNickname(Long userId, UserRequestDto userRequestDto) {

        Optional<User> userOptional = userRepository.findById(userId);

        if (!userOptional.isPresent()) {
            throw new CustomException(ErrorCode.USERNAME_NOT_FOUND);
        }

        User user = userOptional.get();

        if (userRequestDto.getNickname() != null && !userRequestDto.getNickname().isEmpty()) {
            user.setNickname(userRequestDto.getNickname());
            userRepository.save(user);
            logger.info("닉네임 변경 완료");
        }

        return new UserResponseDto(user);

    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserProfile(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USERNAME_NOT_FOUND));

        return new UserResponseDto(user);

    }

    // 사용자 프로필 가져오기
    @Transactional(readOnly = true)
    public List<UserResponseDto> getUserProfiles(User currentUser) {

        List<UserResponseDto> userResponseDto = new ArrayList<>();

        if (currentUser.getRole() == User.Role.ADMIN) {
            List<User> users = userRepository.findAll();
            for (User user : users) {
                userResponseDto.add(new UserResponseDto(user));
            }
        }
        else if (currentUser.getRole() == User.Role.NORMAL) {
            User user = userRepository.findById(currentUser.getId())
                    .orElseThrow(() -> new CustomException(ErrorCode.USERNAME_NOT_FOUND));
            userResponseDto.add(new UserResponseDto(user));
        }

        return userResponseDto;

    }

}
