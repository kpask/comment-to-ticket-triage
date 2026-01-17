package com.example.pulsedesk.models;

import com.example.pulsedesk.enums.Category;
import com.example.pulsedesk.enums.Priority;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
public class Ticket{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String summary;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @OneToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Ticket(){}

    public Ticket(String title, String summary, Category category, Priority priority, Comment comment){
        this.category = category;
        this.priority = priority;
        this.title = title;
        this.summary = summary;
        this.comment = comment;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
