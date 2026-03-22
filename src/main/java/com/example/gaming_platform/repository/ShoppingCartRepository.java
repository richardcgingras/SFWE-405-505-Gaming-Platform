package com.example.gaming_platform.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.gaming_platform.entity.ShoppingCart;
import com.example.gaming_platform.entity.UserProfile;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long> {
    public ShoppingCart findByAccount(UserProfile account);
}
