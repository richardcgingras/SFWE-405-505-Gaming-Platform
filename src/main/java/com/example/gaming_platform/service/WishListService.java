package com.example.gaming_platform.service;

import org.springframework.stereotype.Service;
import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.entity.WishList;
import com.example.gaming_platform.repository.WishListRepository;


@Service
public class WishListService {

    private final WishListRepository wishListRepo;
    
    public WishListService(WishListRepository wishListRepo) {
        this.wishListRepo = wishListRepo;
    }

    // should we use Id instead of the object?
    // calculate total price of all games in the wish list
    public float calculateTotalPrice(WishList wishList) {
        float total = 0.0f;
        for (VideoGame game : wishList.getGames()) {
            total += game.getPrice();
        }
        return total;
    }

    // calculate total disk size of games in the wish list
    public float calculateTotalDiskSize(WishList wishList) {
        float total = 0.0f;
        for (VideoGame game : wishList.getGames()) {
            total += game.getSize();
        }
        return total;
    }

    public void addGameToWishList(WishList wishList, VideoGame game) {
        wishList.getGames().add(game);
        wishListRepo.save(wishList);
    }

    public void removeGameFromWishList(WishList wishList, VideoGame game) {
        wishList.getGames().remove(game);
        wishListRepo.save(wishList);
    }
}
