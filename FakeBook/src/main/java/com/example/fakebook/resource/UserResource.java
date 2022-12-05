package com.example.fakebook.resource;

import com.example.fakebook.domain.DTO.PostDTO;
import com.example.fakebook.domain.Post;
import com.example.fakebook.domain.User;
import com.example.fakebook.repository.PostRepository;
import com.example.fakebook.service.PostService;
import com.example.fakebook.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserResource {
    private final UserService userService;

    private final PostService postService;

    private final PostRepository postRepository;

    public UserResource(UserService userService, PostService postService, PostRepository postRepository) {
        this.userService = userService;
        this.postService = postService;
        this.postRepository = postRepository;
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> get(){
        List<User> users = userService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}/")
    public ResponseEntity<Object> get(HttpServletRequest request, @PathVariable("id") Integer id){
        Optional<User> optionalUser = userService.getUser(id);
        if(optionalUser.isEmpty()){
            return new ResponseEntity<>(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalUser.get(), HttpStatus.OK);
    }

    private User mapUserNoId(Map<String, Object> userJson){
        List<Post> posts = postService.getPosts();
        List<PostDTO> postDTOS = posts.stream()
                .map(post-> {
                    PostDTO postDTO = post.getDto();
                    List<User> userPosts = userService.getBooksForCategory(category);
                    List<BookDTO> bookDTOS = categoryBooks.stream()
                            .map(Book::getDto)
                            .collect(Collectors.toList());
                    categoryDTO.setBooks(bookDTOS);
                    return categoryDTO;
                })

                .collect(Collectors.toList());
        User user = new User();
                user.setFirstName((String) userJson.getOrDefault("firstName", ""));
                user.setSecondName((String) userJson.getOrDefault("secondName", ""));
//                user.setPosts(posts);
                return user;
    }

    private User mapUserWithId(Map<String, Object> userJson, Integer id){
        User user = mapUserNoId(userJson);
        user.setId(id);
        return user;
    }

    @PostMapping("/create")
    public ResponseEntity<User> create(@RequestBody Map<String, Object> userJson){
        Optional<User> createdUser = userService.create(mapUserNoId(userJson));
        if (createdUser.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "could not create user");
        }
        return new ResponseEntity<>(createdUser.get(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<User> update(@RequestBody Map<String, Object> userJson, @PathVariable("id") Integer id){
        Optional<User> updatedUser = userService.update(mapUserWithId(userJson, id));
        if(updatedUser.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "could not update user");
        }
        return new ResponseEntity<>(updatedUser.get(), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Map<String, Boolean>> delete(@PathVariable("id") Integer id){
        userService.delete(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
