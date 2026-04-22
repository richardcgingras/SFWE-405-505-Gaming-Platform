package com.example.gaming_platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.gaming_platform.entity.FriendRequest;
import com.example.gaming_platform.entity.UserProfile;

/**
 * Repository for handling friend request database operations.
 */
public interface FriendRequestRepository extends CrudRepository<FriendRequest, Long> {

    // get all pending requests for a receiver
    List<FriendRequest> findByReceiverAndStatus(UserProfile receiver, String status);

    // find a specific request between two users
    Optional<FriendRequest> findBySenderAndReceiverAndStatus(
            UserProfile sender,
            UserProfile receiver,
            String status
    );

    // check if a request already exists
    boolean existsBySenderAndReceiverAndStatus(
            UserProfile sender,
            UserProfile receiver,
            String status
    );
}