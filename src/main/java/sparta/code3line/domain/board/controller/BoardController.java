package sparta.code3line.domain.board.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sparta.code3line.common.CommonResponse;
import sparta.code3line.domain.board.dto.BoardRequestDto;
import sparta.code3line.domain.board.dto.BoardResponseDto;
import sparta.code3line.domain.board.service.BoardService;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // 게시글 추가.
    @PostMapping("/boards")
    public ResponseEntity<CommonResponse<BoardResponseDto>> addBoard(
            HttpServletRequest servletRequest,
            @RequestBody BoardRequestDto requestDto) {
        return boardService.addBoard(servletRequest, requestDto);
    }
}
