package com.lzb.wishlist.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lzb.wishlist.adapter.in.web.dto.ProductRequest;
import com.lzb.wishlist.adapter.out.persistence.repository.MongoWishlistRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class WishlistIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.8");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MongoWishlistRepository wishlistRepository;

    private final String CUSTOMER_ID = "customer-integration";
    private final String PRODUCT_ID = "product-integration";

    @BeforeEach
    void setUp() {
        wishlistRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        wishlistRepository.deleteAll();
    }

    @Test
    void shouldCompleteWishlistFlow() throws Exception {
        ProductRequest productRequest = new ProductRequest(PRODUCT_ID, "Test Product");

        mockMvc.perform(post("/v1/customers/{customerId}/wishlist/products", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value(CUSTOMER_ID))
                .andExpect(jsonPath("$.products", hasSize(1)))
                .andExpect(jsonPath("$.products[0].id").value(PRODUCT_ID));

        // Check if product exists in wishlist
        mockMvc.perform(get("/v1/customers/{customerId}/wishlist/products/{productId}", CUSTOMER_ID, PRODUCT_ID))
                .andExpect(status().isOk());

        // Get all products in wishlist
        mockMvc.perform(get("/v1/customers/{customerId}/wishlist/products", CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(PRODUCT_ID));

        // Remove product from wishlist
        mockMvc.perform(delete("/v1/customers/{customerId}/wishlist/products/{productId}", CUSTOMER_ID, PRODUCT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasSize(0)));

        // Verify product is no longer in wishlist
        mockMvc.perform(get("/v1/customers/{customerId}/wishlist/products/{productId}", CUSTOMER_ID, PRODUCT_ID))
                .andExpect(status().isNotFound());
    }
}