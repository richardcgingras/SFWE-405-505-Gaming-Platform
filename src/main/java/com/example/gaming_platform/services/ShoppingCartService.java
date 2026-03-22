package com.example.gaming_platform.services;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gaming_platform.entity.ShoppingCart;
import com.example.gaming_platform.entity.UserProfile;
import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.entity.Orders;
import com.example.gaming_platform.repository.OrdersRepository;
import com.example.gaming_platform.repository.ShoppingCartRepository;
import com.example.gaming_platform.repository.UserProfileRepository;

@Service
public class ShoppingCartService {

    private final OrdersRepository ordersRepository;
    private final UserProfileRepository userProfileRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository,
                                OrdersRepository ordersRepository,
                                UserProfileRepository userProfileRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.ordersRepository = ordersRepository;
        this.userProfileRepository = userProfileRepository;
    }

    public boolean addGame(ShoppingCart shoppingCart, VideoGame gameTooAdd){
        // Attempt too add the specified game to the shopping cart
        // If a check fails return false, else true
        boolean success = true;

        // Tests:
        Date currentDate = new Date();
        if (currentDate.after(gameTooAdd.getReleaseDate())){
            // Past the release date to continue testing
            for (VideoGame game : shoppingCart.getGames()) {
                if (game == gameTooAdd){
                    // Game is already in the shopping cart
                    success = false;
                }
            }
        } else {
            // Game is not available for sale yet
            success = false;
        }

        // Add the game to the list
        List<VideoGame> currentGamesList = shoppingCart.getGames();
        currentGamesList.add(gameTooAdd);
        shoppingCart.setGames(currentGamesList);
        shoppingCartRepository.save(shoppingCart);
        return success;
    }

    public void removeGame(ShoppingCart shoppingCart, VideoGame gameTooRemove){
        // Remove the game to the list
        List<VideoGame> currentGamesList = shoppingCart.getGames();
        currentGamesList.remove(gameTooRemove);
        shoppingCart.setGames(currentGamesList);
        shoppingCartRepository.save(shoppingCart);
    }

    public double calcTotalPrice(ShoppingCart shoppingCart){
        // Calculates the total price of the shopping cart
        // This should probably be a private function as it should be called during the checkout process
        double calcTotal = 0.0;
        // Iterate over the games and sum their prices
        for (VideoGame game : shoppingCart.getGames()) {
            calcTotal += game.getPrice();
        }
        return calcTotal;
    }

    public boolean checkout(ShoppingCart shoppingCart, UserProfile destinationAccount){
        // Returns a boolean based on if the checkout process was a success
        boolean success = true;

        // Test to make sure none of the games in the shopping cart are already in the destination account
        for (VideoGame game : shoppingCart.getGames()) {
            for (VideoGame ownedGame : destinationAccount.getGameLibrary()){
                if (game == ownedGame){
                    // One of the games is already in the account
                    success = false;
                }
            }
        }
        if (success){
            // All checks passed so we can continue with the order
            // Get final price
            double cartTotal = calcTotalPrice(shoppingCart);
            shoppingCart.setPrice(cartTotal);

            // Process payment for the total amount
            // TODO: 0 idea how we plan on handling this, given it would all be faked can we just do a static true?

            if (success){
                // Payment was successful. For each game, go through and process an order (each order is for a single game)
                for (VideoGame game : shoppingCart.getGames()) {
                    Date d1 = new Date();
                    // Create and save the order
                    Orders newOrder = new Orders();
                    newOrder.setDestinationAccount(destinationAccount);
                    newOrder.setGame(game);
                    newOrder.setDate(d1);
                    newOrder.setPaymentProcessed(true);
                    ordersRepository.save(newOrder);
                    // Attach the game to the user account
                    destinationAccount.addGame(game);
                    userProfileRepository.save(destinationAccount);
                }
                // Set the shopping cart to empty
                shoppingCart.setGames(null);
                shoppingCartRepository.save(shoppingCart);
            }
        }
        // Return if successful or not
        return success;
    }
}
