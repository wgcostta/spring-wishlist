package com.lzb.wishlist.adapter.in.web.controller;

import com.lzb.wishlist.adapter.in.web.dto.ErrorResponse;
import com.lzb.wishlist.adapter.in.web.dto.ProductRequest;
import com.lzb.wishlist.adapter.in.web.dto.ProductResponse;
import com.lzb.wishlist.adapter.in.web.dto.WishlistResponse;
import com.lzb.wishlist.adapter.in.web.mapper.WebMapper;
import com.lzb.wishlist.domain.model.ProductTO;
import com.lzb.wishlist.domain.port.in.WishlistUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/v1/customers/{customerId}/wishlist")
@RequiredArgsConstructor
@Tag(name = "Wishlist", description = "Wishlist management endpoints")
public class WishlistController {

    private final WishlistUseCase wishlistUseCase;
    private final WebMapper webMapper;

    @PostMapping("/products")
    @Operation(summary = "Add product to wishlist",
            description = "Adds a product to the customer's wishlist. Limited to 20 products per wishlist.")
    @ApiResponse(responseCode = "201", description = "Product added successfully")
    @ApiResponse(responseCode = "400", description = "Wishlist is full",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<WishlistResponse> addProduct(
            @PathVariable String customerId,
            @Valid @RequestBody ProductRequest productRequest) {

        ProductTO product = webMapper.toProduct(productRequest);
        var wishlist = wishlistUseCase.addProduct(customerId, product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(webMapper.toWishlistResponse(wishlist));
    }

    @DeleteMapping("/products/{productId}")
    @Operation(summary = "Remove product from wishlist",
            description = "Removes a product from the customer's wishlist.")
    @ApiResponse(responseCode = "200", description = "Product removed successfully")
    @ApiResponse(responseCode = "404", description = "Product not found in wishlist",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<WishlistResponse> removeProduct(
            @PathVariable String customerId,
            @PathVariable String productId) {

        var wishlist = wishlistUseCase.removeProduct(customerId, productId);
        return ResponseEntity.ok(webMapper.toWishlistResponse(wishlist));
    }

    @GetMapping("/products")
    @Operation(summary = "Get all wishlist products",
            description = "Returns all products from the customer's wishlist.")
    @ApiResponse(responseCode = "200", description = "Products successfully retrieved")
    @ApiResponse(responseCode = "404", description = "Wishlist not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<Set<ProductResponse>> getAllProducts(
            @PathVariable String customerId) {

        Set<ProductTO> products = wishlistUseCase.getAllProducts(customerId);
        return ResponseEntity.ok(webMapper.toProductResponseSet(products));
    }

    @GetMapping("/products/{productId}")
    @Operation(summary = "Check if product is in wishlist",
            description = "Checks if a specific product is in the customer's wishlist.")
    @ApiResponse(responseCode = "200", description = "Product found in wishlist")
    @ApiResponse(responseCode = "404", description = "Product not found in wishlist or wishlist not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<Void> hasProduct(
            @PathVariable String customerId,
            @PathVariable String productId) {

        boolean hasProduct = wishlistUseCase.hasProduct(customerId, productId);

        return hasProduct ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

}