package sparta.code3line.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sparta.code3line.common.Timestamp;
import sparta.code3line.domain.follow.entity.Follow;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@RequiredArgsConstructor
public class User extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = true)
    private String email;

    @Column(nullable = true)
    private String nickname;

    @Column
    private String socialId;

    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private Status status;

    @OneToMany(mappedBy = "following")
    private List<Follow> followerList;

    @OneToMany(mappedBy = "follower")
    private List<Follow> followingList;

    @ElementCollection
    @CollectionTable(name = "user_previous_passwords", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "password")
    private List<String> previousPasswords = new ArrayList<>();


    @Builder
    public User(String username, String password, String email, String nickname, String socialId, Role role, Status status) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.socialId = socialId;
        this.role = role;
        this.status = status;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }
    public void addPreviousPassword(String password) {
        if (previousPasswords.size() >= 3) {
            previousPasswords.remove(0);
        }
        previousPasswords.add(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Role {
        NORMAL("NORMAL"),
        ADMIN("ADMIN");

        private final String roleName;
    }

    public enum Status {
        INACTIVE,
        ACTIVE,
        DELETED,
        BLOCK;
    }
}
