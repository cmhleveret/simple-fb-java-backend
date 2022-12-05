package com.example.fakebook.repository;

import com.example.fakebook.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Book;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByUserId(Integer user);

}
