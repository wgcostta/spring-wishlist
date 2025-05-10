package com.lzb.wishlist.domain.exception;

public class ProductNotFoundException extends DomainException {
    public ProductNotFoundException(String productId) {
        super("Product with id " + productId + " not found in wishlist");
    }
}