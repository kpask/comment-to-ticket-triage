package com.example.pulsedesk.repository;

import com.example.pulsedesk.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Comment entities.
 * Provides CRUD operations and query methods for Comment data.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
