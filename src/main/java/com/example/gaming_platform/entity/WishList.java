package com.example.gaming_platform.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;

@Entity
public class WishList {

    @Id
    @GeneratedValue
    private Long id;

    // not sure about the relationship at this point
    // will it not change when we decide how to link things??
    @ManyToMany
    List<VideoGame> games;

    float totalPrice;

    @OneToOne
    UserProfile user;
    
    public WishList(){}

    public WishList(List<VideoGame> games, float totalPrice, UserProfile user) {
        this.games = games;
        this.totalPrice = totalPrice;
        this.user = user;
    }

    // setters
    public void setId(Long id) { this.id = id; }
    public void setGames(List<VideoGame> games) { this.games = games; }
    public void setTotalPrice(float totalPrice) { this.totalPrice = totalPrice; }
    public void setUser(UserProfile user) { this.user = user; }

    // getters
    public Long getId() { return id; }
    public List<VideoGame> getGames() { return games; }
    public float getTotalPrice() { return totalPrice; }
    public UserProfile getUser() { return user; }
    
}
