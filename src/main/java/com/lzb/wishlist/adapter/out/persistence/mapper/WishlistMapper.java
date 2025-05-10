package com.lzb.wishlist.adapter.out.persistence.mapper;

import com.lzb.wishlist.adapter.out.persistence.entity.ProductEntity;
import com.lzb.wishlist.adapter.out.persistence.entity.WishlistEntity;


import com.lzb.wishlist.domain.model.ProductTO;
import com.lzb.wishlist.domain.model.WishlistTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WishlistMapper {

    ProductTO toProduct(ProductEntity productEntity);
    ProductEntity toProductEntity(ProductTO product);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "products", source = "products")
    WishlistTO toWishlist(WishlistEntity wishlistEntity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "products", source = "products")
    WishlistEntity toWishlistEntity(WishlistTO wishlist);
}