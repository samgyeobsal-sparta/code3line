package sparta.code3line.domain.comment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sparta.code3line.common.exception.CustomException;
import sparta.code3line.common.exception.ErrorCode;
import sparta.code3line.domain.board.entity.Board;
import sparta.code3line.domain.board.repository.BoardRepository;
import sparta.code3line.domain.comment.dto.CommentRequestDto;
import sparta.code3line.domain.comment.dto.CommentResponseDto;
import sparta.code3line.domain.comment.entity.Comment;
import sparta.code3line.domain.comment.repository.CommentRepository;
import sparta.code3line.domain.user.entity.User;
import sparta.code3line.domain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    private final ModelMapper mapper = new ModelMapper();

    public CommentResponseDto createComment(Long boardId, User user, CommentRequestDto requestDto) {
        Board board = boardRepository.findByBoardId(boardId).orElseThrow(
                () -> new CustomException(ErrorCode.BOARD_NOT_FOUND)
        );

        Comment comment = Comment.builder()
                .user(user)
                .board(board)
                .contents(requestDto.getContents())
                .build();

        commentRepository.save(comment);

        return CommentResponseDto.builder()
                .userId(user.getId())
                .boardId(board.getBoardId())
                .contents(requestDto.getContents())
                .build();
    }

    public List<CommentResponseDto> readComments(Long boardId) {
        List<Comment> comments = commentRepository.findAllbyBoardId(boardId).orElse(null);
        List<CommentResponseDto> responseDtos = new ArrayList<>();

        if(comments != null) {
            for (Comment comment : comments) {
                responseDtos.add(new CommentResponseDto(comment));
            }
        }

        return responseDtos;
    }

    @Transactional
    public CommentResponseDto updateComment(Long boardId, Long commentId, User user, CommentRequestDto requestDto) {

        Comment comment = getComment(boardId, commentId, user);

        comment.updateContent(requestDto.getContents());
        return new CommentResponseDto(comment);
    }

    @Transactional
    public Void deleteComment(Long boardId, Long commentId, User user) {

        Comment comment = getComment(boardId, commentId, user);

        commentRepository.delete(comment);
        return null;
    }

    // comment 찾아오기
    private Comment getComment(Long boardId, Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ErrorCode.COMMENT_NOT_FOUND)
        );

        if (!comment.getBoard().getBoardId().equals(boardId) || !comment.getUser().equals(user)) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        return comment;
    }
}
