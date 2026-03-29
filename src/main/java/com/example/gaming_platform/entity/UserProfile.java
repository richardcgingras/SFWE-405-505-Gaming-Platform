package com.example.gaming_platform.entity;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

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

    @ElementCollection(targetClass = Category.class)
    @Enumerated(EnumType.STRING)
    private List<Category> preferredCategories;

    @ManyToMany
    private List<VideoGame> gameLibrary;

    @OneToOne
    private ShoppingCart shoppingCart;

    @OneToOne
    private WishList wishList;

    private String bio;

    public UserProfile() {}

    // constructor
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

    // setters
    public void setEmail(String email) { this.email = email; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setStatus(String status) { this.status = status; }
    public void setFriends(List<UserProfile> friends) { this.friends = friends; }
    public void setPreferredCategories(List<Category> categories) { this.preferredCategories = categories; }
    public void setGameLibrary(List<VideoGame> games) { this.gameLibrary = games; }
    public void setBio(String bio) { this.bio = bio; }
    public void setShoppingCart(ShoppingCart cart) { this.shoppingCart = cart; }
    public void setWishList(WishList wishList) { this.wishList = wishList; }

    // getters
    public String getEmail() { return this.email; }
    public String getUserName() { return this.userName; }
    public String getStatus() { return this.status; }
    public List<UserProfile> getFriends() { return this.friends; }
    public List<Category> getPreferredCategories() { return this.preferredCategories; }
    public List<VideoGame> getGameLibrary() { return this.gameLibrary; }
    public ShoppingCart getCart() { return this.shoppingCart; }
    public String getBio() { return this.bio; }
    public WishList getWishList() { return this.wishList; }

}
