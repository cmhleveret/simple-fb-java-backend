package com.example.fakebook.resource;

import com.example.fakebook.domain.Post;
import com.example.fakebook.domain.User;
import com.example.fakebook.repository.UserRepository;
import com.example.fakebook.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostResource {

    private final PostService postService;

    private final UserRepository userRepository;

    public PostResource(PostService postService, UserRepository userRepository) {
        this.postService = postService;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public ResponseEntity<List<Post>> get(){
        List<Post> posts = postService.getPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{id}/")
    public ResponseEntity<Object> get(HttpServletRequest request, @PathVariable("id") Integer id){
        Optional<Post> optionalPost = postService.getPost(id);
        if (optionalPost.isEmpty()){
            return new ResponseEntity<>(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalPost.get(), HttpStatus.OK);
    }

    private Post mapPostNoId(Map<String, Object> postJson){
        Integer userId = (Integer) postJson.getOrDefault("userId", -1);
        if (userId == -1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing UserID");
        }
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "cannot find user");
        }


        Post post = new Post();
        post.setTitle((String) postJson.getOrDefault("title", ""));
        post.setBody( (String) postJson.getOrDefault("body", ""));
        post.setUser(optionalUser.get());
           return post;

    }

    private Post mapPostWithId(Map<String, Object> postJson, Integer id){

        Post post = mapPostNoId(postJson);
        post.setId(id);
        return post;
    }

    @PostMapping("/create")
    public ResponseEntity<Post> create(@RequestBody Map<String, Object> postJson){
        Optional<Post> createdPost = postService.create(mapPostNoId(postJson));
        if(createdPost.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "could not create post");
        }
        return new ResponseEntity<>(createdPost.get(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Post> update(@RequestBody Map<String, Object> postJson, @PathVariable("id") Integer id){
        Optional<Post> updatedPost = postService.update(mapPostWithId(postJson, id));
        if(updatedPost.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "could not update post");
        }
        return new ResponseEntity<>(updatedPost.get(), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Map<String, Boolean>> delete(@PathVariable("id") Integer id){
        postService.delete(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("Success", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
