package com.example.gaming_platform.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.entity.WishList;
import com.example.gaming_platform.entity.UserProfile;
import com.example.gaming_platform.repository.WishListRepository;
import com.example.gaming_platform.repository.UserProfileRepository;
import com.example.gaming_platform.repository.VideoGameRepository;


/**
 * Service for wish list business operations.
 */
@Service
public class WishListService {

    private final WishListRepository wishListRepo;
    private final UserProfileRepository userProfileRepo;
    private final VideoGameRepository videoGameRepo;
    
/**
 * Creates a new WishListService instance.
 *
 * @param wishListRepo the wish list repo
 * @param userProfileRepo the user profile repo
 * @param videoGameRepo the video game repo
 */
    public WishListService(WishListRepository wishListRepo, UserProfileRepository userProfileRepo, VideoGameRepository videoGameRepo) {
        this.wishListRepo = wishListRepo;
        this.userProfileRepo = userProfileRepo;
        this.videoGameRepo = videoGameRepo;
    }

/**
 * Gets a user's wish list
 *
 * @param userId id of a user
 * @return wish list
 */
    public WishList getWishList(Long userId){
        UserProfile user = userProfileRepo.findById(userId).orElse(null);
        if (user == null) return null;
        
        WishList wishList = wishListRepo.findByAccount(user);
        if (wishList == null) {
            wishList = new WishList();
            wishList.setAccount(user);
            wishList.setGames(new java.util.ArrayList<>());
            wishList.setTotalPrice(0.0f);
            wishList = wishListRepo.save(wishList);
            
            user.setWishList(wishList);
            userProfileRepo.save(user);
        }
        return wishList;
    }

/**
 * Gets all games in a user's wish list
 *
 * @param userId id of a user
 * @return List of Games
 */
    public List<VideoGame> getGames(Long userId) {
        return Optional.ofNullable(getWishList(userId))
            .map(WishList::getGames)
            .orElse(List.of());
    }

/**
 * Adds game to wish list.
 *
 * @param userId the user id
 * @param gameId the id of the game to add
 * @return the operation result
 */
    public String addGame(Long userId, Long gameId) {
        WishList wishList = getWishList(userId);
        if (wishList == null) {
            return "Wish list does not exist";
        }

        Optional<VideoGame> gameLookup = videoGameRepo.findById(gameId);
        if (gameLookup.isEmpty()) {
            return "Game does not exist";
        }
        VideoGame gameToAdd = gameLookup.get();

        boolean gameExists = wishList.getGames().stream()
            .anyMatch(g -> g.getId() == gameId);
        if (gameExists) {
            return "Game is already in the Wish List";
        }

        wishList.getGames().add(gameToAdd);
        wishListRepo.save(wishList);
        return "Success";
    }

/**
 * Removes game from wish list.
 *
 * @param userId the user id
 * @param gameId the id of the game to remove
 * @return the operation result
 */
    public String removeGame(Long userId, Long gameId) {
        WishList wishList = getWishList(userId);
        if (wishList == null) {
            return "Wish list does not exist";
        }

        Optional<VideoGame> gameLookup = videoGameRepo.findById(gameId);
        if (gameLookup.isEmpty()) {
            return "Game does not exist";
        }
        VideoGame gameToRemove = gameLookup.get();

        wishList.getGames().remove(gameToRemove);
        wishListRepo.save(wishList);
        return "Success";
    }

/**
 * Calculates the total price of all games in the wish list
 *
 * @param userId the user id
 * @return the total price
 */
    public float calculateTotalPrice(Long userId) {
        return Optional.ofNullable(getWishList(userId))
            .map(wishList -> wishList.getGames().stream()
                .map(VideoGame::getPrice)
                .reduce(0.0f, Float::sum))
            .orElse(0.0f);
    }
}
