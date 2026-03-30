package com.example.gaming_platform.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

/**
 * Entity representing category data.
 */
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

/**
 * Creates a new Category instance.
 */
    public Category() {
    }

/**
 * Creates a new Category instance.
 *
 * @param type the type
 * @param description the description
 * @param userProfiles the user profiles
 * @param videoGames the video games
 */
    public Category(String type, String description, List<UserProfile> userProfiles, List<VideoGame> videoGames) {
        this.type = type;
        this.description = description;
        this.userProfiles = userProfiles;
        this.videoGames = videoGames;
    }

/**
 * Gets the ID.
 *
 * @return the ID
 */
    public Long getId() {
        return id;
    }

/**
 * Gets the type.
 *
 * @return the type
 */
    public String getType() {
        return type;
    }

/**
 * Gets the description.
 *
 * @return the description
 */
    public String getDescription() {
        return description;
    }

/**
 * Gets the user profiles.
 *
 * @return the user profiles
 */
    public List<UserProfile> getUserProfiles() {
        return userProfiles;
    }

/**
 * Gets the video games.
 *
 * @return the video games
 */
    public List<VideoGame> getVideoGames() {
        return videoGames;
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
 * Sets the type.
 *
 * @param type the type
 */
    public void setType(String type) {
        this.type = type;
    }

/**
 * Sets the description.
 *
 * @param description the description
 */
    public void setDescription(String description) {
        this.description = description;
    }

/**
 * Sets the user profiles.
 *
 * @param userProfiles the user profiles
 */
    public void setUserProfiles(List<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
    }

/**
 * Sets the video games.
 *
 * @param videoGames the video games
 */
    public void setVideoGames(List<VideoGame> videoGames) {
        this.videoGames = videoGames;
    }
}
