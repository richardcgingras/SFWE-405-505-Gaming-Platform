package com.example.gaming_platform.service;

import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.entity.WebStorePage;
import com.example.gaming_platform.repository.WebStoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for web store business operations.
 */
@Service
public class WebStoreService {

    private WebStoreRepository repository;

/**
 * Creates a new WebStoreService instance.
 *
 * @param repository the repository
 */
    public WebStoreService(WebStoreRepository repository) {
        this.repository = repository;
    }

/**
 * Retrieves all web store pages.
 *
 * @return all web store pages
 */
    public Iterable<WebStorePage> getAllWebStorePages() {
        return repository.findAll();
    }

/**
 * Retrieves a web store page by ID.
 *
 * @param id the ID
 * @return the matching web store page when found
 */
    public WebStorePage getWebStorePageById(Long id) {
        return repository.findById(id).orElse(null);
    }

/**
 * Creates a new web store page.
 *
 * @param webStorePage the web store page
 * @return the created web store page
 */
    public WebStorePage createWebStorePage(WebStorePage webStorePage) {
        return repository.save(webStorePage);
    }

/**
 * Updates the web store page.
 *
 * @param id the ID
 * @param updated the updated
 * @return the updated web store page
 */
    public WebStorePage updateWebStorePage(Long id, WebStorePage updated) {
        WebStorePage page = repository.findById(id).orElse(null);

        if (page != null) {
            page.setName(updated.getName());
            page.setDescription(updated.getDescription());
            page.setImageUrl(updated.getImageUrl());
            page.setDeveloperRecommendations(updated.getDeveloperRecommendations());
            page.setGame(updated.getGame());
            return repository.save(page);
        }

        return null;
    }

/**
 * Deletes the web store page.
 *
 * @param id the ID
 * @return the result of the delete operation
 */
    public boolean deleteWebStorePage(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

/**
 * Gets the recommendations.
 *
 * @param id the ID
 * @return the recommendations
 */
    public List<VideoGame> getRecommendations(Long id) {
        WebStorePage page = repository.findById(id).orElse(null);

        if (page != null) {
            return page.getDeveloperRecommendations();
        }

        return null;
    }

/**
 * Adds recommendation.
 *
 * @param id the ID
 * @param game the game
 * @return the updated result
 */
    public WebStorePage addRecommendation(Long id, VideoGame game) {
        WebStorePage page = repository.findById(id).orElse(null);

        if (page != null) {
            page.getDeveloperRecommendations().add(game);
            return repository.save(page);
        }

        return null;
    }


/**
 * Removes recommendation.
 *
 * @param id the ID
 * @param gameId the game ID
 * @return the updated result
 */
    public WebStorePage removeRecommendation(Long id, Long gameId) {
        WebStorePage page = repository.findById(id).orElse(null);

        if (page != null) {
            page.getDeveloperRecommendations()
                    .removeIf(g -> g.getId() == gameId);
            return repository.save(page);
        }

        return null;
    }
}
