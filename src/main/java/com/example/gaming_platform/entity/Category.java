package com.example.gaming_platform.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Category {
    @Id
    @GeneratedValue
    private Long id;

    private String type;
    private String description;

    @ManyToMany(mappedBy = "preferredCategories")
    @JsonIgnore
    private List<UserProfile> userProfiles;

    @ManyToMany(mappedBy = "category")
    @JsonIgnore
    private List<VideoGame> videoGames;

    public Category() {
    }

    public Category(String type, String description, List<UserProfile> userProfiles, List<VideoGame> videoGames) {
        this.type = type;
        this.description = description;
        this.userProfiles = userProfiles;
        this.videoGames = videoGames;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public List<UserProfile> getUserProfiles() {
        return userProfiles;
    }

    public List<VideoGame> getVideoGames() {
        return videoGames;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUserProfiles(List<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
    }

    public void setVideoGames(List<VideoGame> videoGames) {
        this.videoGames = videoGames;
    }
}
