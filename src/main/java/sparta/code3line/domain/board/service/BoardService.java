package sparta.code3line.domain.board.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.bytebuddy.implementation.bytecode.Throw;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sparta.code3line.common.CommonResponse;
import sparta.code3line.common.exception.CustomException;
import sparta.code3line.common.exception.ErrorCode;
import sparta.code3line.domain.board.dto.BoardRequestDto;
import sparta.code3line.domain.board.dto.BoardResponseDto;
import sparta.code3line.domain.board.entity.Board;
import sparta.code3line.domain.board.repository.BoardRepository;
import sparta.code3line.domain.user.entity.User;
import sparta.code3line.domain.user.repository.UserRepository;
import sparta.code3line.jwt.JwtService;

@Slf4j(topic = "BoardService")
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    // 헤더에 담긴 토큰에서 유저정보 빼오는 메서드.
    private User getUserFromRequest(HttpServletRequest servletRequest) {
        log.info("getUserFromRequest 메서드 실행.");
        // 헤더에서 토큰 빼오기
        String token = servletRequest.getHeader(JwtService.AUTHORIZATION_HEADER);
        if (token == null) {
            log.error("토큰 없음");
            throw new CustomException(ErrorCode.TOKEN_INVALID);
        }
        if (!token.startsWith(JwtService.BEARER_PREFIX)) {
            log.error("!token.startsWith(JwtService.BEARER_PREFIX) 실행 결과 토큰 유효하지 않음. : ");
            throw new CustomException(ErrorCode.TOKEN_INVALID);
        }
        token = token.substring(JwtService.BEARER_PREFIX.length());

        // 빼온 토큰에서 username 빼오기
        String username = jwtService.extractUsername(token);

        // userRepository 에서 username 으로 해당 유저 찾기.
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USERNAME_NOT_FOUND));
    }

    // 게시글 추가 메서드.
    public ResponseEntity<CommonResponse<BoardResponseDto>> addBoard(
            HttpServletRequest servletRequest,
            BoardRequestDto requestDto) {
        log.info("addBoard 메서드 실행");
        User user = getUserFromRequest(servletRequest);

        Board board = new Board(user, requestDto.getTitle(), requestDto.getContent());
        Board addBoard = boardRepository.save(board);

        BoardResponseDto responseDto = new BoardResponseDto(
                addBoard.getBoardId(),
                addBoard.getTitle(),
                addBoard.getContent(),
                addBoard.getCreatedAt()
        );
        CommonResponse<BoardResponseDto> commonResponse = new CommonResponse<>(
                "게시글 등록 성공",
                200,
                responseDto
        );
        log.info("addBoard 메서드 성공");
        return ResponseEntity.ok(commonResponse);
    }


}
