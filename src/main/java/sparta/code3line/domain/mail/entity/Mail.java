package sparta.code3line.domain.mail.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.code3line.common.Timestamp;
import sparta.code3line.domain.user.entity.User;

@Getter
@NoArgsConstructor
@Entity
public class Mail extends Timestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mail_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String code;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Mail(User user) {
        this.user = user;
        this.email = user.getEmail();
        this.code = "initcode";
    }

    public void mailAddCode(String code) {
        this.code = code;
    }
}
