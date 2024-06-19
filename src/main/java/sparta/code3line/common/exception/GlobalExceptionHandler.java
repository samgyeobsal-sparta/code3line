package sparta.code3line.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // common error
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonErrorResponse> handleException(final CustomException exception, final HttpServletRequest request) {
        return ResponseEntity.status(exception.getStatusCode()).body(
                CommonErrorResponse.builder()
                        .msg(exception.getMessage())
                        .status(exception.getStatusCode().value())
                        .error(exception.getStatusCode().getReasonPhrase())
                        .path(request.getRequestURI())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // bean validation error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonErrorResponse> handleException(final MethodArgumentNotValidException exception, final HttpServletRequest request) {
        return ResponseEntity.status(exception.getStatusCode()).body(
                CommonErrorResponse.builder()
                        .msg(exception.getMessage())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .path(request.getRequestURI())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
