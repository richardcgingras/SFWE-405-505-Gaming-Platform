package simulations;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import java.util.*;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class MySimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    // Feeder for random users
    Iterator<Map<String, Object>> userFeeder = Stream.generate(() -> {
        String username = "Gamer_" + UUID.randomUUID().toString().substring(0, 8);
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("email", username + "@test.com");
        map.put("password", "Stress123!");
        return map;
    }).iterator();

    // Feeder for random games
    Iterator<Map<String, Object>> gameFeeder = Stream.generate(() -> {
        String gameName = "MegaGame_" + UUID.randomUUID().toString().substring(0, 8);
        Map<String, Object> map = new HashMap<>();
        map.put("gameName", gameName);
        map.put("price", 19.99 + (new Random().nextDouble() * 50.0));
        return map;
    }).iterator();

    ScenarioBuilder fullPlatformStressTest = scenario("Full Platform Stress Test")
            .feed(userFeeder)
            // 1. Account Creation & Auth
            .exec(
                    http("Register New User")
                            .post("/api/user-profiles")
                            .body(StringBody(
                                    "{ \"userName\": \"#{username}\", \"email\": \"#{email}\", \"password\": \"#{password}\", \"status\": \"ACTIVE\" }"))
                            .check(status().is(201)))
            .pause(1)
            .exec(
                    http("Login")
                            .post("/api/auth/login")
                            .body(StringBody("{ \"username\": \"#{username}\", \"password\": \"#{password}\" }"))
                            .check(status().is(200))
                            .check(jsonPath("$.accessToken").saveAs("jwtToken"))
                            .check(jsonPath("$.userId").saveAs("currentUserId")))
            .pause(1)
            // 2. Browsing & Discovery (Public/Authenticated Metadata)
            .exec(
                    http("Get Video Games")
                            .get("/api/video-games")
                            .header("Authorization", "Bearer #{jwtToken}")
                            .check(status().is(200)))
            .exec(
                    http("Get Categories")
                            .get("/api/categories")
                            .header("Authorization", "Bearer #{jwtToken}")
                            .check(status().in(200, 404)))
            .exec(
                    http("Get Developers")
                            .get("/api/developers")
                            .header("Authorization", "Bearer #{jwtToken}")
                            .check(status().in(200, 404)))
            .pause(1)
            // 3. Content Creation (Games & Reviews)
            .feed(gameFeeder)
            .exec(
                    http("Create Video Game")
                            .post("/api/video-games")
                            .header("Authorization", "Bearer #{jwtToken}")
                            .body(StringBody("""
                                        {
                                          "name": "#{gameName}",
                                          "releaseDate": "2026-05-01T00:00:00.000+00:00",
                                          "category": [],
                                          "system": ["PC"],
                                          "price": #{price},
                                          "size": 30.0,
                                          "ageRating": "T"
                                        }
                                    """))
                            .check(status().is(201))
                            .check(jsonPath("$.id").saveAs("newGameId")))
            .pause(1)
            .exec(
                    http("Submit Game Review")
                            .post("/api/reviews/submit")
                            .header("Authorization", "Bearer #{jwtToken}")
                            .queryParam("userId", "#{currentUserId}")
                            .queryParam("gameId", "#{newGameId}")
                            .queryParam("comments", "Amazing performance and gameplay!")
                            .queryParam("rating", "5")
                            .check(status().is(201)))
            .pause(1)
            // 4. Shopping & Wishlist
            .exec(
                    http("Add to Wishlist")
                            .post("/api/wishlist/game/#{newGameId}")
                            .header("Authorization", "Bearer #{jwtToken}")
                            .check(status().in(200, 409)))
            .exec(
                    http("Add to Shopping Cart")
                            .post("/api/shopping-cart/game/#{newGameId}")
                            .header("Authorization", "Bearer #{jwtToken}")
                            .check(status().in(200, 409)))
            .exec(
                    http("Get Cart Total")
                            .get("/api/shopping-cart/total")
                            .header("Authorization", "Bearer #{jwtToken}")
                            .check(status().is(200)))
            .pause(1)
            // 5. Social & Connections
            .exec(
                    http("Send Friend Request")
                            .post("/api/friend-requests")
                            .header("Authorization", "Bearer #{jwtToken}")
                            .body(StringBody("{ \"senderId\": #{currentUserId}, \"receiverId\": 1 }")) // Target user 1
                                                                                                       // as a dummy
                            .check(status().in(200, 400, 404)))
            .exec(
                    http("Get Chat Messages")
                            .get("/api/messages/#{currentUserId}/1")
                            .header("Authorization", "Bearer #{jwtToken}")
                            .check(status().in(200, 404)))
            .pause(1)
            // 6. Final Account Actions
            .exec(
                    http("Get User Profile")
                            .get("/api/user-profiles/#{currentUserId}")
                            .header("Authorization", "Bearer #{jwtToken}")
                            .check(status().is(200)))
            .exec(
                    http("Update Bio")
                            .put("/api/user-profiles/#{currentUserId}/bio")
                            .header("Authorization", "Bearer #{jwtToken}")
                            .body(StringBody("Busy gaming and testing APIs!"))
                            .check(status().is(200)));

    {
        setUp(
                fullPlatformStressTest.injectOpen(
                        rampUsers(400).during(30),
                        constantUsersPerSec(10).during(20)))
                .protocols(httpProtocol);
    }
}