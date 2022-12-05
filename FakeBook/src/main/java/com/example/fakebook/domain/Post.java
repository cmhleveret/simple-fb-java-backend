package com.example.fakebook.domain;

import com.example.fakebook.domain.DTO.PostDTO;

import javax.persistence.*;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private User user;

    private String title;

    private String body;


    public Post(String title, String body){
        this.title = title;
        this.body = body;
    }

    public Post() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PostDTO getDto() {
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle(this.title);
        postDTO.setId(this.id);
        return postDTO;
    }

}
