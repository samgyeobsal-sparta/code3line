package sparta.code3line.domain.like.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sparta.code3line.common.exception.CustomException;
import sparta.code3line.common.exception.ErrorCode;
import sparta.code3line.domain.board.entity.Board;
import sparta.code3line.domain.board.repository.BoardRepository;
import sparta.code3line.domain.comment.entity.Comment;
import sparta.code3line.domain.comment.repository.CommentRepository;
import sparta.code3line.domain.like.dto.LikeResponseDto;
import sparta.code3line.domain.like.entity.LikeBoard;
import sparta.code3line.domain.like.entity.LikeComment;
import sparta.code3line.domain.like.repository.LikeBoardRepository;
import sparta.code3line.domain.like.repository.LikeCommentRepository;
import sparta.code3line.domain.user.entity.User;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final LikeBoardRepository likeBoardRepository;
    private final LikeCommentRepository likeCommentRepository;

    public LikeResponseDto createLikeBoard(Long id, User user) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND)
        );

        if(Objects.equals(board.getUser().getId(), user.getId())) {
            throw new CustomException(ErrorCode.LIKE_ME);
        }

        if(likeBoardRepository.findByUserIdAndBoardId(user.getId(), board.getId()).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_LIKE);
        }

        LikeBoard likeBoard = LikeBoard.builder()
                .user(user)
                .board(board)
                .build();

        likeBoard = likeBoardRepository.save(likeBoard);
        return modelMapper.map(likeBoard, LikeResponseDto.class);
    }

    public LikeResponseDto deleteLikeBoard(Long id, User user) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND)
        );

        LikeBoard likeBoard = likeBoardRepository.findByUserIdAndBoardId(user.getId(), board.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_LIKE)
        );

        likeBoardRepository.delete(likeBoard);
        return modelMapper.map(likeBoard, LikeResponseDto.class);
    }

    public LikeResponseDto createLikeComment(Long id, User user) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND)
        );

        if(Objects.equals(comment.getUser().getId(), user.getId())) {
            throw new CustomException(ErrorCode.LIKE_ME);
        }

        if(likeCommentRepository.findByUserIdAndCommentId(user.getId(), comment.getId()).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_LIKE);
        }

        LikeComment likeComment = LikeComment.builder()
                .user(user)
                .comment(comment)
                .build();

        likeComment = likeCommentRepository.save(likeComment);
        return modelMapper.map(likeComment, LikeResponseDto.class);
    }

    public LikeResponseDto deleteLikeComment(Long id, User user) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND)
        );

        LikeComment likeComment = likeCommentRepository.findByUserIdAndCommentId(user.getId(), comment.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_LIKE)
        );

        likeCommentRepository.delete(likeComment);
        return modelMapper.map(likeComment, LikeResponseDto.class);
    }
}
