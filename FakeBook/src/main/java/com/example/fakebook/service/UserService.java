package com.example.fakebook.service;

import com.example.fakebook.domain.Post;
import com.example.fakebook.domain.User;
import com.example.fakebook.repository.PostRepository;
import com.example.fakebook.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public UserService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public Optional<User> getUser(Integer id){
        return userRepository.findById(id);
    }

    private void validateUser(User user){
        if(user.getFirstName().isEmpty() || user.getSecondName().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "FirstName or SecondName empty");
        }
    }

    public Optional<User> create(User user){
        validateUser(user);
        return Optional.of(userRepository.save(user));
    }

    public Optional<User> update(User user){
        validateUser(user);
        return Optional.of(userRepository.save(user));
    }

    public void delete(Integer id){
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "cant find user");
        }
        userRepository.delete(optionalUser.get());
    }

//    public List<User> getUsersForPost(Post post){
//        return userRepository.find
//    }
}
