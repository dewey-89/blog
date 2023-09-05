package com.sparta.blog.controller;

import com.sparta.blog.dto.BoardDeleteRequestDto;
import com.sparta.blog.dto.BoardRequestDto;
import com.sparta.blog.dto.BoardResponseDto;
import com.sparta.blog.security.UserDetailsImpl;
import com.sparta.blog.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BoardController {
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 1.전체 게시글 조회
    @GetMapping("/board")
    public List<BoardResponseDto> getBoard() {
        return boardService.getBoard();
    }

    // 2.게시글 작성
    // Json형식으로 요청
    @PostMapping("/board")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.createBoard(requestDto, userDetails.getUser());
    }


    // 3. 선택한 게시글 조회 API
    @GetMapping("/board/{id}")
    public BoardResponseDto getBoardById(@PathVariable Long id) {
        return boardService.getBoardById(id);
    }


    // 4. 선택한 게시글 수정(password 일치)
    @PutMapping("/board/{id}")
    public ResponseEntity<String> updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto boardRequestDto,@AuthenticationPrincipal UserDetailsImpl userDetails){
        return boardService.updateBoardByPassword(id,boardRequestDto,userDetails.getUser());
    }

    // 5. 선택한 게시글 삭제(password 일치)
    @DeleteMapping("/board/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return boardService.deleteBoard(id, userDetails.getUser());
    }

}
