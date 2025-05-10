package com.lzb.wishlist.domain.port.in;

import com.lzb.wishlist.domain.model.ProductTO;
import com.lzb.wishlist.domain.model.WishlistTO;
import java.util.Set;

public interface WishlistUseCase {
    WishlistTO addProduct(String customerId, ProductTO product);
    WishlistTO removeProduct(String customerId, String productId);
    Set<ProductTO> getAllProducts(String customerId);
    boolean hasProduct(String customerId, String productId);
}