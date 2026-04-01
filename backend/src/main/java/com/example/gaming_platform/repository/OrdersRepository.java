package com.example.gaming_platform.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.gaming_platform.entity.Orders;
import com.example.gaming_platform.entity.UserProfile;
import com.example.gaming_platform.entity.VideoGame;

/**
 * Repository for persisting and querying orders records.
 */
public interface OrdersRepository extends CrudRepository<Orders, Long> {
/**
 * Finds by destination account.
 *
 * @param user the user
 * @return the matching by destination account
 */
    Orders findByDestinationAccount(UserProfile user);
/**
 * Finds by game.
 *
 * @param game the game
 * @return the matching by game
 */
    VideoGame findByGame(VideoGame game);
}
