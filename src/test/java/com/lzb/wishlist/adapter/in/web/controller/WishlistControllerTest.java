package com.lzb.wishlist.adapter.in.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lzb.wishlist.adapter.in.web.dto.ProductRequest;
import com.lzb.wishlist.adapter.in.web.mapper.WebMapper;
import com.lzb.wishlist.domain.exception.ProductNotFoundException;
import com.lzb.wishlist.domain.exception.WishlistFullException;
import com.lzb.wishlist.domain.exception.WishlistNotFoundException;
import com.lzb.wishlist.domain.model.ProductTO;
import com.lzb.wishlist.domain.model.WishlistTO;
import com.lzb.wishlist.domain.port.in.WishlistUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WishlistController.class)
class WishlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WishlistUseCase wishlistUseCase;

    @MockitoBean
    private WebMapper webMapper;

    private final String CUSTOMER_ID = "customer-123";
    private final String PRODUCT_ID = "product-123";
    private ProductTO product;
    private WishlistTO wishlist;
    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        product = new ProductTO(PRODUCT_ID, "Test Product");

        productRequest = new ProductRequest(PRODUCT_ID, "Test Product");

        wishlist = WishlistTO.builder()
                .id("wishlist-123")
                .customerId(CUSTOMER_ID)
                .products(new HashSet<>(Set.of(product)))
                .build();

        when(webMapper.toProduct(any(ProductRequest.class))).thenReturn(product);
    }

    @Test
    void shouldAddProductToWishlist() throws Exception {
        when(wishlistUseCase.addProduct(eq(CUSTOMER_ID), any(ProductTO.class))).thenReturn(wishlist);

        mockMvc.perform(post("/v1/customers/{customerId}/wishlist/products", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnBadRequestWhenAddingProductToFullWishlist() throws Exception {
        when(wishlistUseCase.addProduct(eq(CUSTOMER_ID), any(ProductTO.class)))
                .thenThrow(new WishlistFullException());

        mockMvc.perform(post("/v1/customers/{customerId}/wishlist/products", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void shouldRemoveProductFromWishlist() throws Exception {
        when(wishlistUseCase.removeProduct(CUSTOMER_ID, PRODUCT_ID)).thenReturn(wishlist);

        mockMvc.perform(delete("/v1/customers/{customerId}/wishlist/products/{productId}", CUSTOMER_ID, PRODUCT_ID))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenRemovingNonExistentProduct() throws Exception {
        when(wishlistUseCase.removeProduct(CUSTOMER_ID, PRODUCT_ID))
                .thenThrow(new ProductNotFoundException(PRODUCT_ID));

        mockMvc.perform(delete("/v1/customers/{customerId}/wishlist/products/{productId}", CUSTOMER_ID, PRODUCT_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldGetAllProductsFromWishlist() throws Exception {
        when(wishlistUseCase.getAllProducts(CUSTOMER_ID)).thenReturn(Set.of(product));

        mockMvc.perform(get("/v1/customers/{customerId}/wishlist/products", CUSTOMER_ID))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenWishlistDoesNotExist() throws Exception {
        when(wishlistUseCase.getAllProducts(CUSTOMER_ID))
                .thenThrow(new WishlistNotFoundException(CUSTOMER_ID));

        mockMvc.perform(get("/v1/customers/{customerId}/wishlist/products", CUSTOMER_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldReturnOkWhenProductExistsInWishlist() throws Exception {
        when(wishlistUseCase.hasProduct(CUSTOMER_ID, PRODUCT_ID)).thenReturn(true);

        mockMvc.perform(get("/v1/customers/{customerId}/wishlist/products/{productId}", CUSTOMER_ID, PRODUCT_ID))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenProductDoesNotExistInWishlist() throws Exception {
        when(wishlistUseCase.hasProduct(CUSTOMER_ID, PRODUCT_ID)).thenReturn(false);

        mockMvc.perform(get("/v1/customers/{customerId}/wishlist/products/{productId}", CUSTOMER_ID, PRODUCT_ID))
                .andExpect(status().isNotFound());
    }
}