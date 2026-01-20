package com.example.pulsedesk.service;

import com.example.pulsedesk.exceptions.CommentNotFoundException;
import com.example.pulsedesk.models.Comment;
import com.example.pulsedesk.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for managing user comments.
 * Provides methods for adding, retrieving, editing, and deleting comments.
 */
@Service
public class CommentService {

    private final CommentRepository repo;
    private final AiTicketCreator aiTicketCreator;

    public CommentService(CommentRepository repo, AiTicketCreator aiTicketCreator){
        this.repo = repo;
        this.aiTicketCreator = aiTicketCreator;
    }

    /**
     * Adds a new comment and triggers AI ticket creation if applicable.
     *
     * @param text the text of the comment to add.
     * @return the saved comment.
     */
    public Comment addComment(String text) {
        Comment comment = repo.save(new Comment(text.trim()));
        aiTicketCreator.createTicket(comment);
        return comment;
    }

    /**
     * Retrieves all comments from the repository.
     *
     * @return a list of all comments.
     */
    public List<Comment> getAllComments(){
        return repo.findAll();
    }

    /**
     * Retrieves a specific comment by its ID.
     *
     * @param commentId the ID of the comment to retrieve.
     * @return the comment with the specified ID.
     * @throws CommentNotFoundException if the comment is not found.
     */
    public Comment getCommentById(int commentId){
        return repo.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + commentId + "not found."));
    }
    /**
     * Deletes a comment by its ID.
     *
     * @param commentId the ID of the comment to delete.
     * @throws CommentNotFoundException if the comment is not found.
     */
    public void deleteComment(int commentId) {
        getCommentById(commentId);
        repo.deleteById(commentId);
    }

    public Comment editComment(int commentId, String newText) {
        Comment comment = getCommentById(commentId);
        comment.setText(newText.trim());
        return repo.save(comment);
    }
}
