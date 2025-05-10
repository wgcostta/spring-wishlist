package com.lzb.wishlist.application.service;

import com.lzb.wishlist.domain.exception.ProductNotFoundException;
import com.lzb.wishlist.domain.exception.WishlistFullException;
import com.lzb.wishlist.domain.exception.WishlistNotFoundException;

import com.lzb.wishlist.domain.model.ProductTO;
import com.lzb.wishlist.domain.model.WishlistTO;
import com.lzb.wishlist.domain.port.in.WishlistUseCase;
import com.lzb.wishlist.domain.port.out.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class WishlistService implements WishlistUseCase {

    private final WishlistRepository wishlistRepository;

    @Override
    public WishlistTO addProduct(String customerId, ProductTO product) {
        WishlistTO wishlist = wishlistRepository.findByCustomerId(customerId)
                .orElse(WishlistTO.builder().customerId(customerId).build());

        if (!wishlist.addProduct(product)) {
            throw new WishlistFullException();
        }

        return wishlistRepository.save(wishlist);
    }

    @Override
    public WishlistTO removeProduct(String customerId, String productId) {
        WishlistTO wishlist = getWishlistOrThrow(customerId);

        if (!wishlist.removeProduct(productId)) {
            throw new ProductNotFoundException(productId);
        }

        return wishlistRepository.save(wishlist);
    }

    @Override
    public Set<ProductTO> getAllProducts(String customerId) {
        WishlistTO wishlist = getWishlistOrThrow(customerId);
        return wishlist.getProducts();
    }

    @Override
    public boolean hasProduct(String customerId, String productId) {
        WishlistTO wishlist = getWishlistOrThrow(customerId);
        return wishlist.containsProduct(productId);
    }

    private WishlistTO getWishlistOrThrow(String customerId) {
        return wishlistRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new WishlistNotFoundException(customerId));
    }
}