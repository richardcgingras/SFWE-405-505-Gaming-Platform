package com.example.gaming_platform.repository;

import com.example.gaming_platform.entity.UserProfile;
import org.springframework.data.repository.CrudRepository;

public interface UserProfileRepository extends CrudRepository<UserProfile, Long> {
    UserProfile findByUserName(String name);

    UserProfile findByEmail(String email);
}
