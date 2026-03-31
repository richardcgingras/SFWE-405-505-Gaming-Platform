package com.example.gaming_platform.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.gaming_platform.entity.ShoppingCart;
import com.example.gaming_platform.entity.UserProfile;
import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.service.ShoppingCartService;

/**
 * REST controller for shopping cart operations.
 */
@RestController
@RequestMapping("/api/shopping-cart")
@CrossOrigin(origins = "*")
public class ShoppingCartController {
    // private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartService shoppingCartService;

/**
 * Creates a new ShoppingCartController instance.
 *
 * @param shoppingCartService the shopping cart service
 */
    public ShoppingCartController(ShoppingCartService shoppingCartService){
        this.shoppingCartService = shoppingCartService;
    }

/**
 * Executes the postAddGame operation.
 *
 * @PathVariable cartId the shopping cart Id
 * @PathVariable gameId the Id of the game too add
 * @return the result of the operation
 */
    @PostMapping("game/{cartId}/{gameId}")
    public ResponseEntity postAddGame(@PathVariable Long cartId, @PathVariable Long gameId) {
        String success;
        System.out.println("User trying to add a game to the cart, cartId: %d, gameId: %d".formatted(cartId, gameId));
        success = shoppingCartService.addGame(cartId, gameId);
        if (success.equals("Success")){
            return ResponseEntity.status(HttpStatus.OK).body("{\"Status\": \"success\"}");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"Status\": \"%s\"}".formatted(success));
        }
    }

/**
 * Deletes the remove game.
 *
 * @PathVariable cartId the shopping cart Id
 * @PathVariable gameId the Id of the game too remove
 * @return the result of the operation
 */
    @DeleteMapping("game/{cartId}/{gameId}")
    public ResponseEntity deleteRemoveGame(@PathVariable Long cartId, @PathVariable Long gameId) {
        String success;
        success = shoppingCartService.removeGame(cartId, gameId);
        if (success.equals("Success")){
            return ResponseEntity.status(HttpStatus.OK).body("{\"Status\": \"success\"}");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"Status\": \"%s\"}".formatted(success));
        }
    }

/**
 * Gets the total.
 *
 * @PathVariable cartId the shopping cart Id
 * @return the total or error message
 */
    @GetMapping("total/{cartId}")
    public ResponseEntity getTotal(@PathVariable Long cartId) {
        float total;
        String response;
        response = shoppingCartService.calcTotalPrice(cartId);
        if (response.contains("Success")){
            total = Float.parseFloat(response.replace("Success: ", ""));
            return ResponseEntity.status(HttpStatus.OK).body("{\"total\": \"%.2f\"}".formatted(total));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"Status\": \"%s\"}".formatted(success));
        }
    }

/**
 * Executes the postCheckouString operation.
 *
 * @PathVariable cartId the shopping cart Id
 * @PathVariable destinationAccountId id of the destination account
 * @return the result of the operation
 */
    @PostMapping("checkout/{cartId}/{accountId}")
    public ResponseEntity postCheckoutString(@PathVariable Long cartId, @PathVariable Long destinationAccountId) {
        String success;
        success = shoppingCartService.checkout(cartId, destinationAccountId);
        if (success.equals("Success")){
            return ResponseEntity.status(HttpStatus.OK).body("{\"Status\": \"success\"}");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"Status\": \"%s\"}".formatted(success));
        }
    }
}
