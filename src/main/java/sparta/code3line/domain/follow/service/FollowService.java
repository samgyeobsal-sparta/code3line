package sparta.code3line.domain.follow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sparta.code3line.common.exception.CustomException;
import sparta.code3line.domain.follow.dto.FollowResponseDto;
import sparta.code3line.domain.follow.entity.Follow;
import sparta.code3line.domain.follow.repository.FollowRepository;
import sparta.code3line.domain.user.entity.User;
import sparta.code3line.domain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static sparta.code3line.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 팔로우 기능
    public void followUser(Long followingUserId, User follower) {
        if (followingUserId.equals(follower.getId())) {
            throw new CustomException(USERNAME_NOT_FOUND);
        }

        Optional<Follow> checkFollow = followRepository.findByFollowingIdAndFollowerId(followingUserId, follower.getId());
        if (checkFollow.isPresent()) {
            throw new CustomException(ALREADY_FOLLOW);
        }

        User followingUser = findUser(followingUserId);
        Follow follow = new Follow(followingUser, follower);
        followRepository.save(follow);
    }

    // 언팔로우 기능
    public void unfollowUser(Long followingUserId, User follower) {
        Follow follow = followRepository.findByFollowingIdAndFollowerId(followingUserId, follower.getId())
                .orElseThrow(() -> new CustomException(NOT_FOLLOWED));

        followRepository.delete(follow);
    }

    // 팔로잉 목록 조회 기능
    public List<FollowResponseDto> getFollowings(Long userId) {
        User user = findUser(userId);
        List<Follow> followingList = user.getFollowingList();

        return followingList.stream()
                .map(FollowResponseDto::new)
                .collect(Collectors.toList());
    }

    // 사용자 조회 메서드 (
    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USERNAME_NOT_FOUND));
    }
}

