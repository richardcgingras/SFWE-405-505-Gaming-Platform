package com.example.gaming_platform.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.gaming_platform.entity.WishList;

public interface WishListRepository extends CrudRepository<WishList, Long> {
    // not sure if this is needed since a lot of this is handled by other classes


}
