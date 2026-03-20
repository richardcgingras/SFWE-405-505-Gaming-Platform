package com.example.gaming_platform.entity;

import java.util.List;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

public class ShoppingCart {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    private List<VideoGame> games;

    private double totalPrice;

    // Setters
    public void setGames(List<VideoGame> setGames){games = setGames;}
    public void setPrice(double newPrice){totalPrice = newPrice;}

    // Getters
    public List<VideoGame> getGames(){return games;}
    public double getTotal(){return totalPrice;}
}
