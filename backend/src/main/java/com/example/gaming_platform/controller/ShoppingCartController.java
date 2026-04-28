package com.example.gaming_platform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.impl.CreditCardPayment;
import com.example.gaming_platform.impl.DebitCardPayment;
import com.example.gaming_platform.impl.GiftCardPayment;
import com.example.gaming_platform.service.ShoppingCartService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * REST controller for shopping cart operations.
 */
@RestController
@RequestMapping("/api/shopping-cart")
public class ShoppingCartController {
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
 * Gets a Users Shopping cart.
 *
 * @return user's shopping cart or an empty cart
 */
    @GetMapping("")
    public List<VideoGame> getCart(HttpServletRequest request) throws Exception{
        Long userId = Long.parseLong(request.getAttribute("currentUserId").toString());
        return shoppingCartService.getGames(userId);
    }

/**
 * Executes the postAddGame operation.
 *
 * @param gameId the id of the game to add
 * @return the result of the operation
 */
    @PostMapping("game/{gameId}")
    public ResponseEntity postAddGame(HttpServletRequest request, @PathVariable Long gameId) {
        String success;
        Long userId = Long.parseLong(request.getAttribute("currentUserId").toString());

        success = shoppingCartService.addGame(userId, gameId);
        if (success.equals("Success")){
            return ResponseEntity.status(HttpStatus.OK).body("{\"Status\": \"success\"}");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"Status\": \"%s\"}".formatted(success));
        }
    }

/**
 * Deletes the remove game.
 *
 * @param gameId the id of the game to remove
 * @return the result of the operation
 */
    @DeleteMapping("game/{gameId}")
    public ResponseEntity deleteRemoveGame(HttpServletRequest request, @PathVariable Long gameId) {
        String success;
        Long userId = Long.parseLong(request.getAttribute("currentUserId").toString());

        success = shoppingCartService.removeGame(userId, gameId);
        if (success.equals("Success")){
            return ResponseEntity.status(HttpStatus.OK).body("{\"Status\": \"success\"}");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"Status\": \"%s\"}".formatted(success));
        }
    }

/**
 * Gets the total.
 *
 * @return the total or error message
 */
    @GetMapping("total")
    public ResponseEntity getTotal(HttpServletRequest request) {
        float total;
        String response;
        Long userId = Long.parseLong(request.getAttribute("currentUserId").toString());

        response = shoppingCartService.calcTotalPrice(userId);
        if (response.contains("Success")){
            total = Float.parseFloat(response.replace("Success: ", ""));
            return ResponseEntity.status(HttpStatus.OK).body("{\"total\": \"%.2f\"}".formatted(total));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"Status\": \"%s\"}".formatted(response));
        }
    }

/**
 * Executes the postCheckout operation.
 *
 * @param destinationAccountId the destination account id
 * @param paymentObject the credit card payment object used for processing the payment
 * @return the result of the operation
 */
    @PostMapping("checkout/creditcard/{destinationAccountId}")
    public ResponseEntity postCreditCardCheckout(HttpServletRequest request,
                                    @PathVariable Long destinationAccountId,
                                    @RequestBody CreditCardPayment paymentObject) {
        String success;
        Long userId = Long.parseLong(request.getAttribute("currentUserId").toString());

        success = shoppingCartService.checkout(userId, destinationAccountId, paymentObject);
        if (success.equals("Success")){
            return ResponseEntity.status(HttpStatus.OK).body("{\"Status\": \"success\"}");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"Status\": \"%s\"}".formatted(success));
        }
    }

/**
 * Executes the postCheckout operation.
 *
 * @param destinationAccountId the destination account id
 * @param paymentObject the debit card payment object used for processing the payment
 * @return the result of the operation
 */
    @PostMapping("checkout/debitcard/{destinationAccountId}")
    public ResponseEntity postDebitCardCheckout(HttpServletRequest request,
                                    @PathVariable Long destinationAccountId,
                                    @RequestBody DebitCardPayment paymentObject) {
        String success;
        Long userId = Long.parseLong(request.getAttribute("currentUserId").toString());

        success = shoppingCartService.checkout(userId, destinationAccountId, paymentObject);
        if (success.equals("Success")){
            return ResponseEntity.status(HttpStatus.OK).body("{\"Status\": \"success\"}");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"Status\": \"%s\"}".formatted(success));
        }
    }

/**
 * Executes the postCheckout operation.
 *
 * @param destinationAccountId the destination account id
 * @param paymentObject the gift card payment object used for processing the payment
 * @return the result of the operation
 */
    @PostMapping("checkout/giftcard/{destinationAccountId}")
    public ResponseEntity postGiftCardCheckout(HttpServletRequest request,
                                    @PathVariable Long destinationAccountId,
                                    @RequestBody GiftCardPayment paymentObject) {
        String success;
        Long userId = Long.parseLong(request.getAttribute("currentUserId").toString());

        success = shoppingCartService.checkout(userId, destinationAccountId, paymentObject);
        if (success.equals("Success")){
            return ResponseEntity.status(HttpStatus.OK).body("{\"Status\": \"success\"}");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"Status\": \"%s\"}".formatted(success));
        }
    }
}
