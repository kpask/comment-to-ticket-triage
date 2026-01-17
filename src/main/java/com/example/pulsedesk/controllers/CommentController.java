package com.example.pulsedesk.controllers;

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
    public ResponseEntity<List<Comment>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable int id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @PostMapping
    public ResponseEntity<Comment> addComment(@RequestBody String text) {
        Comment saved = commentService.addComment(text);
        return ResponseEntity.ok(saved);
    }
}
