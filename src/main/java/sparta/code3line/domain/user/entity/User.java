package sparta.code3line.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String socialId;

    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private Status status;

    @Builder
    public User(String username, String password, String email, String nickname, String socialId, Role role, Status status)
    {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.socialId = socialId;
        this.role = role;
        this.status = status;
    }
    public enum Role {
        NORMAL,
        ADMIN;
    }

    public enum Status {
        INACTIVE,
        ACTIVE,
        DELETED,
        BLOCK;
    }
}
