package com.example.gaming_platform.entity;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class UserProfile {
    @Id
    @GeneratedValue
    private Long id;

    // private variables
    private String email, userName, status;

    @OneToMany // JoinColumn or cascading???
    private List<UserProfile> friends;

    /*
     * ORIGINAL:
     *
     * @OneToMany
     * private List<Category> preferredCategories;
     *
     * This breaks the app because Category is an enum right now (not an @Entity),
     * and @OneToMany only works with entity relationships.
     *
     * For Phase 1, we just want the app to run + basic REST working,
     * so we store the enum values as strings in a simple collection table instead.
     */
    // @OneToMany
    // private List<Category> preferredCategories;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Category> preferredCategories;

    @OneToMany
    private List<VideoGame> gameLibrary;

    public UserProfile() {}

    // constructor
    public UserProfile(String email, String userName, String status, List<UserProfile> friends,
                       List<Category> preferredCategories, List<VideoGame> gameLibrary) {
        this.email = email;
        this.userName = userName;
        this.status = status;
        this.friends = friends;
        this.preferredCategories = preferredCategories;
        this.gameLibrary = gameLibrary;
    }

    // setters
    public void setEmail(String email) { this.email = email; }

    public void setUserName(String userName) { this.userName = userName; }

    public void setStatus(String status) { this.status = status; }

    public void setFriends(List<UserProfile> friends) { this.friends = friends; }

    public void setPreferredCategories(List<Category> categories) { this.preferredCategories = categories; }

    public void setGameLibrary(List<VideoGame> games) { this.gameLibrary = games; }

    // getters
    public String getEmail() { return this.email; }

    public String getUserName() { return this.userName; }

    public String getStatus() { return this.status; }

    public List<UserProfile> getFriends() { return this.friends; }

    public List<Category> getPreferredCategories() { return this.preferredCategories; }

    public List<VideoGame> getGameLibrary() { return this.gameLibrary; }

    // add friend
    public void addFriend(UserProfile friend) {
        friends.add(friend);
    }

    // add preferred category
    public void addCategory(Category c) {
        preferredCategories.add(c);
    }

    // add game to library
    public void addGame(VideoGame game) {
        gameLibrary.add(game);
    }
}
