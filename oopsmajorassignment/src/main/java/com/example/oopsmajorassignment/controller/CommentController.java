package com.example.oopsmajorassignment.controller;

import com.example.oopsmajorassignment.entity.Comment;
import com.example.oopsmajorassignment.entity.Post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
public class CommentController {
    @Autowired
    private com.example.oopsmajorassignment.repository.CommentRepository commentRepository;

    @Autowired
    private com.example.oopsmajorassignment.repository.PostRepository postRepository;

    @Autowired
    private com.example.oopsmajorassignment.repository.UserRepository userRepository;

    // Create a new comment
    // CommentController.java

    @PostMapping("/comment")
    public ResponseEntity<String> createComment(@RequestBody Map<String, Object> commentData) {
        Long userID = commentData.get("userID") != null ? Long.parseLong(commentData.get("userID").toString()) : null;
        Long postID = commentData.get("postID") != null ? Long.parseLong(commentData.get("postID").toString()) : null;
        String commentBody = commentData.get("commentBody") != null ? commentData.get("commentBody").toString() : null;


        com.example.oopsmajorassignment.entity.User user = userRepository.findById(userID).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not exist");
        }

        Optional<Post> optionalPost = postRepository.findById(postID);
        if (!optionalPost.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post does not exist");
        }
        Post post = optionalPost.get();

        com.example.oopsmajorassignment.entity.Comment comment = new Comment();
        comment.setPost(post); // Set the post associated with this comment
        comment.setUser(user);

        comment.setCommentBody(commentBody);
        commentRepository.save(comment);

        return ResponseEntity.ok("Comment created successfully");
    }







    // Retrieve an existing comment
    @GetMapping("/comment/{commentID}")
    public ResponseEntity<?> getComment(@PathVariable Long commentID) {
        Optional<com.example.oopsmajorassignment.entity.Comment> commentOptional = commentRepository.findById(commentID);
        if (!commentOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Comment does not exist");
        }

        return ResponseEntity.ok(commentOptional.get());
    }

    // Edit an existing comment
    @PatchMapping("/comment")
    public ResponseEntity<String> editComment(@RequestBody Map<String, Object> editRequest) {
        Long commentID = ((Integer) editRequest.get("commentID")).longValue();
        String commentBody = (String) editRequest.get("commentBody");

        Optional<Comment> commentOptional = commentRepository.findById(commentID);
        if (!commentOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Comment does not exist");
        }

        Comment comment = commentOptional.get();
        comment.setCommentBody(commentBody);
        commentRepository.save(comment);

        return ResponseEntity.ok("Comment edited successfully");
    }

    // Delete an existing comment
    @DeleteMapping("/comment/{commentID}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentID) {
        Optional<Comment> commentOptional = commentRepository.findById(commentID);
        if (!commentOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Comment does not exist");
        }

        commentRepository.deleteById(commentID);

        return ResponseEntity.ok("Comment deleted");
    }

}
