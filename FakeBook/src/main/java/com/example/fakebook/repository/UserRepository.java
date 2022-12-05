package com.example.fakebook.repository;


import com.example.fakebook.domain.Post;
import com.example.fakebook.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
