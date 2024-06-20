package sparta.code3line.domain.comment.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CommentResponseDto {

    private Long userId;
    private Long boardId;
    private String contents;

    @Builder
    public CommentResponseDto(Long userId, Long boardId, String contents) {
        this.userId = userId;
        this.boardId = boardId;
        this.contents = contents;
    }
}
