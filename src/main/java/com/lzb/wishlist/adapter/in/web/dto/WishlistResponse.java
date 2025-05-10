package com.lzb.wishlist.adapter.in.web.dto;


import java.util.Set;

public record WishlistResponse(
        String customerId,
        Set<ProductResponse> products
) {
}