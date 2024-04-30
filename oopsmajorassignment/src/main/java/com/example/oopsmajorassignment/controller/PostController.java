package com.example.oopsmajorassignment.controller;

import com.example.oopsmajorassignment.entity.Post;
import com.example.oopsmajorassignment.entity.User;
import com.example.oopsmajorassignment.repository.PostRepository;
import com.example.oopsmajorassignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class PostController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @PostMapping("/post")
    public ResponseEntity<String> createPost(@RequestBody Map<String, Object> postData) {
        Long userID = Long.parseLong(postData.get("userID").toString());
        String postBody = postData.get("postBody").toString();
        User user = userRepository.findById(userID).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not exist");
        } else {
            Post post = new Post();
            post.setUser(user);
            post.setPostBody(postBody);
            post.setDate(LocalDateTime.now());
            postRepository.save(post);
            return ResponseEntity.ok("Post created successfully");
        }
    }

    @GetMapping("/post")
    public ResponseEntity<Post> getPost(@RequestParam Long postID) {
        Optional<Post> optionalPost = postRepository.findById(postID);
        if (optionalPost.isPresent()) {
            return ResponseEntity.ok(optionalPost.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PatchMapping("/post")
    public ResponseEntity<String> editPost(@RequestBody Map<String, Object> postData) {
        Long postID = Long.parseLong(postData.get("postID").toString());
        String postBody = postData.get("postBody").toString();
        Optional<Post> optionalPost = postRepository.findById(postID);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            post.setPostBody(postBody);
            postRepository.save(post);
            return ResponseEntity.ok("Post edited successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post does not exist");
        }
    }

    @DeleteMapping("/post")
    public ResponseEntity<String> deletePost(@RequestParam Long postID) {
        if (postRepository.existsById(postID)) {
            postRepository.deleteById(postID);
            return ResponseEntity.ok("Post deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post does not exist");
        }
    }
}