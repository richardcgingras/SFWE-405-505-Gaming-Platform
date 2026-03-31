package com.example.gaming_platform.entity;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

//s
/**
 * Entity representing web store page data.
 */
@Entity
public class WebStorePage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Lob
    private byte[] imageUrl;

    @OneToOne
    private VideoGame game;

    @ManyToMany
    private List<VideoGame> developerRecommendations;

/**
 * Creates a new WebStorePage instance.
 */
    public WebStorePage(){}

    public WebStorePage(String name, String description, byte[] imageUrl,
                        List<VideoGame> developerRecommendations){
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.developerRecommendations = developerRecommendations;
    }

    //setters
/**
 * Sets the ID.
 *
 * @param id the ID
 */
    public void setId(Long id) { this.id = id; }
    public void setDescription(String description) { this.description = description; }
    public void setDeveloperRecommendations(List<VideoGame> developerRecommendations) {
        this.developerRecommendations = developerRecommendations;
    }
/**
 * Sets the game.
 *
 * @param game the game
 */
    public void setGame(VideoGame game) { this.game = game; }
    public void setImageUrl(byte[] imageUrl) { this.imageUrl = imageUrl; }
    public void setName(String name) { this.name = name; }

    //Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public byte[] getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
    public VideoGame getGame() { return game; }
    public List<VideoGame> getDeveloperRecommendations() { return developerRecommendations; }
}
