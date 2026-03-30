package com.example.gaming_platform.service;

import org.springframework.stereotype.Service;
import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.entity.WishList;
import com.example.gaming_platform.repository.WishListRepository;


/**
 * Service for wish list business operations.
 */
@Service
public class WishListService {

    private final WishListRepository wishListRepo;
    
/**
 * Creates a new WishListService instance.
 *
 * @param wishListRepo the wish list repo
 */
    public WishListService(WishListRepository wishListRepo) {
        this.wishListRepo = wishListRepo;
    }

    // should we use Id instead of the object?
    // calculate total price of all games in the wish list
/**
 * Calculates the ulate total price.
 *
 * @param wishList the wish list
 * @return the calculated ulate total price
 */
    public float calculateTotalPrice(WishList wishList) {
        float total = 0.0f;
        for (VideoGame game : wishList.getGames()) {
            total += game.getPrice();
        }
        return total;
    }

    // calculate total disk size of games in the wish list
/**
 * Calculates the ulate total disk size.
 *
 * @param wishList the wish list
 * @return the calculated ulate total disk size
 */
    public float calculateTotalDiskSize(WishList wishList) {
        float total = 0.0f;
        for (VideoGame game : wishList.getGames()) {
            total += game.getSize();
        }
        return total;
    }

/**
 * Adds game to wish list.
 *
 * @param wishList the wish list
 * @param game the game
 */
    public void addGameToWishList(WishList wishList, VideoGame game) {
        wishList.getGames().add(game);
        wishListRepo.save(wishList);
    }

/**
 * Removes game from wish list.
 *
 * @param wishList the wish list
 * @param game the game
 */
    public void removeGameFromWishList(WishList wishList, VideoGame game) {
        wishList.getGames().remove(game);
        wishListRepo.save(wishList);
    }
}
