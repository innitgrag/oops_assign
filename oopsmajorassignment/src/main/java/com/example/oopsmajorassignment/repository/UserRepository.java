package com.example.oopsmajorassignment.repository;

import com.example.oopsmajorassignment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
