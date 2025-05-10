package com.lzb.wishlist.application.service;

import com.lzb.wishlist.domain.exception.ProductNotFoundException;
import com.lzb.wishlist.domain.exception.WishlistFullException;
import com.lzb.wishlist.domain.exception.WishlistNotFoundException;
import com.lzb.wishlist.domain.model.ProductTO;
import com.lzb.wishlist.domain.model.WishlistTO;
import com.lzb.wishlist.domain.port.out.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @InjectMocks
    private WishlistService wishlistService;

    private final String CUSTOMER_ID = "customer-123";
    private final String PRODUCT_ID = "product-123";
    private ProductTO product;
    private WishlistTO wishlist;

    @BeforeEach
    void setUp() {
        product = new ProductTO(PRODUCT_ID, "Test Product");

        wishlist = new WishlistTO("wishlist-123", CUSTOMER_ID, new HashSet<>());
    }

    @Test
    void shouldAddProductToNewWishlist() {
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.empty());
        when(wishlistRepository.save(any(WishlistTO.class))).thenAnswer(invocation -> invocation.getArgument(0));

        WishlistTO result = wishlistService.addProduct(CUSTOMER_ID, product);

        assertNotNull(result);
        assertEquals(1, result.getProducts().size());
        assertTrue(result.getProducts().contains(product));
        verify(wishlistRepository).save(any(WishlistTO.class));
    }

    @Test
    void shouldAddProductToExistingWishlist() {
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));
        when(wishlistRepository.save(any(WishlistTO.class))).thenAnswer(invocation -> invocation.getArgument(0));

        WishlistTO result = wishlistService.addProduct(CUSTOMER_ID, product);

        assertNotNull(result);
        assertEquals(1, result.getProducts().size());
        assertTrue(result.getProducts().contains(product));
        verify(wishlistRepository).save(wishlist);
    }

    @Test
    void shouldThrowExceptionWhenWishlistIsFull() {
        // Create a wishlist with maximum products
        Set<ProductTO> products = IntStream.range(0, WishlistTO.MAX_PRODUCTS)
                .mapToObj(i -> new ProductTO("product-" + i, "Product " + i))
                .collect(Collectors.toSet());

        wishlist.setProducts(products);

        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));

        assertThrows(WishlistFullException.class, () -> wishlistService.addProduct(CUSTOMER_ID, product));
        verify(wishlistRepository, never()).save(any(WishlistTO.class));
    }

    @Test
    void shouldRemoveProductFromWishlist() {
        wishlist.getProducts().add(product);
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));
        when(wishlistRepository.save(any(WishlistTO.class))).thenAnswer(invocation -> invocation.getArgument(0));

        WishlistTO result = wishlistService.removeProduct(CUSTOMER_ID, PRODUCT_ID);

        assertNotNull(result);
        assertTrue(result.getProducts().isEmpty());
        verify(wishlistRepository).save(wishlist);
    }

    @Test
    void shouldThrowExceptionWhenRemovingNonExistentProduct() {
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));

        assertThrows(ProductNotFoundException.class, () -> wishlistService.removeProduct(CUSTOMER_ID, PRODUCT_ID));
        verify(wishlistRepository, never()).save(any(WishlistTO.class));
    }

    @Test
    void shouldGetAllProductsFromWishlist() {
        wishlist.getProducts().add(product);
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));

        Set<ProductTO> result = wishlistService.getAllProducts(CUSTOMER_ID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(product));
    }

    @Test
    void shouldCheckIfProductExistsInWishlist() {
        wishlist.getProducts().add(product);
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));

        boolean result = wishlistService.hasProduct(CUSTOMER_ID, PRODUCT_ID);

        assertTrue(result);
    }

    @Test
    void shouldThrowExceptionWhenWishlistNotFound() {
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.empty());

        assertThrows(WishlistNotFoundException.class, () -> wishlistService.getAllProducts(CUSTOMER_ID));
    }
}