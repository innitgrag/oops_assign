package com.example.oopsmajorassignment.controller;
import com.example.oopsmajorassignment.entity.Comment;
import com.example.oopsmajorassignment.entity.Post;
import com.example.oopsmajorassignment.entity.User;

import com.example.oopsmajorassignment.repository.UserRepository;
import com.example.oopsmajorassignment.repository.PostRepository;
import com.example.oopsmajorassignment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not exist");
        } else if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username/Password Incorrect");
        } else {
            return ResponseEntity.ok("Login Successful");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden, Account already exists");
        } else {
            userRepository.save(user);
            return ResponseEntity.ok("Account Creation Successful");
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(@RequestParam Long userID) {
        Optional<User> optionalUser = userRepository.findById(userID);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return ResponseEntity.ok(user);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : allUsers) {
            userDTOs.add(new UserDTO(user.getId(), user.getName(),user.getEmail()));
        }
        return ResponseEntity.ok(userDTOs);
    }

    @Autowired
    private CommentRepository commentRepository; // Add this line to autowire CommentRepository

    @GetMapping("/")
    public ResponseEntity<List<PostDTO>> getUserFeed() {
        List<PostDTO> posts = new ArrayList<>();
        List<Post> allPosts = postRepository.findAllByOrderByDateDesc();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Post post : allPosts) {
            PostDTO postDTO = new PostDTO();
            postDTO.setPostID(post.getId());
            postDTO.setPostBody(post.getPostBody());

            // Format date to include only the date portion
            if (post.getDate() != null) {
                postDTO.setDate(post.getDate().format(dateFormatter));
            }

            List<CommentDTO> commentDTOs = new ArrayList<>();

            // Fetch comments for this post using CommentRepository.findByPostId method
            List<Comment> commentsForPost = commentRepository.findByPostId(post.getId());

            for (Comment comment : commentsForPost) {
                CommentDTO commentDTO = new CommentDTO();
                commentDTO.setCommentID(comment.getId());
                commentDTO.setCommentBody(comment.getCommentBody());

                // Set comment creator details
                User commentCreator = comment.getUser();
                commentDTO.setCommentCreator(new UserDTO(commentCreator.getId(), commentCreator.getName(), commentCreator.getEmail()));

                commentDTOs.add(commentDTO);
            }

            // Set comments for this post
            postDTO.setComments(commentDTOs);
            posts.add(postDTO);
        }

        return ResponseEntity.ok(posts);
    }


    static class UserDTO {
        private Long userID;
        private String name;
        private String email; // Include email here

        public UserDTO(Long userID, String name) {
            this.userID = userID;
            this.name = name;
        }

        public UserDTO(Long userID, String name, String email) {
            this.userID = userID;
            this.name = name;
            this.email = email;
        }

        public Long getUserID() {
            return userID;
        }

        public void setUserID(Long userID) {
            this.userID = userID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
    public class CommentDTO {
        private Long commentID;
        private String commentBody;
        private UserDTO commentCreator;

        public Long getCommentID() {
            return commentID;
        }

        public void setCommentID(Long commentID) {
            this.commentID = commentID;
        }

        public String getCommentBody() {
            return commentBody;
        }

        public void setCommentBody(String commentBody) {
            this.commentBody = commentBody;
        }

        public UserDTO getCommentCreator() {
            return commentCreator;
        }

        public void setCommentCreator(UserDTO commentCreator) {
            this.commentCreator = commentCreator;
        }
    }

    public class PostDTO {
        private Long postID;
        private String postBody;
        private String date;
        private List<CommentDTO> comments;

        public Long getPostID() {
            return postID;
        }

        public void setPostID(Long postID) {
            this.postID = postID;
        }

        public String getPostBody() {
            return postBody;
        }

        public void setPostBody(String postBody) {
            this.postBody = postBody;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<CommentDTO> getComments() {
            return comments;
        }

        public void setComments(List<CommentDTO> comments) {
            this.comments = comments;
        }
    }
}
