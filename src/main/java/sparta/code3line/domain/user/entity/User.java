package sparta.code3line.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sparta.code3line.common.Timestamp;
import sparta.code3line.common.exception.CustomException;
import sparta.code3line.common.exception.ErrorCode;

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

    @Column(name = "profile_img")
    private String profileImg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

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

    public void updateProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void updateStatus(Status status) {
        if(this.status == Status.DELETED)
        {
            throw new CustomException(ErrorCode.ALREADY_DELETED);
        }
        if(this.status == Status.BLOCK)
        {
            throw new CustomException(ErrorCode.ALREADY_BLOCK);
        }
        this.status = status;
    }


    public void updateRole(Role role)
    {
        if(this.role == Role.ADMIN)
        {
            throw new CustomException(ErrorCode.ALREADY_ADMIN);
        }
        this.role = role;
    }

    public boolean isUserToAdmin(){
        return this.role == Role.ADMIN;
    }

    public boolean isDeleted() {
        return this.status == Status.DELETED;
    }

    public boolean isBlock()
    {
        return this.status == Status.BLOCK;
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
