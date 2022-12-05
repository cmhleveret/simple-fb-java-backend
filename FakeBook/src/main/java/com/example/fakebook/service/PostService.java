package com.example.fakebook.service;

import com.example.fakebook.domain.Post;
import com.example.fakebook.domain.User;
import com.example.fakebook.repository.PostRepository;
import com.example.fakebook.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.plaf.PanelUI;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostService {

    private PostRepository postRepository;

    private final UserRepository userRepository;


    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Optional<Post> getPost(Integer id){
        return postRepository.findById(id);
    }

    public List<Post> getPosts(){
        return postRepository.findAll();
    }

    private void validatePost(Post post){
        if (post.getTitle().isEmpty() || post.getBody().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post title or body is empty");
        }
    }

    public Optional<Post> create(Post post){
        validatePost(post);
        return Optional.of(postRepository.save(post));
    }

    public Optional<Post> update(Post post){
        validatePost(post);
        return Optional.of(postRepository.save(post));
    }

    public void delete(Integer id){
        Optional<Post> optionalPost = postRepository.findById(id);
        if(optionalPost.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "cant find post");
        }
        postRepository.delete(optionalPost.get());
    }

}

