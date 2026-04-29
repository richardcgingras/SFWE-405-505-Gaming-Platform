package com.example.gaming_platform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.service.WishListService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * REST controller for wish list operations.
 */
@RestController
@RequestMapping("/api/wishlist")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

/**
 * Gets a User's Wish list.
 *
 * @return user's wish list games
 */
    @GetMapping("")
    public List<VideoGame> getWishList(HttpServletRequest request) {
        Long userId = Long.parseLong(request.getAttribute("currentUserId").toString());
        return wishListService.getGames(userId);
    }

/**
 * Adds a game to the wish list.
 *
 * @param gameId the id of the game to add
 * @return the result of the operation
 */
    @PostMapping("/game/{gameId}")
    public ResponseEntity addGame(HttpServletRequest request, @PathVariable Long gameId) {
        Long userId = Long.parseLong(request.getAttribute("currentUserId").toString());
        String result = wishListService.addGame(userId, gameId);
        if (result.equals("Success")) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"Status\": \"success\"}");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"Status\": \"%s\"}".formatted(result));
        }
    }

/**
 * Removes a game from the wish list.
 *
 * @param gameId the id of the game to remove
 * @return the result of the operation
 */
    @DeleteMapping("/game/{gameId}")
    public ResponseEntity removeGame(HttpServletRequest request, @PathVariable Long gameId) {
        Long userId = Long.parseLong(request.getAttribute("currentUserId").toString());
        String result = wishListService.removeGame(userId, gameId);
        if (result.equals("Success")) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"Status\": \"success\"}");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"Status\": \"%s\"}".formatted(result));
        }
    }

/**
 * Gets the total price of games in the wish list.
 *
 * @return the total price
 */
    @GetMapping("/total")
    public ResponseEntity getTotal(HttpServletRequest request) {
        Long userId = Long.parseLong(request.getAttribute("currentUserId").toString());
        float total = wishListService.calculateTotalPrice(userId);
        return ResponseEntity.status(HttpStatus.OK).body("{\"total\": \"%.2f\"}".formatted(total));
    }
}
