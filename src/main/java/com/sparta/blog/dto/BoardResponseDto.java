package com.sparta.blog.dto;

import com.sparta.blog.entity.Board;
import com.sparta.blog.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class BoardResponseDto {
    private String username;
    private String title;
    private String contents;
    private Long id;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private List<CommentResponseDto> commentList = new ArrayList<>();

    public BoardResponseDto(Board board) {
        this.username = board.getUser().getUsername();
        this.title = board.getTitle();
        this.id = board.getId();
        this.contents = board.getContents();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        for (Comment comment : board.getCommentList()) {
            this.commentList.add(new CommentResponseDto(comment));
        }

        Collections.reverse(commentList);

    }
}
