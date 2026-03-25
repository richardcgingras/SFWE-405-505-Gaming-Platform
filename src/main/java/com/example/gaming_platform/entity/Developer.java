package com.example.gaming_platform.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Developer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;
    private String username;

    @OneToMany(mappedBy = "publisher")
    private List<VideoGame> publishedGames = new ArrayList<>();

    public Developer() {}

    public Developer(String email, String username){
        this.email = email;
        this.username = username;
    }

    //Setters and Getters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public List<VideoGame> getPublishedGames() {
        return publishedGames;
    }
    public void setPublishedGames(List<VideoGame> publishedGames) {
        this.publishedGames = publishedGames;
    }
 }
