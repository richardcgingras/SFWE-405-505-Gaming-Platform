package com.example.gaming_platform.controller;

import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.entity.WebStorePage;
import com.example.gaming_platform.service.WebStoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/webstore")
@CrossOrigin(origins = "*")
public class WebStoreController {

    private final WebStoreService webStoreService;

    public WebStoreController(WebStoreService webStoreService) {
        this.webStoreService = webStoreService;
    }

    // GET /api/webstore
    @GetMapping
    public Iterable<WebStorePage> getAllWebStorePages() {
        return webStoreService.getAllWebStorePages();
    }

    // GET /api/webstore/{id}
    @GetMapping("/{id}")
    public ResponseEntity<WebStorePage> getWebStorePageById(@PathVariable Long id) {
        WebStorePage page = webStoreService.getWebStorePageById(id);

        if (page == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(page);
    }

    // POST /api/webstore
    @PostMapping
    public ResponseEntity<WebStorePage> createWebStorePage(@RequestBody WebStorePage webStorePage) {
        WebStorePage newPage = webStoreService.createWebStorePage(webStorePage);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPage);
    }

    // PUT /api/webstore/{id}
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWebStorePage(@PathVariable Long id) {
        boolean deleted = webStoreService.deleteWebStorePage(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    // GET /api/webstore/{id}/recommendations
    @GetMapping("/{id}/recommendations")
    public ResponseEntity<List<VideoGame>> getRecommendations(@PathVariable Long id) {
        List<VideoGame> games = webStoreService.getRecommendations(id);

        if (games == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(games);
    }

    // POST /api/webstore/{id}/recommendations
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