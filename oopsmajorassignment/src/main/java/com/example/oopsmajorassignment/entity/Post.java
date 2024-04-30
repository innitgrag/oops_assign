package com.example.oopsmajorassignment.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String postBody;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private com.example.oopsmajorassignment.entity.User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<com.example.oopsmajorassignment.entity.Comment> comments = new ArrayList<>();

    @Column(name = "date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public com.example.oopsmajorassignment.entity.User getUser() {
        return user;
    }

    public void setUser(com.example.oopsmajorassignment.entity.User user) {
        this.user = user;
    }

    public List<com.example.oopsmajorassignment.entity.Comment> getComments() {
        return comments;
    }

    public void setComments(List<com.example.oopsmajorassignment.entity.Comment> comments) {
        this.comments = comments;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
