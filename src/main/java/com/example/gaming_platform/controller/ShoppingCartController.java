package com.example.gaming_platform.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gaming_platform.repository.ShoppingCartRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.gaming_platform.entity.ShoppingCart;
import com.example.gaming_platform.entity.UserProfile;
import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.service.ShoppingCartService;

@RestController
@RequestMapping("/api/shopping-cart")
@CrossOrigin(origins = "*")
public class ShoppingCartController {
    // private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService){
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping("game")
    public ResponseEntity postAddGame(@RequestBody ShoppingCart shoppingCart, VideoGame gameTooAdd) {
        boolean success;
        success = shoppingCartService.addGame(shoppingCart, gameTooAdd);
        if (success){
            return ResponseEntity.status(HttpStatus.OK).body("{\"Status\": \"success\"");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"Status\": \"failed\"");
        }
        
    }

    @DeleteMapping("game")
    public ResponseEntity deleteRemoveGame(@RequestBody ShoppingCart shoppingCart, VideoGame gameTooAdd) {
        shoppingCartService.addGame(shoppingCart, gameTooAdd);
        return ResponseEntity.status(HttpStatus.OK).body("{\"Status\": \"success\"");
    }

    @GetMapping("total")
    public ResponseEntity getTotal(@RequestParam ShoppingCart shoppingCart) {
        float total;
        String response;
        total = shoppingCartService.calcTotalPrice(shoppingCart);
        response = "{\"total\": \"%.2f\"}".formatted(total);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @PostMapping("checkout")
    public ResponseEntity postCheckouString(@RequestBody ShoppingCart shoppingCart, UserProfile destinationAccount) {
        boolean success;
        success = shoppingCartService.checkout(shoppingCart, destinationAccount);
        if (success){
            return ResponseEntity.status(HttpStatus.OK).body("{\"Status\": \"success\"");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"Status\": \"failed\"");
        }
    }
    
    
}
