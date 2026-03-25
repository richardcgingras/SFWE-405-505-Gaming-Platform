package com.example.gaming_platform.service;

import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.entity.WebStorePage;
import com.example.gaming_platform.repository.WebStoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebStoreService {

    private WebStoreRepository repository;

    public WebStoreService(WebStoreRepository repository) {
        this.repository = repository;
    }

    public Iterable<WebStorePage> getAllWebStorePages() {
        return repository.findAll();
    }

    public WebStorePage getWebStorePageById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public WebStorePage createWebStorePage(WebStorePage webStorePage) {
        return repository.save(webStorePage);
    }

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

    public boolean deleteWebStorePage(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    public List<VideoGame> getRecommendations(Long id) {
        WebStorePage page = repository.findById(id).orElse(null);

        if (page != null) {
            return page.getDeveloperRecommendations();
        }

        return null;
    }

    public WebStorePage addRecommendation(Long id, VideoGame game) {
        WebStorePage page = repository.findById(id).orElse(null);

        if (page != null) {
            page.getDeveloperRecommendations().add(game);
            return repository.save(page);
        }

        return null;
    }


    public WebStorePage removeRecommendation(Long id, Long gameId) {
        WebStorePage page = repository.findById(id).orElse(null);

        if (page != null) {
            page.getDeveloperRecommendations()
                    .removeIf(g -> g.getId().equals(gameId));
            return repository.save(page);
        }

        return null;
    }
}