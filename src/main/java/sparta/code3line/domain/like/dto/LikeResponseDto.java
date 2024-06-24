package sparta.code3line.domain.like.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import sparta.code3line.domain.like.entity.LikeBoard;
import sparta.code3line.domain.like.entity.LikeComment;

import java.time.LocalDateTime;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LikeResponseDto {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public LikeResponseDto(LikeBoard like) {
        id = like.getBoard().getId();
        createdAt = like.getCreatedAt();
        modifiedAt = like.getModifiedAt();
    }

    public LikeResponseDto(LikeComment like) {
        id = like.getComment().getId();
        createdAt = like.getCreatedAt();
        modifiedAt = like.getModifiedAt();
    }
}
