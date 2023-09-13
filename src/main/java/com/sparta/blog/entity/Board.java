package com.sparta.blog.entity;

import com.sparta.blog.dto.BoardRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.List;

@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@Table(name = "board") // 매핑할 테이블의 이름을 지정
@NoArgsConstructor
public class Board extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "contents", nullable = false, length = 500)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<LikedBoard> LikedBoardList = new ArrayList<>();

    public Board(BoardRequestDto requestDto, User user) {
        this.contents = requestDto.getContents();
        this.title = requestDto.getTitle();
        this.user = user;
    }

    public void update(BoardRequestDto requestDto) {
        this.contents = requestDto.getContents();
        this.title = requestDto.getTitle();
    }

}
