package com.sparta.blog.service;

import com.sparta.blog.dto.BoardRequestDto;
import com.sparta.blog.dto.BoardResponseDto;
import com.sparta.blog.entity.Board;
import com.sparta.blog.entity.User;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.repository.BoardRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // 전체 게시글 조회 API
    @Transactional
    public List<BoardResponseDto> getBoard() {
        // DB 조회
        List<Board> boards = boardRepository.findAllByOrderByModifiedAtDesc();
        List<BoardResponseDto> boardResponseDtos = new ArrayList<>();
        for (Board board : boards) {
            boardResponseDtos.add(new BoardResponseDto(board));
        }
        return boardResponseDtos;
    }

    // 게시글 작성 API
    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto requestDto, User user) {
        // RequestDto -> Entity
        Board board = new Board(requestDto, user);
        // DB 저장
        Board saveBoard = boardRepository.save(board);
        // Entity -> ResponseDto
        BoardResponseDto boardResponseDto = new BoardResponseDto(saveBoard);
        return boardResponseDto;
    }

    // 게시글 선택 조회
    @Transactional
    public BoardResponseDto getBoardById(Long id) {
        Board board = boardRepository.findBoardById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다"));
        return new BoardResponseDto(board);
    }

    // 선택한 게시글 수정 API
    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto boardRequestDto, User user) {

        Board board = findBoardById(id);

        if(!(user.getRole().equals(UserRoleEnum.ADMIN)||board.getUser().getId().equals(user.getId()))){
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        board.update(boardRequestDto);
        return new BoardResponseDto(board);
    }

    // 선택한 게시글 삭제 API
    @Transactional
    public ResponseEntity<String> deleteBoard(Long id, User user) {

        Board board = findBoardById(id);
        if(!(user.getRole().equals(UserRoleEnum.ADMIN)||board.getUser().getId().equals(user.getId()))){
//            throw new IllegalArgumentException("권한이 없습니다.");
            return ResponseEntity.status(400).body("msg :작성자만 수정할 수 있습니다. , statusCode : 400");
        }

        boardRepository.delete(board);
        return ResponseEntity.status(200).body("msg : 게시물 삭제 성공, statusCode : 200");
    }

    public Board findBoardById(Long id) {
        return boardRepository.findBoardById(id).orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다"));
    }

}
