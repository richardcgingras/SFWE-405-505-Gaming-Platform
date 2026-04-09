package com.example.gaming_platform.service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.gaming_platform.Payment;
import com.example.gaming_platform.entity.ShoppingCart;
import com.example.gaming_platform.entity.UserProfile;
import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.entity.Orders;
import com.example.gaming_platform.entity.PaymentResponse;
import com.example.gaming_platform.repository.OrdersRepository;
import com.example.gaming_platform.repository.ShoppingCartRepository;
import com.example.gaming_platform.repository.UserProfileRepository;
import com.example.gaming_platform.repository.VideoGameRepository;

/**
 * Service for shopping cart business operations.
 */
@Service
public class ShoppingCartService {

    private final OrdersRepository ordersRepository;
    private final UserProfileRepository userProfileRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final VideoGameRepository videoGameRepository;

/**
 * Creates a new ShoppingCartService instance.
 *
 * @param shoppingCartRepository the shopping cart repository
 * @param ordersRepository the orders repository
 * @param userProfileRepository the user profile repository
 */
    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository,
                                OrdersRepository ordersRepository,
                                UserProfileRepository userProfileRepository,
                                VideoGameRepository videoGameRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.ordersRepository = ordersRepository;
        this.userProfileRepository = userProfileRepository;
        this.videoGameRepository = videoGameRepository;
    }

/**
 * Adds game.
 *
 * @param shoppingCart the shopping cart
 * @param gameTooAdd the game too add
 * @return the updated result
 */
    public String addGame(Long cartId, Long gameId){
        // Attempt too add the specified game to the shopping cart

        VideoGame gameTooAdd;
        ShoppingCart shoppingCart;

        Optional<VideoGame> gameLookup = videoGameRepository.findById(gameId);
        if (gameLookup.isPresent()){
            gameTooAdd = gameLookup.get();
        } else {
            return "Game does not exist";
        }
        Optional<ShoppingCart> cartLookup = shoppingCartRepository.findById(cartId);
        if (cartLookup != null){
            shoppingCart = cartLookup.get();
        } else {
            return "Shopping cart does not exist";
        }

        // Tests:
        Calendar currentDate = Calendar.getInstance();
        if (currentDate.after(gameTooAdd.getReleaseDate())){
            // Past the release date to continue testing
            boolean gameExists = shoppingCart.getGames().stream()
                .anyMatch(g -> g == gameTooAdd);
            if (gameExists) {
                return "Game is already in the Shopping Cart";
            }
        } else {
            // Game is not available for sale yet
            return "Game is not for sale yet";
        }

        // Add the game to the list
        List<VideoGame> currentGamesList = shoppingCart.getGames();
        currentGamesList.add(gameTooAdd);
        shoppingCart.setGames(currentGamesList);
        shoppingCartRepository.save(shoppingCart);
        return "Success";
    }

/**
 * Removes game.
 *
 * @param shoppingCart the shopping cart
 * @param gameTooRemove the game too remove
 */
    public String removeGame(Long cartId, Long gameId){
        // Remove the game to the list

        VideoGame gameTooRemove;
        ShoppingCart shoppingCart;

        Optional<VideoGame> gameLookup = videoGameRepository.findById(gameId);
        if (gameLookup != null){
            gameTooRemove = gameLookup.get();
        } else {
            return "Game does not exist";
        }
        Optional<ShoppingCart> cartLookup = shoppingCartRepository.findById(cartId);
        if (cartLookup != null){
            shoppingCart = cartLookup.get();
        } else {
            return "Shopping cart does not exist";
        }

        List<VideoGame> currentGamesList = shoppingCart.getGames();
        currentGamesList.remove(gameTooRemove);
        shoppingCart.setGames(currentGamesList);
        shoppingCartRepository.save(shoppingCart);
        return "Success";
    }

/**
 * Calculates the total price.
 *
 * @param shoppingCart the shopping cart
 * @return the calculated total price
 */
    public float calcPrice(ShoppingCart shoppingCart){
        // Calculates the total price of the shopping cart
        // This should probably be a private function as it should be called during the checkout process
        float calcTotal = shoppingCart.getGames().stream()
            .map(VideoGame::getPrice)
            .reduce(0.0f, (sum, price) -> sum + price);
        return calcTotal;
    }

/**
 * Calculates the total price.
 *
 * @param shoppingCart the shopping cart
 * @return the calculated total price
 */
    public String calcTotalPrice(Long cartId){
        // Calculates the total price of the shopping cart

        ShoppingCart shoppingCart;
        Optional<ShoppingCart> cartLookup = shoppingCartRepository.findById(cartId);
        if (cartLookup != null){
            shoppingCart = cartLookup.get();
        } else {
            return "Shopping cart does not exist";
        }

        return "Success: %.2f".formatted(shoppingCart.getTotal());
    }

/**
 * Completes the checkout process.
 *
 * @param shoppingCart the shopping cart
 * @param destinationAccount the destination account
 * @param paymentObject the filled in payment object to be processed
 * @return {@code true} when checkout succeeds
 */
    public String checkout(Long cartId, Long destinationAccountId, Payment paymentObject){
        // Returns a boolean based on if the checkout process was a success

        UserProfile destinationAccount;
        ShoppingCart shoppingCart;
        PaymentResponse paymentSuccessful;

        Optional<UserProfile> accountLookup = userProfileRepository.findById(destinationAccountId);
        if (accountLookup.isEmpty()) {
            return "Account does not exist";
        }
        destinationAccount = accountLookup.get();

        Optional<ShoppingCart> cartLookup = shoppingCartRepository.findById(cartId);
        if (cartLookup.isEmpty()) {
            return "Shopping cart does not exist";
        }
        shoppingCart = cartLookup.get();

        // Test to make sure none of the games in the shopping cart are already in the destination account
        for (VideoGame game : shoppingCart.getGames()) {
            for (VideoGame ownedGame : destinationAccount.getGameLibrary()){
                if (game == ownedGame){
                    // One of the games is already in the account
                    return "Game %s already exists in the account".formatted(game.getName());
                }
            }
        }

        // All checks passed so we can continue with the order
        // Get final price
        float cartTotal = calcPrice(shoppingCart);
        shoppingCart.setPrice(cartTotal);

        // Process payment for the total amount
        paymentSuccessful = paymentObject.processPayment(cartTotal);
        if (!paymentSuccessful.isSuccess()){
            return "Failed to processes payment because: %s".formatted(paymentSuccessful.getMessage());
        }

        // Payment was successful. For each game, go through and process an order (each order is for a single game)
        for (VideoGame game : shoppingCart.getGames()) {
            Calendar orderTime = Calendar.getInstance();
            // Create and save the order
            Orders newOrder = new Orders();
            newOrder.setDestinationAccount(destinationAccount);
            newOrder.setGame(game);
            newOrder.setDate(orderTime);
            newOrder.setPaymentProcessed(true);
            ordersRepository.save(newOrder);
            // Attach the game to the user account
            List<VideoGame> accountLibrary = destinationAccount.getGameLibrary();
            accountLibrary.add(game);
            destinationAccount.setGameLibrary(accountLibrary);
            userProfileRepository.save(destinationAccount);
        }
        // Set the shopping cart to empty
        shoppingCart.setGames(null);
        shoppingCartRepository.save(shoppingCart);

        // Return if successful or not
        return "Success";
    }
}
