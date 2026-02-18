package com.example.gaming_platform.entity;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;


@Entity
public class WebStorePage {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String description;

    @Lob
    private byte[] imageUrl;

    @OneToOne
    private VideoGame game;

    @ManyToMany
    private List<VideoGame> developerRecommendations;

    public WebStorePage(){}

    public WebStorePage(String name, String description, byte[] imageUrl,
                        List<VideoGame> developerRecommendations){
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.developerRecommendations = developerRecommendations;
    }

    //setters
    public void setId(Long id) { this.id = id; }
    public void setDescription(String description) { this.description = description; }
    public void setDeveloperRecommendations(List<VideoGame> developerRecommendations) {
        this.developerRecommendations = developerRecommendations;
    }
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
