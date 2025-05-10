package com.lzb.wishlist.adapter.out.persistence;

import com.lzb.wishlist.adapter.out.persistence.mapper.WishlistMapper;
import com.lzb.wishlist.adapter.out.persistence.repository.MongoWishlistRepository;

import com.lzb.wishlist.domain.model.WishlistTO;
import com.lzb.wishlist.domain.port.out.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WishlistRepositoryAdapter implements WishlistRepository {

    private final MongoWishlistRepository mongoWishlistRepository;
    private final WishlistMapper wishlistMapper;

    @Override
    public WishlistTO save(WishlistTO wishlist) {
        var wishlistEntity = wishlistMapper.toWishlistEntity(wishlist);
        var savedEntity = mongoWishlistRepository.save(wishlistEntity);
        return wishlistMapper.toWishlist(savedEntity);
    }

    @Override
    public Optional<WishlistTO> findByCustomerId(String customerId) {
        return mongoWishlistRepository.findByCustomerId(customerId)
                .map(wishlistMapper::toWishlist);
    }

    @Override
    public void deleteByCustomerId(String customerId) {
        mongoWishlistRepository.deleteByCustomerId(customerId);
    }
}