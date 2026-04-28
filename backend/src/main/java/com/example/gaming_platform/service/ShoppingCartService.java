package com.example.gaming_platform.service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.gaming_platform.Payment;
import com.example.gaming_platform.entity.Orders;
import com.example.gaming_platform.entity.PaymentResponse;
import com.example.gaming_platform.entity.ShoppingCart;
import com.example.gaming_platform.entity.UserProfile;
import com.example.gaming_platform.entity.VideoGame;
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
 * Gets all games in a user's shopping cart games
 *
 * @param userId id of a user
 * @return List of Games
 */
    public List<VideoGame> getGames(Long userId) {
        ShoppingCart shoppingCart = getCart(userId);
        return (shoppingCart != null) ? shoppingCart.getGames() : List.of();
    }

/**
 * Gets a user's shopping cart
 *
 * @param userId id of a user
 * @return shopping cart
 */
    public ShoppingCart getCart(Long userId){
        Optional<UserProfile> userOpt = userProfileRepository.findById(userId);
        if (userOpt.isEmpty()) return null;
        
        UserProfile user = userOpt.get();
        ShoppingCart shoppingCart = shoppingCartRepository.findByAccount(user);
        
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
            shoppingCart.setAccount(user);
            shoppingCart.setGames(new java.util.ArrayList<>());
            shoppingCart.setPrice(0.0f);
            shoppingCart = shoppingCartRepository.save(shoppingCart);
            
            user.setShoppingCart(shoppingCart);
            userProfileRepository.save(user);
        }
        return shoppingCart;
    }

/**
 * Adds game.
 *
 * @param userId the user id
 * @param gameId the id of the game to add
 * @return the operation result
 */
    public String addGame(Long userId, Long gameId){
        // Attempt too add the specified game to the shopping cart

        VideoGame gameTooAdd;
        ShoppingCart shoppingCart = getCart(userId);

        Optional<VideoGame> gameLookup = videoGameRepository.findById(gameId);
        if (gameLookup.isPresent()){
            gameTooAdd = gameLookup.get();
        } else {
            return "Game does not exist";
        }
        if (shoppingCart == null){
            return "User not found";
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
 * @param userId the user id
 * @param gameId the id of the game to add
 * @return the operation result
 */
    public String removeGame(Long userId, Long gameId){
        // Remove the game to the list

        VideoGame gameTooRemove;
        ShoppingCart shoppingCart = getCart(userId);

        Optional<VideoGame> gameLookup = videoGameRepository.findById(gameId);
        if (gameLookup.isPresent()){
            gameTooRemove = gameLookup.get();
        } else {
            return "Game does not exist";
        }
        if (shoppingCart == null){
            return "User not found";
        }

        List<VideoGame> currentGamesList = shoppingCart.getGames();
        currentGamesList.remove(gameTooRemove);
        shoppingCart.setGames(currentGamesList);
        shoppingCartRepository.save(shoppingCart);
        return "Success";
    }

/**
 * Calculates the total price of a users shopping cart.
 *
 * @param userId Id of the user
 * @return the calculated total price
 */
    public float calcPrice(Long userId){
        // Calculates the total price of the shopping cart
        // This should probably be a private function as it should be called during the checkout process
        ShoppingCart shoppingCart = getCart(userId);
        float calcTotal = shoppingCart.getGames().stream()
            .map(VideoGame::getPrice)
            .reduce(0.0f, (sum, price) -> sum + price);
        return calcTotal;
    }

/**
 * Calculates the total price.
 *
 * @param userId the user id
 * @return the calculated total price or an error message
 */
    public String calcTotalPrice(Long userId){
        // Calculates the total price of the shopping cart

        ShoppingCart shoppingCart = getCart(userId);
        if (shoppingCart == null){
            return "User not found";
        }

        return "Success: %.2f".formatted(shoppingCart.getTotal());
    }

/**
 * Completes the checkout process.
 *
 * @param userId the user id
 * @param destinationAccountId the destination account id
 * @param paymentObject the filled in payment object to be processed
 * @return the operation result
 */
    public String checkout(Long userId, Long destinationAccountId, Payment paymentObject){
        // Returns a boolean based on if the checkout process was a success

        UserProfile destinationAccount;
        ShoppingCart shoppingCart = getCart(userId);
        PaymentResponse paymentSuccessful;

        Optional<UserProfile> accountLookup = userProfileRepository.findById(destinationAccountId);
        if (accountLookup.isEmpty()) {
            return "Account does not exist";
        }
        destinationAccount = accountLookup.get();

        // Test to make sure none of the games in the shopping cart are already in the destination account
        // TODO: this was using the game list in the user profile rather than the gamelibrary entity
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
        float cartTotal = calcPrice(userId);
        shoppingCart.setPrice(cartTotal);

        // Process payment for the total amount
        paymentSuccessful = paymentObject.processPayment(cartTotal);
        if (!paymentSuccessful.isSuccess()){
            return "Failed to processes payment because: %s".formatted(paymentSuccessful.getMessage());
        }

        // Payment was successful. For each game, go through and process an order (each order is for a single game)
        // TODO: this was using the game list in the user profile rather than the gamelibrary entity
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
