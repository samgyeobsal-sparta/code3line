package sparta.code3line.domain.follow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.code3line.domain.follow.repository.FollowRepository;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
}
