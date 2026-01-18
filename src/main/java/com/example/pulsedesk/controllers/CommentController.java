package com.example.pulsedesk.controllers;

import com.example.pulsedesk.dtos.CommentRequest;
import com.example.pulsedesk.dtos.CommentResponse;
import com.example.pulsedesk.models.Comment;
import com.example.pulsedesk.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getAllComments() {
        List<CommentResponse> list = commentService.getAllComments().stream()
                .map(c -> new CommentResponse(c.getText(), c.getCreatedAt()))
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable int id) {
        Comment comment = commentService.getCommentById(id);
        return ResponseEntity.ok(new CommentResponse(comment.getText(), comment.getCreatedAt()));
    }

    @PostMapping
    public ResponseEntity<CommentResponse> addComment(@RequestBody CommentRequest body) {
        Comment saved = commentService.addComment(body.text());
        CommentResponse response = new CommentResponse(saved.getText(), saved.getCreatedAt());
        return ResponseEntity.ok(response);
    }
}
