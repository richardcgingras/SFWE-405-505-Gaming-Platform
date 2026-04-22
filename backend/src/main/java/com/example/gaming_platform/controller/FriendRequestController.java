package com.example.gaming_platform.controller;

import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.gaming_platform.entity.FriendRequest;
import com.example.gaming_platform.entity.FriendRequestRequest;
import com.example.gaming_platform.entity.UserProfile;
import com.example.gaming_platform.repository.FriendRequestRepository;
import com.example.gaming_platform.repository.UserProfileRepository;

/**
 * Controller that handles friend request actions.
 */
@RestController
@RequestMapping("/api/friend-requests")
@CrossOrigin(origins = "*")
public class FriendRequestController {

    private final FriendRequestRepository friendRequestRepository;
    private final UserProfileRepository userProfileRepository;

    public FriendRequestController(
            FriendRequestRepository friendRequestRepository,
            UserProfileRepository userProfileRepository
    ) {
        this.friendRequestRepository = friendRequestRepository;
        this.userProfileRepository = userProfileRepository;
    }

    /**
     * Sends a friend request from one user to another.
     */
    @PostMapping
    public ResponseEntity<?> sendFriendRequest(@RequestBody FriendRequestRequest request) {

        // basic validation
        if (request.getSenderId() == null || request.getReceiverId() == null) {
            return ResponseEntity.badRequest().body("senderId and receiverId are required");
        }

        if (request.getSenderId().equals(request.getReceiverId())) {
            return ResponseEntity.badRequest().body("Cannot send request to yourself");
        }

        UserProfile sender = userProfileRepository.findById(request.getSenderId()).orElse(null);
        UserProfile receiver = userProfileRepository.findById(request.getReceiverId()).orElse(null);

        if (sender == null || receiver == null) {
            return ResponseEntity.notFound().build();
        }

        // check if already friends
        if (sender.getFriends() != null &&
            sender.getFriends().stream().anyMatch(f -> f.getId().equals(receiver.getId()))) {
            return ResponseEntity.badRequest().body("Already friends");
        }

        // check if request already exists
        boolean alreadyPending =
                friendRequestRepository.existsBySenderAndReceiverAndStatus(sender, receiver, "PENDING") ||
                friendRequestRepository.existsBySenderAndReceiverAndStatus(receiver, sender, "PENDING");

        if (alreadyPending) {
            return ResponseEntity.badRequest().body("Request already pending");
        }

        // create new request
        FriendRequest saved = friendRequestRepository.save(
                new FriendRequest(sender, receiver, "PENDING")
        );

        Map<String, Object> response = new HashMap<>();
        response.put("id", saved.getId());
        response.put("status", saved.getStatus());
        response.put("message", "Request Sent");

        return ResponseEntity.ok(response);
    }

    /**
     * Gets all incoming friend requests for a user.
     */
    @GetMapping("/received/{userId}")
    public ResponseEntity<?> getReceivedRequests(@PathVariable Long userId) {

        UserProfile receiver = userProfileRepository.findById(userId).orElse(null);
        if (receiver == null) {
            return ResponseEntity.notFound().build();
        }

        List<FriendRequest> requests =
                friendRequestRepository.findByReceiverAndStatus(receiver, "PENDING");

        return ResponseEntity.ok(requests);
    }

    /**
     * Accepts a friend request and adds users to each other's friend list.
     */
    @PostMapping("/{requestId}/accept")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable Long requestId) {

        FriendRequest request = friendRequestRepository.findById(requestId).orElse(null);
        if (request == null) {
            return ResponseEntity.notFound().build();
        }

        UserProfile sender = request.getSender();
        UserProfile receiver = request.getReceiver();

        if (sender.getFriends() == null) sender.setFriends(new ArrayList<>());
        if (receiver.getFriends() == null) receiver.setFriends(new ArrayList<>());

        sender.getFriends().add(receiver);
        receiver.getFriends().add(sender);

        userProfileRepository.save(sender);
        userProfileRepository.save(receiver);

        request.setStatus("ACCEPTED");
        friendRequestRepository.save(request);

        return ResponseEntity.ok("Friend request accepted");
    }
}