package sparta.code3line.domain.board.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BoardRequestDto {
    @Size(max = 50, message = "제목은 최대 50자까지만 입력이 가능합니다.")
    private String title;

    @Size(max = 100, message = "내용은 최대 100자까지만 입력이 가능합니다.")
    private String content;
}
