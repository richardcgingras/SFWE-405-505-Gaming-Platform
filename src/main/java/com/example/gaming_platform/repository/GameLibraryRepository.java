package com.example.gaming_platform.repository;

import com.example.gaming_platform.entity.GameLibrary;
import com.example.gaming_platform.entity.UserProfile;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

/**
 * Repository for persisting and querying game library records.
 */
public interface GameLibraryRepository extends CrudRepository<GameLibrary, Long> {
/**
 * Finds by owner.
 *
 * @param owner the owner
 * @return the matching by owner
 */
    GameLibrary findByOwner(UserProfile owner);
}
