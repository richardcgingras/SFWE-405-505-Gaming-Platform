package com.example.gaming_platform.service;

import org.springframework.stereotype.Service;

import com.example.gaming_platform.entity.UserProfile;
import com.example.gaming_platform.entity.VideoGame;
import com.example.gaming_platform.entity.WishList;
import com.example.gaming_platform.repository.WishListRepository;
import com.example.gaming_platform.repository.UserProfileRepository;
import com.example.gaming_platform.entity.Category;
import com.example.gaming_platform.repository.CategoryRepository;

@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepo;
    private final WishListRepository wishListRepo;
    private final CategoryRepository categoryRepo;

    public UserProfileService(UserProfileRepository userProfileRepo, WishListRepository wishListRepo, 
        CategoryRepository categoryRepo) {
        this.userProfileRepo = userProfileRepo;
        this.wishListRepo = wishListRepo;
        this.categoryRepo = categoryRepo;
    }

    // add a friend to the user's friend list
    public void addFriend(Long userId, Long friendId) {
        UserProfile user = userProfileRepo.findById(userId).orElseThrow();
        UserProfile friend = userProfileRepo.findById(friendId).orElseThrow();
        user.getFriends().add(friend);
        userProfileRepo.save(user);
    }

    // remove a friend from the user's friend list
    public void removeFriend(Long userId, Long friendId) {
        UserProfile user = userProfileRepo.findById(userId).orElseThrow();
        UserProfile friend = userProfileRepo.findById(friendId).orElseThrow();
        user.getFriends().remove(friend);
        userProfileRepo.save(user);
    }

    // add a preferred category to the user's profile
    public void addPreferredCategory(Long userId, Long categoryId) {
        UserProfile user = userProfileRepo.findById(userId).orElseThrow();
        Category category = categoryRepo.findById(categoryId).orElseThrow();
        user.getPreferredCategories().add(category);
        userProfileRepo.save(user);
    }

    // remove a preferred category from the user's profile
    public void removePreferredCategory(Long userId, Long categoryId) {
        UserProfile user = userProfileRepo.findById(userId).orElseThrow();
        Category category = categoryRepo.findById(categoryId).orElseThrow();
        user.getPreferredCategories().remove(category);
        userProfileRepo.save(user);
    }

    // edit status
    public void editStatus(Long userId, String newStatus) {
        UserProfile user = userProfileRepo.findById(userId).orElseThrow();
        user.setStatus(newStatus);
        userProfileRepo.save(user);
    }

    // edit bio
    public void editBio(Long userId, String newBio) {
        // NOTE ----------
        // we need a way to limit the length of the bio
        UserProfile user = userProfileRepo.findById(userId).orElseThrow();
        validateBio(newBio); // needs more work, just temporary
        user.setBio(newBio);
        userProfileRepo.save(user);
    }

    // validate bio
    public void validateBio(String bio) {
        // NOTE ----------
        // not sure if this is the best way to implement this
        if (bio.length() > 500) {
            throw new IllegalArgumentException("Bio cannot be longer than 500 characters");
            // this should also prompt user to try again
            // it would be better if we had a counter on the front end that showed how many characters they could still add
            // similar to what discord does when you edit your profile
        }
    }
    
}
