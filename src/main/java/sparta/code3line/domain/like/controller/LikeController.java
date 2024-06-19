package sparta.code3line.domain.like.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import sparta.code3line.domain.like.service.LikeService;

@RestController
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
}
