package sparta.code3line.domain.follow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import sparta.code3line.domain.follow.entity.Follow;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class FollowResponseDto {

    @JsonProperty("following_user_id")
    private Long followingUserId;
    @JsonProperty("follower_user_id")
    private Long followerUserId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedAt;

    public FollowResponseDto(Follow follow)
    {
        this.followingUserId = follow.getFollowing().getId();
        this.followerUserId = follow.getFollower().getId();
        this.createdAt = follow.getCreatedAt();
        this.modifiedAt = follow.getModifiedAt();
    }

    public FollowResponseDto(Long followingUserId, Long followerUserId, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.followingUserId = followingUserId;
        this.followerUserId = followerUserId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
