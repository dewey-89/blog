package com.sparta.blog.repository;

import com.sparta.blog.entity.LikedBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikedBoardRepository extends JpaRepository<LikedBoard, Long>{
    Optional<LikedBoard> findByBoardIdAndUserId(Long boardId, Long userId);
}
