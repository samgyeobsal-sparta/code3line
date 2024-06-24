package sparta.code3line.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {


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

    // User
    ALREADY_EXISTING_USER(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.FORBIDDEN, "비밀번호가 일치하지 않습니다."),
    NOT_VERIFIED(HttpStatus.FORBIDDEN, "검증이 되지 않은 사용자입니다."),
    ALREADY_DELETED(HttpStatus.FORBIDDEN, "이미 탈퇴된 사용자입니다."),

    // follow
    ALREADY_FOLLOW(HttpStatus.BAD_REQUEST,"이미 팔로우했습니다."),
    NOT_FOLLOWED(HttpStatus.BAD_REQUEST,"팔로우 하지 않았습니다."),
    NOT_FOLLOW_POST(HttpStatus.BAD_REQUEST,"팔로우 대상자가 게시글을 작성하지 않았습니다."),
    NOT_FOLLOWED_ID(HttpStatus.BAD_REQUEST,"팔로우 하려는 ID를 찾을 수 없습니다."),
    NOT_FOLLOW(HttpStatus.BAD_REQUEST,"본인은 팔로우 할 수 없습니다."),

    // Token
    NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND, "토큰 없음."),
    TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "토큰 만료됨."),
    TOKEN_INVALID(HttpStatus.BAD_REQUEST, "토큰 유효하지 않음"),

    // 좋아요
    LIKE_ME(HttpStatus.BAD_REQUEST, "자기 자신이 작성한 글에 좋아요 할 수 없습니다."),
    ALREADY_LIKE(HttpStatus.BAD_REQUEST, "이미 좋아요 하였습니다."),
    NOT_LIKE(HttpStatus.BAD_REQUEST, "좋아요 하지 않았습니다."),

    // 게시글
    USER_DIFFERENT(HttpStatus.BAD_REQUEST, "다른 사용자의 게시물입니다."),

    // 파일
    FILE_IS_EMPTY(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),
    FILE_NAME_INVALID(HttpStatus.BAD_REQUEST, "잘못된 파일명입니다."),
    IO_EXCEPTION_ON_UPLOAD(HttpStatus.BAD_REQUEST, "업로드에 문제가 발생했습니다."),
    EXTENSION_IS_EMPTY(HttpStatus.BAD_REQUEST, "확장자명이 비어있습니다."),
    EXTENSION_INVALID(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "잘못된 확장자명입니다."),
    IMAGE_SIZE_INVALID(HttpStatus.PAYLOAD_TOO_LARGE, "이미지 파일 크기는 10MB를 초과할 수 없습니다."),
    VIDEO_SIZE_INVALID(HttpStatus.PAYLOAD_TOO_LARGE, "비디오 파일 크기는 200MB를 초과할 수 없습니다."),
    PUT_OBJECT_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "s3 업로드에 문제가 발생했습니다."),
    IO_EXCEPTION_ON_IMAGE_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "s3 삭제에 문제가 발생했습니다.");


    private final HttpStatus status;
    private final String message;
}
