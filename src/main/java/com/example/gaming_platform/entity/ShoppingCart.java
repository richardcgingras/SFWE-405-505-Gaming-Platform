package com.example.gaming_platform.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    private List<VideoGame> games;

    @OneToOne
    private UserProfile account;

    private float totalPrice;

    // Setters
    public void setGames(List<VideoGame> setGames){games = setGames;}
    public void setPrice(float newPrice){totalPrice = newPrice;}

    // Getters
    public List<VideoGame> getGames(){return games;}
    public float getTotal(){return totalPrice;}
    public UserProfile getAccount(){return account;}
}
