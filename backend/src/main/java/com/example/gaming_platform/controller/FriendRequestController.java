package com.example.gaming_platform.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        System.out.println("FriendRequestController hit");
        System.out.println("senderId = " + request.getSenderId());
        System.out.println("receiverId = " + request.getReceiverId());

        // check that both ids were sent
        if (request.getSenderId() == null || request.getReceiverId() == null) {
            return ResponseEntity.badRequest().body("senderId and receiverId are required");
        }

        // user cannot send a request to themself
        if (request.getSenderId().equals(request.getReceiverId())) {
            return ResponseEntity.badRequest().body("Cannot send request to yourself");
        }

        UserProfile sender = userProfileRepository.findById(request.getSenderId()).orElse(null);
        UserProfile receiver = userProfileRepository.findById(request.getReceiverId()).orElse(null);

        System.out.println("sender found = " + (sender != null));
        System.out.println("receiver found = " + (receiver != null));

        if (sender == null || receiver == null) {
            return ResponseEntity.notFound().build();
        }

        // check if already friends
        if (sender.getFriends() != null &&
            sender.getFriends().stream().anyMatch(f -> f.getId().equals(receiver.getId()))) {
            return ResponseEntity.badRequest().body("Already friends");
        }

        // check if request already exists in either direction
        boolean alreadyPending =
                friendRequestRepository.existsBySenderAndReceiverAndStatus(sender, receiver, "PENDING") ||
                friendRequestRepository.existsBySenderAndReceiverAndStatus(receiver, sender, "PENDING");

        System.out.println("alreadyPending = " + alreadyPending);

        if (alreadyPending) {
            return ResponseEntity.badRequest().body("Request already pending");
        }

        // create and save new request
        FriendRequest saved = friendRequestRepository.save(
                new FriendRequest(sender, receiver, "PENDING")
        );

        System.out.println("saved request id = " + saved.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("id", saved.getId());
        response.put("status", saved.getStatus());
        response.put("message", "Request Sent");

        return ResponseEntity.ok(response);
    }

    /**
     * Gets the friend status between two users.
     */
    @GetMapping("/status")
    public ResponseEntity<?> getFriendStatus(
            @RequestParam Long senderId,
            @RequestParam Long receiverId
    ) {
        UserProfile sender = userProfileRepository.findById(senderId).orElse(null);
        UserProfile receiver = userProfileRepository.findById(receiverId).orElse(null);

        if (sender == null || receiver == null) {
            return ResponseEntity.notFound().build();
        }

        if (sender.getFriends() != null &&
            sender.getFriends().stream().anyMatch(f -> f.getId().equals(receiver.getId()))) {
            return ResponseEntity.ok("FRIENDS");
        }

        boolean pending =
                friendRequestRepository.existsBySenderAndReceiverAndStatus(sender, receiver, "PENDING") ||
                friendRequestRepository.existsBySenderAndReceiverAndStatus(receiver, sender, "PENDING");

        if (pending) {
            return ResponseEntity.ok("PENDING");
        }

        return ResponseEntity.ok("NONE");
    }

    /**
     * Gets all incoming friend requests for a user.
     */
    @GetMapping("/received/{userId}")
    public ResponseEntity<?> getReceivedRequests(@PathVariable Long userId) {

        System.out.println("getReceivedRequests hit for userId = " + userId);

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

        System.out.println("acceptFriendRequest hit for requestId = " + requestId);

        FriendRequest request = friendRequestRepository.findById(requestId).orElse(null);
        if (request == null) {
            return ResponseEntity.notFound().build();
        }

        UserProfile sender = request.getSender();
        UserProfile receiver = request.getReceiver();

        if (sender.getFriends() == null) {
            sender.setFriends(new ArrayList<>());
        }

        if (receiver.getFriends() == null) {
            receiver.setFriends(new ArrayList<>());
        }

        // avoid duplicate friend entries
        if (sender.getFriends().stream().noneMatch(f -> f.getId().equals(receiver.getId()))) {
            sender.getFriends().add(receiver);
        }

        if (receiver.getFriends().stream().noneMatch(f -> f.getId().equals(sender.getId()))) {
            receiver.getFriends().add(sender);
        }

        userProfileRepository.save(sender);
        userProfileRepository.save(receiver);

        request.setStatus("ACCEPTED");
        friendRequestRepository.save(request);

        return ResponseEntity.ok("Friend request accepted");
    }

    /**
    * Denies a friend request.
    */
    @PostMapping("/{requestId}/deny")
    public ResponseEntity<?> denyFriendRequest(@PathVariable Long requestId) {

        FriendRequest request = friendRequestRepository.findById(requestId).orElse(null);
        if (request == null) {
            return ResponseEntity.notFound().build();
        }

        request.setStatus("DENIED");
        friendRequestRepository.save(request);

        return ResponseEntity.ok("Friend request denied");
    }
}