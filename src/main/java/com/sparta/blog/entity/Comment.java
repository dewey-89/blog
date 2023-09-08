package com.sparta.blog.entity;

import com.sparta.blog.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "comment")
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    public Comment(CommentRequestDto commentRequestDto, Board board, User user) {
        this.comment = commentRequestDto.getComment();
        this.board = board;
        this.user = user;
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.comment = commentRequestDto.getComment();
    }
}
