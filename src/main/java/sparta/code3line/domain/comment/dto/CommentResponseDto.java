package sparta.code3line.domain.comment.dto;

import lombok.Builder;
import lombok.Data;
import sparta.code3line.domain.comment.entity.Comment;

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

    public CommentResponseDto(Comment comment) {
        this.userId = comment.getUser().getId();
        this.boardId = comment.getBoard().getBoardId();
        this.contents = comment.getContents();
    }
}
