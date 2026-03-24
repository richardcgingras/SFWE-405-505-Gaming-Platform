package com.example.gaming_platform.repository;

import com.example.gaming_platform.entity.GameLibrary;
import com.example.gaming_platform.entity.UserProfile;
import org.springframework.data.repository.CrudRepository;

public interface GameLibraryRepository extends CrudRepository<GameLibrary, Long> {
}