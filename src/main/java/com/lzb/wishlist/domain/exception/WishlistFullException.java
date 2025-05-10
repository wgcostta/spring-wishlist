package com.lzb.wishlist.domain.exception;

public class WishlistFullException extends DomainException {
    public WishlistFullException() {
        super("Wishlist has reached the maximum limit of products");
    }
}