package com.example.pulsedesk.service;

import com.example.pulsedesk.models.Comment;
import com.example.pulsedesk.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository repo;

    public CommentService(CommentRepository repo) {
        this.repo = repo;
    }

    public Comment addComment(String text) {
        if (text == null || text.trim().length() < 5) {
            throw new IllegalArgumentException("Comment text is too short");
        }
        return repo.save(new Comment(text.trim()));
    }

    public List<Comment> getAllComments(){
        return repo.findAll();
    }

    public Comment getCommentById(int commentId){
        return repo.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
    }
    public void deleteComment(int commentId) {
        getCommentById(commentId);
        repo.deleteById(commentId);
    }

    public Comment editComment(int commentId, String newText) {
        Comment comment = getCommentById(commentId);

        if (newText == null || newText.trim().length() < 5) {
            throw new IllegalArgumentException("Updated comment text is too short");
        }

        comment.setText(newText.trim());
        return repo.save(comment);
    }
}
