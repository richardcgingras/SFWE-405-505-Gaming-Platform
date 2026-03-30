package com.example.gaming_platform.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.gaming_platform.entity.ShoppingCart;
import com.example.gaming_platform.entity.UserProfile;

/**
 * Repository for persisting and querying shopping cart records.
 */
public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long> {
/**
 * Finds by account.
 *
 * @param account the account
 * @return the matching by account
 */
    public ShoppingCart findByAccount(UserProfile account);
}
