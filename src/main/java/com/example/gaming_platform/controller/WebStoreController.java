package com.example.gaming_platform.controller;

import com.example.gaming_platform.entity.WebStorePage;
import com.example.gaming_platform.repository.WebStoreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//s
@RestController
@RequestMapping("/api/webstore")
@CrossOrigin(origins = "*")
public class WebStoreController {

    private final WebStoreRepository repository;

    // Corrected Constructor Injection
    public WebStoreController(WebStoreRepository repository) {
        this.repository = repository;
    }

    // GET /api/webstore
    @GetMapping
    public Iterable<WebStorePage> getAllWebStorePages() {
        return repository.findAll();
    }

    // GET /api/webstore/{id}
    @GetMapping("/{id}")
    public ResponseEntity<WebStorePage> getWebStorePageById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/webstore
    @PostMapping(consumes = "application/json")
    public ResponseEntity<WebStorePage> createWebStorePage(@RequestBody WebStorePage webStorePage) {
        WebStorePage saved = repository.save(webStorePage);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}