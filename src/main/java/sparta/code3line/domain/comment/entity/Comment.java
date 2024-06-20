package sparta.code3line.domain.comment.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.code3line.common.Timestamp;
import sparta.code3line.domain.board.entity.Board;
import sparta.code3line.domain.user.entity.User;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Builder
    public Comment(String contents, User user, Board board) {
        this.contents = contents;
        this.user = user;
        this.board = board;
    }


    public void updateContent (String contents) {
        this.contents = contents;
    }
}
