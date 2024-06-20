package sparta.code3line.domain.follow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import sparta.code3line.domain.follow.entity.Follow;

@Getter
@Setter
public class FollowResponseDto {

    @JsonProperty("following_user_id")
    private Long followingUserId;
    @JsonProperty("follower_user_id")
    private Long followerUserId;

    public FollowResponseDto(Follow follow)
    {
        this.followingUserId = follow.getFollowing().getId();
        this.followerUserId = follow.getFollower().getId();
    }

    public FollowResponseDto(Long followingUserId, Long followerUserId) {
        this.followingUserId = followingUserId;
        this.followerUserId = followerUserId;
    }
}
