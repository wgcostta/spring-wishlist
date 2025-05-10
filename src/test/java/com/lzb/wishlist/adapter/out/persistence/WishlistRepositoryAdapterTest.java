package com.lzb.wishlist.adapter.out.persistence;

import com.lzb.wishlist.adapter.out.persistence.entity.ProductEntity;
import com.lzb.wishlist.adapter.out.persistence.entity.WishlistEntity;
import com.lzb.wishlist.adapter.out.persistence.mapper.WishlistMapper;
import com.lzb.wishlist.adapter.out.persistence.repository.MongoWishlistRepository;
import com.lzb.wishlist.domain.model.ProductTO;
import com.lzb.wishlist.domain.model.WishlistTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WishlistRepositoryAdapterTest {

    @Mock
    private MongoWishlistRepository mongoWishlistRepository;

    @Mock
    private WishlistMapper wishlistMapper;

    @InjectMocks
    private WishlistRepositoryAdapter wishlistRepositoryAdapter;

    private final String CUSTOMER_ID = "customer-123";
    private ProductTO product;
    private WishlistTO wishlist;
    private ProductEntity productEntity;
    private WishlistEntity wishlistEntity;

    @BeforeEach
    void setUp() {
        product = new ProductTO("product-123", "Test Product");

        wishlist = new WishlistTO("wishlist-123", CUSTOMER_ID, new HashSet<>(Set.of(product)));

        productEntity = ProductEntity.builder()
                .id("product-123")
                .name("Test Product")
                .build();

        wishlistEntity = WishlistEntity.builder()
                .id("wishlist-123")
                .customerId(CUSTOMER_ID)
                .products(new HashSet<>(Set.of(productEntity)))
                .build();
    }

    @Test
    void shouldSaveWishlist() {
        when(wishlistMapper.toWishlistEntity(wishlist)).thenReturn(wishlistEntity);
        when(mongoWishlistRepository.save(wishlistEntity)).thenReturn(wishlistEntity);
        when(wishlistMapper.toWishlist(wishlistEntity)).thenReturn(wishlist);

        WishlistTO result = wishlistRepositoryAdapter.save(wishlist);

        assertNotNull(result);
        assertEquals(wishlist.getId(), result.getId());
        assertEquals(wishlist.getCustomerId(), result.getCustomerId());
        verify(mongoWishlistRepository).save(wishlistEntity);
    }

    @Test
    void shouldFindWishlistByCustomerId() {
        when(mongoWishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlistEntity));
        when(wishlistMapper.toWishlist(wishlistEntity)).thenReturn(wishlist);

        Optional<WishlistTO> result = wishlistRepositoryAdapter.findByCustomerId(CUSTOMER_ID);

        assertTrue(result.isPresent());
        assertEquals(wishlist, result.get());
    }

    @Test
    void shouldReturnEmptyWhenWishlistNotFound() {
        when(mongoWishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.empty());

        Optional<WishlistTO> result = wishlistRepositoryAdapter.findByCustomerId(CUSTOMER_ID);

        assertFalse(result.isPresent());
    }

    @Test
    void shouldDeleteWishlistByCustomerId() {
        wishlistRepositoryAdapter.deleteByCustomerId(CUSTOMER_ID);

        verify(mongoWishlistRepository).deleteByCustomerId(CUSTOMER_ID);
    }
}