package com.example.pulsedesk.controllers;

import com.example.pulsedesk.dtos.CommentRequest;
import com.example.pulsedesk.dtos.CommentResponse;
import com.example.pulsedesk.models.Comment;
import com.example.pulsedesk.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing user comments.
 * Provides endpoints to submit and retrieve comments.
 */
@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Retrieves all comments.
     *
     * @return a list of all comments with their text and creation date.
     */
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getAllComments() {
        List<CommentResponse> list = commentService.getAllComments().stream()
                .map(c -> new CommentResponse(c.getText(), c.getCreatedAt()))
                .toList();
        return ResponseEntity.ok(list);
    }

    /**
     * Retrieves a specific comment by its ID.
     *
     * @param id the ID of the comment to retrieve.
     * @return the comment with its text and creation date.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable int id) {
        Comment comment = commentService.getCommentById(id);
        return ResponseEntity.ok(new CommentResponse(comment.getText(), comment.getCreatedAt()));
    }

    /**
     * Submits a new comment.
     *
     * @param body the comment request containing the text of the comment.
     * @return the saved comment with its text and creation date.
     */
    @PostMapping
    public ResponseEntity<CommentResponse> addComment(@Valid @RequestBody CommentRequest body) {
        Comment saved = commentService.addComment(body.text());
        CommentResponse response = new CommentResponse(saved.getText(), saved.getCreatedAt());
        return ResponseEntity.ok(response);
    }
}
