package com.sparta.blog.service;

import com.sparta.blog.dto.CommentRequestDto;
import com.sparta.blog.dto.CommentResponseDto;
import com.sparta.blog.entity.Board;
import com.sparta.blog.entity.Comment;
import com.sparta.blog.entity.User;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.repository.BoardRepository;
import com.sparta.blog.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    // 1. 댓글 작성
    @Transactional
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, User user) {
        Board board = boardRepository.findById(commentRequestDto.getBoardId()).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        Comment comment = new Comment(commentRequestDto, board, user);

        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    // 2. 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto, User user) {
        Comment comment = findCommentById(commentId);

        if(user.getRole().equals(UserRoleEnum.ADMIN)){
            comment.update(commentRequestDto);
        }
        else if(comment.getUser().getId().equals(user.getId())){
            comment.update(commentRequestDto);
        }
        else{
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        return new CommentResponseDto(comment);
    }


    public ResponseEntity<String> deleteComment(Long commentId, User user) {
        Comment comment = findCommentById(commentId);

        if(user.getRole().equals(UserRoleEnum.ADMIN)){
            commentRepository.delete(comment);
        }
        else if(comment.getUser().getId().equals(user.getId())){
            commentRepository.delete(comment);
        }
        else{
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        return ResponseEntity.status(200).body("msg: 댓글 삭제 성공, status: 200");
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );
    }
}
