package com.sparta.blog.repository;

import com.sparta.blog.entity.LikedComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikedCommentRepository extends JpaRepository<LikedComment, Long> {
    Optional<LikedComment> findByCommentIdAndUserId(Long commentId, Long id);
}
