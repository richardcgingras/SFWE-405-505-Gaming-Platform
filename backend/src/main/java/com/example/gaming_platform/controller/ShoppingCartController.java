package com.example.gaming_platform.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gaming_platform.impl.CreditCardPayment;
import com.example.gaming_platform.impl.DebitCardPayment;
import com.example.gaming_platform.impl.GiftCardPayment;
import com.example.gaming_platform.service.ShoppingCartService;

/**
 * REST controller for shopping cart operations.
 */
@RestController
@RequestMapping("/api/shopping-cart")
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
 * @param cartId the shopping cart id
 * @param gameId the id of the game to add
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
 * @param cartId the shopping cart id
 * @param gameId the id of the game to remove
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
 * @param cartId the shopping cart id
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
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"Status\": \"%s\"}".formatted(response));
        }
    }

/**
 * Executes the postCheckout operation.
 *
 * @param cartId the shopping cart id
 * @param destinationAccountId the destination account id
 * @param paymentObject the credit card payment object used for processing the payment
 * @return the result of the operation
 */
    @PostMapping("checkout/creditcard/{cartId}/{destinationAccountId}")
    public ResponseEntity postCreditCardCheckout(@PathVariable Long cartId,
                                    @PathVariable Long destinationAccountId,
                                    @RequestBody CreditCardPayment paymentObject) {
        String success;
        success = shoppingCartService.checkout(cartId, destinationAccountId, paymentObject);
        if (success.equals("Success")){
            return ResponseEntity.status(HttpStatus.OK).body("{\"Status\": \"success\"}");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"Status\": \"%s\"}".formatted(success));
        }
    }

/**
 * Executes the postCheckout operation.
 *
 * @param cartId the shopping cart id
 * @param destinationAccountId the destination account id
 * @param paymentObject the debit card payment object used for processing the payment
 * @return the result of the operation
 */
    @PostMapping("checkout/debitcard/{cartId}/{destinationAccountId}")
    public ResponseEntity postDebitCardCheckout(@PathVariable Long cartId,
                                    @PathVariable Long destinationAccountId,
                                    @RequestBody DebitCardPayment paymentObject) {
        String success;
        success = shoppingCartService.checkout(cartId, destinationAccountId, paymentObject);
        if (success.equals("Success")){
            return ResponseEntity.status(HttpStatus.OK).body("{\"Status\": \"success\"}");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"Status\": \"%s\"}".formatted(success));
        }
    }

/**
 * Executes the postCheckout operation.
 *
 * @param cartId the shopping cart id
 * @param destinationAccountId the destination account id
 * @param paymentObject the gift card payment object used for processing the payment
 * @return the result of the operation
 */
    @PostMapping("checkout/giftcard/{cartId}/{destinationAccountId}")
    public ResponseEntity postGiftCardCheckout(@PathVariable Long cartId,
                                    @PathVariable Long destinationAccountId,
                                    @RequestBody GiftCardPayment paymentObject) {
        String success;
        success = shoppingCartService.checkout(cartId, destinationAccountId, paymentObject);
        if (success.equals("Success")){
            return ResponseEntity.status(HttpStatus.OK).body("{\"Status\": \"success\"}");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"Status\": \"%s\"}".formatted(success));
        }
    }
}
