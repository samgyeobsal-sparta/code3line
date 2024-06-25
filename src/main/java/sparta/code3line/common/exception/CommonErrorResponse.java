package sparta.code3line.common.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommonErrorResponse {
    private String msg;
    private int status;
    private String error;
    private String path;
    private LocalDateTime timestamp;
}
