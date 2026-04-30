package com.example.gaming_platform.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;

@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String status;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(unique = true)
    private String userName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({
            "friends",
            "preferredCategories",
            "gameLibrary",
            "shoppingCart",
            "wishList",
            "password"
    })
    private List<UserProfile> friends;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "user_profile_category",
            joinColumns = @JoinColumn(name = "user_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> preferredCategories;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "user_profile_game_library",
            joinColumns = @JoinColumn(name = "user_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "video_game_id")
    )
    private List<VideoGame> gameLibrary;

    @JsonIgnore
    @OneToOne
    private ShoppingCart shoppingCart;

    @JsonIgnore
    @OneToOne
    private WishList wishList;

    private String bio;

    public UserProfile() {}

    public UserProfile(String email, String userName, String status, List<UserProfile> friends,
                       List<Category> preferredCategories, List<VideoGame> gameLibrary,
                       String bio, ShoppingCart shoppingCart, WishList wishList) {
        this.email = email;
        this.userName = userName;
        this.status = status;
        this.friends = friends;
        this.preferredCategories = preferredCategories;
        this.gameLibrary = gameLibrary;
        this.bio = bio;
        this.shoppingCart = shoppingCart;
        this.wishList = wishList;
    }

    public void setEmail(String email) { this.email = email; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setPassword(String passWord) { this.password = passWord; }
    public void setStatus(String status) { this.status = status; }
    public void setFriends(List<UserProfile> friends) { this.friends = friends; }
    public void setPreferredCategories(List<Category> categories) { this.preferredCategories = categories; }
    public void setGameLibrary(List<VideoGame> games) { this.gameLibrary = games; }
    public void setBio(String bio) { this.bio = bio; }
    public void setShoppingCart(ShoppingCart cart) { this.shoppingCart = cart; }
    public void setWishList(WishList wishList) { this.wishList = wishList; }

    public Long getId() { return this.id; }
    public String getEmail() { return this.email; }
    public String getUserName() { return this.userName; }
    public String getPassword() { return this.password; }
    public String getStatus() { return this.status; }
    public List<UserProfile> getFriends() { return this.friends; }
    public List<Category> getPreferredCategories() { return this.preferredCategories; }
    public List<VideoGame> getGameLibrary() { return this.gameLibrary; }
    public ShoppingCart getCart() { return this.shoppingCart; }
    public String getBio() { return this.bio; }
    public WishList getWishList() { return this.wishList; }
}