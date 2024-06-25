package sparta.code3line.domain.board.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.code3line.common.Timestamp;

@Entity
@Getter
@NoArgsConstructor
public class BoardFiles extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public BoardFiles (String file, Board board) {
        this.file = file;
        this.board = board;
    }

}
