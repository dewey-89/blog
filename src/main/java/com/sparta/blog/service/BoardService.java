package com.sparta.blog.service;

import com.sparta.blog.dto.BoardRequestDto;
import com.sparta.blog.dto.BoardResponseDto;
import com.sparta.blog.entity.Board;
import com.sparta.blog.entity.LikedBoard;
import com.sparta.blog.entity.User;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.repository.BoardRepository;
import com.sparta.blog.repository.LikedBoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final LikedBoardRepository likedBoardRepository;


    // 1.전체 게시글 조회 API
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

    // 2.게시글 작성 API
    @Transactional
    public ResponseEntity<BoardResponseDto> createBoard(BoardRequestDto requestDto, User user) {
        // RequestDto -> Entity
        Board board = new Board(requestDto, user);
        // DB 저장
        Board saveBoard = boardRepository.save(board);

        return ResponseEntity.status(200).body(new BoardResponseDto(saveBoard));
    }

    // 3.게시글 선택 조회
    @Transactional
    public ResponseEntity<BoardResponseDto> getBoardById(Long boardId) {

        Board board = boardRepository.findBoardById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다"));

        return ResponseEntity.status(200).body(new BoardResponseDto(board));
    }

    // 4.선택한 게시글 수정 API
    @Transactional
    public ResponseEntity<BoardResponseDto> updateBoard(Long boardId, BoardRequestDto boardRequestDto, User user) {

        Board board = findBoardById(boardId);

        if(!(user.getRole().equals(UserRoleEnum.ADMIN)||board.getUser().getId().equals(user.getId()))){
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        board.update(boardRequestDto);

        return ResponseEntity.status(200).body(new BoardResponseDto(board));
    }

    // 5.선택한 게시글 삭제 API
    @Transactional
    public ResponseEntity<String> deleteBoard(Long boardId, User user) {

        Board board = findBoardById(boardId);
        if(!(user.getRole().equals(UserRoleEnum.ADMIN)||board.getUser().getId().equals(user.getId()))){
            return ResponseEntity.status(400).body("msg :작성자만 수정할 수 있습니다. , statusCode : 400");
        }

        boardRepository.delete(board);

        return ResponseEntity.status(200).body("msg : 게시물 삭제 성공, statusCode : 200");
    }

    // 6.게시글 좋아요 API
    @Transactional
    public ResponseEntity<String> likeBoard(Long boardId, User user) {
        Board board = findBoardById(boardId);
        Optional<LikedBoard> likedBoardList = likedBoardRepository.findByBoardIdAndUserId(boardId, user.getId());

        if(likedBoardList.isPresent()){
            likedBoardRepository.delete(likedBoardList.get());
            return ResponseEntity.status(200).body("msg : 게시물 좋아요 취소 성공, statusCode : 200");
        }

        likedBoardRepository.save(new LikedBoard(board, user));
        return ResponseEntity.status(200).body("msg : 게시물 좋아요 성공, statusCode : 200");
    }

    public Board findBoardById(Long boardId) {
        return boardRepository.findBoardById(boardId).orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다"));
    }


}
