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
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    //1. 댓글 작성
    @PostMapping("/{boardId}/comment")
    public CommentResponseDto createComment(@PathVariable Long boardId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(boardId, commentRequestDto, userDetails.getUser());
    }

    //2. 댓글 수정
    @PutMapping("/{boardId}/comment/{commentId}")
    public CommentResponseDto updateComment(@PathVariable Long boardId, @PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(boardId, commentId, commentRequestDto, userDetails.getUser());
    }

 }



