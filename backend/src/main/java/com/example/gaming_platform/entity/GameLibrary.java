package com.example.gaming_platform.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

import jakarta.persistence.GenerationType;

/**
 * Entity representing game library data.
 */
@Entity
public class GameLibrary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private UserProfile owner;

    @ManyToMany
    private List<VideoGame> games;

    private float totalSize;

/**
 * Creates a new GameLibrary instance.
 */
    public GameLibrary() {
        this.games = new ArrayList<>();
        this.totalSize = 0;
    }

/**
 * Creates a new GameLibrary instance.
 *
 * @param owner the owner
 * @param games the games
 */
    public GameLibrary(UserProfile owner, List<VideoGame> games) {
        this.owner = owner;
        if (games != null) this.games = games;
        else this.games = new ArrayList<>();
        this.totalSize = calculateTotalSize();
    }


/**
 * Calculates the ulate total size.
 *
 * @return the calculated ulate total size
 */
    public float calculateTotalSize() {
        this.totalSize = 0;
        for (VideoGame game : games) {
            this.totalSize += game.getSize();
        }
        return this.totalSize;
    }

/**
 * Adds game.
 *
 * @param game the game
 */
    public void addGame(VideoGame game) {
        this.games.add(game);
        this.totalSize += game.getSize();
    }

/**
 * Removes game.
 *
 * @param game the game
 */
    public void removeGame(VideoGame game) {
        this.games.remove(game);
        this.totalSize -= game.getSize();
    }

/**
 * Checks whether the game exists.
 *
 * @param game the game
 * @return {@code true} when the game exists
 */
    public boolean hasGame(VideoGame game) {
        return this.games.contains(game);
    }

    // Getters
/**
 * Gets the ID.
 *
 * @return the ID
 */
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
/**
 * Sets the total size.
 *
 * @param totalSize the total size
 */
    public void setTotalSize(float totalSize) { this.totalSize = totalSize; }
}
