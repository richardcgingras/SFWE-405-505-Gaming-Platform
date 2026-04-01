package com.example.gaming_platform.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing developer data.
 */
@Entity
public class Developer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;
    private String username;

    @OneToMany(mappedBy = "publisher")
    private List<VideoGame> publishedGames = new ArrayList<>();

/**
 * Creates a new Developer instance.
 */
    public Developer() {}

    public Developer(String email, String username){
        this.email = email;
        this.username = username;
    }

    //Setters and Getters

/**
 * Gets the ID.
 *
 * @return the ID
 */
    public Long getId() {
        return id;
    }
/**
 * Sets the ID.
 *
 * @param id the ID
 */
    public void setId(Long id) {
        this.id = id;
    }
/**
 * Gets the email.
 *
 * @return the email
 */
    public String getEmail() {
        return email;
    }
/**
 * Sets the email.
 *
 * @param email the email
 */
    public void setEmail(String email) {
        this.email = email;
    }
/**
 * Gets the username.
 *
 * @return the username
 */
    public String getUsername() {
        return username;
    }
/**
 * Sets the username.
 *
 * @param username the username
 */
    public void setUsername(String username) {
        this.username = username;
    }
/**
 * Gets the published games.
 *
 * @return the published games
 */
    public List<VideoGame> getPublishedGames() {
        return publishedGames;
    }
/**
 * Sets the published games.
 *
 * @param publishedGames the published games
 */
    public void setPublishedGames(List<VideoGame> publishedGames) {
        this.publishedGames = publishedGames;
    }
 }
