package com.example.gaming_platform.controller;

import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.entity.WebStorePage;
import com.example.gaming_platform.service.WebStoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for web store operations.
 */
@RestController
@RequestMapping("/api/webstore")
@CrossOrigin(origins = "*")
public class WebStoreController {

    private final WebStoreService webStoreService;

/**
 * Creates a new WebStoreController instance.
 *
 * @param webStoreService the web store service
 */
    public WebStoreController(WebStoreService webStoreService) {
        this.webStoreService = webStoreService;
    }

    // GET /api/webstore
/**
 * Retrieves all web store pages.
 *
 * @return all web store pages
 */
    @GetMapping
    public Iterable<WebStorePage> getAllWebStorePages() {
        return webStoreService.getAllWebStorePages();
    }

    // GET /api/webstore/{id}
/**
 * Retrieves a web store page by ID.
 *
 * @param id the ID
 * @return the matching web store page when found
 */
    @GetMapping("/{id}")
    public ResponseEntity<WebStorePage> getWebStorePageById(@PathVariable Long id) {
        WebStorePage page = webStoreService.getWebStorePageById(id);

        if (page == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(page);
    }

    // POST /api/webstore
/**
 * Creates a new web store page.
 *
 * @param webStorePage the web store page
 * @return the created web store page
 */
    @PostMapping
    public ResponseEntity<WebStorePage> createWebStorePage(@RequestBody WebStorePage webStorePage) {
        WebStorePage newPage = webStoreService.createWebStorePage(webStorePage);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPage);
    }

    // PUT /api/webstore/{id}
/**
 * Updates the web store page.
 *
 * @param id the ID
 * @param updated the updated
 * @return the updated web store page
 */
    @PutMapping("/{id}")
    public ResponseEntity<WebStorePage> updateWebStorePage(@PathVariable Long id,
                                                           @RequestBody WebStorePage updated) {
        WebStorePage page = webStoreService.updateWebStorePage(id, updated);

        if (page == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(page);
    }

    // DELETE /api/webstore/{id}
/**
 * Deletes the web store page.
 *
 * @param id the ID
 * @return the result of the delete operation
 */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWebStorePage(@PathVariable Long id) {
        boolean deleted = webStoreService.deleteWebStorePage(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    // GET /api/webstore/{id}/recommendations
/**
 * Gets the recommendations.
 *
 * @param id the ID
 * @return the recommendations
 */
    @GetMapping("/{id}/recommendations")
    public ResponseEntity<List<VideoGame>> getRecommendations(@PathVariable Long id) {
        List<VideoGame> games = webStoreService.getRecommendations(id);

        if (games == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(games);
    }

    // POST /api/webstore/{id}/recommendations
/**
 * Adds recommendation.
 *
 * @param id the ID
 * @param game the game
 * @return the updated result
 */
    @PostMapping("/{id}/recommendations")
    public ResponseEntity<WebStorePage> addRecommendation(@PathVariable Long id,
                                                          @RequestBody VideoGame game) {
        WebStorePage page = webStoreService.addRecommendation(id, game);

        if (page == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(page);
    }

    // DELETE /api/webstore/{id}/recommendations/{gameId}
/**
 * Removes recommendation.
 *
 * @param id the ID
 * @param gameId the game ID
 * @return the updated result
 */
    @DeleteMapping("/{id}/recommendations/{gameId}")
    public ResponseEntity<WebStorePage> removeRecommendation(@PathVariable Long id,
                                                             @PathVariable Long gameId) {
        WebStorePage page = webStoreService.removeRecommendation(id, gameId);

        if (page == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(page);
    }
}
