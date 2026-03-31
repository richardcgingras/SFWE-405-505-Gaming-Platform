package simulations;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class MySimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
        .baseUrl("http://localhost:8080")
        .acceptHeader("application/json")
        .contentTypeHeader("application/json");

    // --- Scenario: Video Games ---
    ScenarioBuilder videoGames = scenario("Video Games")
        .exec(
            http("Get All Video Games")
                .get("/api/video-games")
                .check(status().is(200))
        )
        .pause(1)
        .exec(
            http("Create Video Game")
                .post("/api/video-games")
                .body(StringBody("""
                    {
                      "name": "Halo Infinite",
                      "releaseDate": "2026-02-17T00:00:00.000+00:00",
                      "category": "ACTION",
                      "system": "XBOX"
                    }
                """)).asJson()
                .check(status().is(200))
        );

    // --- Scenario: User Profiles ---
    ScenarioBuilder userProfiles = scenario("User Profiles")
        .exec(
            http("Get All User Profiles")
                .get("/api/user-profiles")
                .check(status().is(200))
        )
        .pause(1)
        .exec(
            http("Create User Profile")
                .post("/api/user-profiles")
                .body(StringBody("""
                    {
                      "email": "renae@example.com",
                      "userName": "Gamer1",
                      "status": "ACTIVE",
                      "preferredCategories": ["RPG", "INDIE"],
                      "gameLibrary": []
                    }
                """)).asJson()
                .check(status().is(200))
        );

    // --- Scenario: Orders ---
    ScenarioBuilder orders = scenario("Orders")
        .exec(
            http("Get All Orders")
                .get("/api/orders")
                .check(status().is(200))
        )
        .pause(1)
        .exec(
            http("Create Order")
                .post("/api/orders")
                .body(StringBody("""
                    {
                      "orderDate": "2026-03-24T12:00:00",
                      "totalAmount": 59.99
                    }
                """)).asJson()
                .check(status().is(200))
        );

    // --- Scenario: Developers ---
    ScenarioBuilder developers = scenario("Developers")
        .exec(
            http("Get All Developers")
                .get("/api/developers")
                .check(status().is(200))
        )
        .pause(1)
        .exec(
            http("Create Developer")
                .post("/api/developers")
                .body(StringBody("""
                    {
                      "name": "Nintendo"
                    }
                """)).asJson()
                .check(status().is(200))
        );

    // --- Scenario: Webstore ---
    ScenarioBuilder webstore = scenario("Webstore")
        .exec(
            http("Get All Webstores")
                .get("/api/webstore")
                .check(status().is(200))
        )
        .pause(1)
        .exec(
            http("Create Webstore")
                .post("/api/webstore")
                .body(StringBody("""
                    {
                      "name": "Main Store"
                    }
                """)).asJson()
                .check(status().is(200))
        );

    {
        setUp(
            videoGames.injectOpen(rampUsers(10).during(10)),
            userProfiles.injectOpen(rampUsers(10).during(10)),
            orders.injectOpen(rampUsers(10).during(10)),
            developers.injectOpen(rampUsers(10).during(10)),
            webstore.injectOpen(rampUsers(10).during(10))
        ).protocols(httpProtocol);
    }
}