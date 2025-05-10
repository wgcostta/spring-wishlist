package com.lzb.wishlist.domain.exception;

public class WishlistNotFoundException extends DomainException {
    public WishlistNotFoundException(String customerId) {
        super("Wishlist not found for customer " + customerId);
    }
}
