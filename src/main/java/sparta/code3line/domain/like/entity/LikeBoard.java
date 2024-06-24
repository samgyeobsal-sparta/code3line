package sparta.code3line.domain.like.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.code3line.common.Timestamp;
import sparta.code3line.domain.board.entity.Board;
import sparta.code3line.domain.user.entity.User;

@Getter
@Entity
@NoArgsConstructor
public class LikeBoard extends Timestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_board_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public LikeBoard(User user, Board board) {
        this.user = user;
        this.board = board;
    }

}
