package sparta.code3line.domain.board.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardResponseDto {
    private Long boardId;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public BoardResponseDto(
            Long boardId,
            String title,
            String content,
            LocalDateTime createdAt) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }
}
