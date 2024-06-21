package sparta.code3line.domain.like.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LikeResponseDto {
    private Long boardId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
