package com.lzb.wishlist.adapter.out.persistence.repository;

import com.lzb.wishlist.adapter.out.persistence.entity.WishlistEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoWishlistRepository extends MongoRepository<WishlistEntity, String> {
    Optional<WishlistEntity> findByCustomerId(String customerId);
    void deleteByCustomerId(String customerId);
}
