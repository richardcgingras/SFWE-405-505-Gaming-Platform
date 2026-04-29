package com.example.gaming_platform.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.gaming_platform.entity.UserProfile;
import com.example.gaming_platform.entity.WishList;

/**
 * Repository for persisting and querying wish list records.
 */
public interface WishListRepository extends CrudRepository<WishList, Long> {
    // not sure if this is needed since a lot of this is handled by other classes
    public WishList findByAccount(UserProfile account);

}
