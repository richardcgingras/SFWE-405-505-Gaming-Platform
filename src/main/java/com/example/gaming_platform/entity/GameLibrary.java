package com.example.gaming_platform.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
public class GameLibrary {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private UserProfile owner;

    @ManyToMany
    private List<VideoGame> games;

    private float totalSize;

    public GameLibrary() {
        this.games = new ArrayList<>();
        this.totalSize = 0;
    }

    public GameLibrary(UserProfile owner, List<VideoGame> games) {
        this.owner = owner;
        if (games != null) this.games = games;
        else this.games = new ArrayList<>();
        this.totalSize = calculateTotalSize();
    }


    public float calculateTotalSize() {
        this.totalSize = 0;
        for (VideoGame game : games) {
            this.totalSize += game.getSize();
        }
        return this.totalSize;
    }

    public void addGame(VideoGame game) {
        this.games.add(game);
        this.totalSize += game.getSize();
    }

    public void removeGame(VideoGame game) {
        this.games.remove(game);
        this.totalSize -= game.getSize();
    }

    public boolean hasGame(VideoGame game) {
        return this.games.contains(game);
    }

    // Getters
    public Long getId() { return id; }
    public UserProfile getOwner() { return owner; }
    public List<VideoGame> getGames() { return games; }
    public float getTotalSize() { return totalSize; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setOwner(UserProfile owner) { this.owner = owner; }
    public void setGames(List<VideoGame> games) {
        this.games = games;
        calculateTotalSize(); // keep totalSize in sync
    }
    public void setTotalSize(float totalSize) { this.totalSize = totalSize; }
}