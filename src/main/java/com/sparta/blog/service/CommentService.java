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
    public CommentResponseDto createComment(Long boardId, CommentRequestDto commentRequestDto, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        Comment comment = new Comment(commentRequestDto, board, user);

        return new CommentResponseDto(comment);
    }

    // 2. 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long boardId, Long commentId, CommentRequestDto commentRequestDto, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );

        if(user.getRole().equals(UserRoleEnum.ADMIN)){
            comment.update(commentRequestDto, user);
        }

        return new CommentResponseDto(comment);
    }
}
