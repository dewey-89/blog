package com.sparta.blog.controller;

import com.sparta.blog.dto.CommentRequestDto;
import com.sparta.blog.dto.CommentResponseDto;
import com.sparta.blog.security.UserDetailsImpl;
import com.sparta.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    //1. 댓글 작성 API
    @PostMapping("/comment")
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(commentRequestDto, userDetails.getUser());
    }

    //2. 댓글 수정 API
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(commentId, commentRequestDto, userDetails.getUser());
    }

    //3. 댓글 삭제 API
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(commentId, userDetails.getUser());
    }

    // 4. 댓글 좋아요 API
    @PostMapping("/comment/{commentId}/like")
    public ResponseEntity<String> likeBoard(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.likeComment(commentId, userDetails.getUser());
    }

 }



