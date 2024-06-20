package sparta.code3line.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //follow

    ALREADY_FOLLOW(HttpStatus.BAD_REQUEST,"이미 팔로우했습니다."),
    NOT_FOLLOWED(HttpStatus.BAD_REQUEST,"팔로우 하지 않았습니다."),
    // basic
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD REQUEST"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT FOUND"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR"),
    // 필요시 직접 추가 (위 참고 해서)

    // Entity Not Found
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),

    // Token
    NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND, "토큰 없음."),
    TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "토큰 만료됨."),
    TOKEN_INVALID(HttpStatus.BAD_REQUEST, "토큰 유효하지 않음");


    private final HttpStatus status;
    private final String message;
}
