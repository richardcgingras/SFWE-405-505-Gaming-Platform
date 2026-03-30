package com.example.gaming_platform.entity;

public class ShoppingCartRequest {
    private ShoppingCart shoppingCart;
    private VideoGame videoGame;

    // Getters and Setters
    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public VideoGame getVideoGame() {
        return videoGame;
    }

    public void setVideoGame(VideoGame videoGame) {
        this.videoGame = videoGame;
    }
}