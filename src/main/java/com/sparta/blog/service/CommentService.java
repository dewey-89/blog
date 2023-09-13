package com.sparta.blog.service;

import com.sparta.blog.dto.CommentRequestDto;
import com.sparta.blog.dto.CommentResponseDto;
import com.sparta.blog.entity.*;
import com.sparta.blog.repository.BoardRepository;
import com.sparta.blog.repository.CommentRepository;
import com.sparta.blog.repository.LikedCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final LikedCommentRepository likedCommentRepository;

    // 1. 댓글 작성
    @Transactional
    public ResponseEntity<CommentResponseDto> createComment(CommentRequestDto commentRequestDto, User user) {
        Board board = boardRepository.findById(commentRequestDto.getBoardId()).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        Comment comment = new Comment(commentRequestDto, board, user);

        commentRepository.save(comment);

        return ResponseEntity.status(200).body(new CommentResponseDto(comment));
    }

    // 2. 댓글 수정
    @Transactional
    public  ResponseEntity<CommentResponseDto> updateComment(Long commentId, CommentRequestDto commentRequestDto, User user) {
        Comment comment = findCommentById(commentId);

        if(!(user.getRole().equals(UserRoleEnum.ADMIN)||comment.getUser().getId().equals(user.getId()))){
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        comment.update(commentRequestDto);
        return ResponseEntity.status(200).body(new CommentResponseDto(comment));
    }

    // 3. 댓글 삭제
    @Transactional
    public ResponseEntity<String> deleteComment(Long commentId, User user) {
        Comment comment = findCommentById(commentId);

        if(!(user.getRole().equals(UserRoleEnum.ADMIN)||comment.getUser().getId().equals(user.getId()))){
            return ResponseEntity.status(400).body("msg: 권한이 없습니다., status: 400");
        }

        commentRepository.delete(comment);
        return ResponseEntity.status(200).body("msg: 댓글 삭제 성공, status: 200");
    }

    // 댓글 좋아요 API
    @Transactional
    public ResponseEntity<String> likeComment(Long commentId, User user) {
        Comment comment = findCommentById(commentId);
        Optional<LikedComment> likedCommentList = likedCommentRepository.findByCommentIdAndUserId(commentId, user.getId());

        if(likedCommentList.isPresent()){
            likedCommentRepository.delete(likedCommentList.get());
            return ResponseEntity.status(200).body("msg : 댓글 좋아요 취소 성공, statusCode : 200");
        }

        likedCommentRepository.save(new LikedComment(comment, user));
        return ResponseEntity.status(200).body("msg : 댓글 좋아요 성공, statusCode : 200");

    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );
    }


}
