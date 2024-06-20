package sparta.code3line.domain.follow.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.code3line.common.Timestamp;
import sparta.code3line.domain.user.entity.User;

/**
 * user 한 명이 여러명을 팔로잉 할 수 있고, 여러명의 팔로우를 가질 수 있음 : 1:n 패턴
 */
@Entity
@Getter
@NoArgsConstructor
public class Follow extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "following_user_id")
    private User following;

    @ManyToOne
    @JoinColumn(name = "follower_user_id")
    private User follower;

    @Builder
    public Follow(User followingUser, User followerUser)
    {
        this.follower = followerUser;
        this.following =  followingUser;
    }
}
