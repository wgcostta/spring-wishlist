package com.lzb.wishlist.domain.port.out;


import com.lzb.wishlist.domain.model.WishlistTO;

import java.util.Optional;

public interface WishlistRepository {
    WishlistTO save(WishlistTO wishlist);
    Optional<WishlistTO> findByCustomerId(String customerId);
    void deleteByCustomerId(String customerId);
}