package sparta.code3line.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static sparta.code3line.domain.user.entity.Token.Tokentype.REFRESH;
import static sparta.code3line.domain.user.entity.Token.Tokentype.VERIFICATION;

@Entity
@Getter
@RequiredArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Tokentype tokenType;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Token(String token, String tokenType, User user) {
        this.token = token;
        this.tokenType = tokenType.equals("Refresh") ? REFRESH : VERIFICATION;
        this.user = user;
    }

    public enum Tokentype {
        VERIFICATION,
        REFRESH
    }

    public String updateToken(String token) {
        return this.token = token;
    }
}
