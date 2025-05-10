package com.lzb.wishlist.domain.model;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistTO {
    private String id;
    private String customerId;

    @Builder.Default
    private Set<ProductTO> products = new HashSet<>();

    public static final int MAX_PRODUCTS = 20;

    public boolean addProduct(ProductTO product) {
        if (products.size() >= MAX_PRODUCTS) {
            return false;
        }
        return products.add(product);
    }

    public boolean removeProduct(String productId) {
        return products.removeIf(product -> product.id().equals(productId));
    }

    public boolean containsProduct(String productId) {
        return products.stream().anyMatch(product -> product.id().equals(productId));
    }
}
