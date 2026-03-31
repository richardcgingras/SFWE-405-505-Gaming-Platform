package com.example.gaming_platform;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.gaming_platform.entity.ShoppingCart;
import com.example.gaming_platform.entity.ShoppingCartRequest;
import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.repository.ShoppingCartRepository;
import com.example.gaming_platform.repository.VideoGameRepository;
import com.example.gaming_platform.service.ShoppingCartService;
import com.example.gaming_platform.repository.UserProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.gaming_platform.entity.UserProfile;

@SpringBootTest
public class ShoppingCartAndOrderTests {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private VideoGameRepository videoGameRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Mock
    private ShoppingCartService shoppingCartService;

    @Test
    void SaveShoppingCartRepo() {
        // Verify basic functionality that saving something to the repo works
        ShoppingCart shoppingCart = new ShoppingCart();
        List<VideoGame> cartGames = new ArrayList<>();
        cartGames.add(videoGameRepository.findByName("weller Evil"));
        shoppingCart.setGames(cartGames);
        shoppingCart.setPrice(11.22f);
        shoppingCart.setAccount(userProfileRepository.findByUserName("sammyt"));
        System.out.println(shoppingCart.getGames());
        System.out.println(shoppingCart.getTotal());
        System.out.println(shoppingCart.getAccount());
        shoppingCartRepository.save(shoppingCart);
    }
}

// Non of these tests currently work
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private VideoGameRepository videoGameRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // @Test
    // void shouldAddGameToShoppingCart() throws Exception {
    //     VideoGame testGame = videoGameRepository.findByName("FallIn");

    //     // Create proper cart and game objects
    //     ShoppingCart testCart = new ShoppingCart();
    //     testCart.setAccount(userProfileRepository.findByUserName("sammyt"));

    //     // Create request body containing both entities
    //     ShoppingCartRequest request = new ShoppingCartRequest();
    //     request.setShoppingCart(testCart);
    //     request.setVideoGame(testGame);

    //     // Convert to JSON
    //     String jsonContent = objectMapper.writeValueAsString(request);

    //     mockMvc.perform(MockMvcRequestBuilders.post("/api/shopping-cart/game")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(jsonContent))
    //             .andExpect(status().isOk());
    // }

    // @Test
    // void shouldAddAnotherGame() throws Exception {
    //     // Create a second game for testing multiple adds
    //     VideoGame anotherGame = videoGameRepository.findByName("Dweller Evil");
    //     ShoppingCart testCart = new ShoppingCart();

    //     // Create request body containing both entities
    //     ShoppingCartRequest request = new ShoppingCartRequest();
    //     request.setShoppingCart(testCart);
    //     request.setVideoGame(anotherGame);

    //     // Convert to JSON
    //     String jsonContent = objectMapper.writeValueAsString(request);
        
    //     mockMvc.perform(MockMvcRequestBuilders.post("/api/shopping-cart/game")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(jsonContent))
    //             .andExpect(status().isOk());
    // }

    // @Test
    // void shouldGetShoppingCart() throws Exception {
    //     ShoppingCart testCart = new ShoppingCart();
    //     VideoGame testGame = videoGameRepository.findByName("FallIn");
    //     UserProfile testUserProfile = userProfileRepository.findByUserName("johna");
        
    //     List<VideoGame> games = new ArrayList<>();
    //     games.add(testGame);
        
    //     shoppingCartService.addGameToCart(games, 19.99f, testUserProfile);
    //     ShoppingCart savedCart = shoppingCartRepository.save(new ShoppingCart());

    //     mockMvc.perform(MockMvcRequestBuilders.get("/api/shopping-cart/" + savedCart.getId()))
    //             .andExpect(status().isOk())
    //             .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    //             .andExpect(jsonPath("$.price").value(19.99f));
    // }
}
