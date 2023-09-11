package com.sparta.blog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class CommentRequestDto {
    private String comment;
    private Long boardId;
}
