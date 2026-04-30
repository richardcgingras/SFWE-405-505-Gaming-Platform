package com.example.gaming_platform.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

/**
 * Entity representing shopping cart data.
 */
@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<VideoGame> games;

    @OneToOne
    @com.fasterxml.jackson.annotation.JsonIgnore
    private UserProfile account;

    private float totalPrice;

    // constructors 
    public ShoppingCart(){}

    public ShoppingCart(List<VideoGame> games, float totalPrice, UserProfile account) {
        this.games = games;
        this.totalPrice = totalPrice;
        this.account = account;
    }

    // Setters
/**
 * Sets the games.
 *
 * @param setGames the set games
 */
    public void setGames(List<VideoGame> setGames){games = setGames;}
    public void setPrice(float newPrice){totalPrice = newPrice;}
    public void setAccount(UserProfile newAccount){account = newAccount;}

    // Getters
    public Long getId(){return id;}
    public List<VideoGame> getGames(){return games;}
    public float getTotal(){return totalPrice;}
    public UserProfile getAccount(){return account;}
}
