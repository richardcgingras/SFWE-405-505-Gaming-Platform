package com.example.gaming_platform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.gaming_platform.entity.WishList;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.gaming_platform.entity.VideoGame;

import com.example.gaming_platform.repository.WishListRepository;

@RestController
@RequestMapping("/api/wishlists")
@CrossOrigin(origins = "*")
public class WishListController {

    private final WishListRepository wishListRepository;

    public WishListController(WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    // POST /api/wishlists
    @PostMapping(consumes = "application/json")
    public ResponseEntity<WishList> createWishList(@RequestBody WishList wishList) {
        WishList saved = wishListRepository.save(wishList);
        return ResponseEntity.ok(saved);
    }

    // PUT /api/wishlists/{id}/add-game
    // add game to wish list
    @PutMapping("/{id}/add-game")
    public ResponseEntity<WishList> addGameToWishList(@PathVariable Long id, @RequestBody VideoGame game) {
        WishList wishList = wishListRepository.findById(id).orElse(null);
        if (wishList == null) {
            return ResponseEntity.notFound().build();
        }
        wishList.getGames().add(game);
        WishList updated = wishListRepository.save(wishList);
        return ResponseEntity.ok(updated);
    }

    // GET /api/wishlists - get all wish lists
    @GetMapping
    public Iterable<WishList> getAllWishLists() {
        return wishListRepository.findAll();
    }

    // GET /api/wishlists/{id} - get wish list by id
    @GetMapping("/{id}")
    public ResponseEntity<WishList> getWishListById(@PathVariable Long id) {
        return wishListRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // GET /api/wishlists/total-price/{id} - get total price of games in wish list by wish list id
    @GetMapping("/total-price/{id}")
    public ResponseEntity<Float> getTotalPriceByWishListId(@PathVariable Long id) {
        WishList wishList = wishListRepository.findById(id).orElse(null);
        if (wishList == null) {
            return ResponseEntity.notFound().build();
        }
        float totalPrice = 0.0f;
        for (var game : wishList.getGames()) {
            totalPrice += game.getPrice();
        }
        return ResponseEntity.ok(totalPrice);
    }

    
}
