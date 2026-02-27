package com.example.gaming_platform.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.gaming_platform.entity.Orders;
import com.example.gaming_platform.entity.UserProfile;
import com.example.gaming_platform.entity.VideoGame;

public interface OrdersRepository extends CrudRepository<Orders, Long> {
    Orders findByDestinationAccount(UserProfile user);
    VideoGame findByGame(VideoGame game);
}
