package com.example.gaming_platform.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

/**
 * Entity representing wish list data.
 */
@Entity
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // not sure about the relationship at this point
    // will it not change when we decide how to link things??
    @ManyToMany
    List<VideoGame> games;

    float totalPrice;

    public WishList(){}

    public WishList(List<VideoGame> games, float totalPrice) {
        this.games = games;
        this.totalPrice = totalPrice;
    }

    // setters
/**
 * Sets the ID.
 *
 * @param id the ID
 */
    public void setId(Long id) { this.id = id; }
    public void setGames(List<VideoGame> games) { this.games = games; }
    public void setTotalPrice(float totalPrice) { this.totalPrice = totalPrice; }

    // getters
    public Long getId() { return id; }
    public List<VideoGame> getGames() { return games; }
    public float getTotalPrice() { return totalPrice; }
    
}
