package com.lzb.wishlist.adapter.out.persistence.mapper;

import com.lzb.wishlist.adapter.out.persistence.entity.ProductEntity;
import com.lzb.wishlist.adapter.out.persistence.entity.WishlistEntity;


import com.lzb.wishlist.domain.model.ProductTO;
import com.lzb.wishlist.domain.model.WishlistTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WishlistMapper {

    ProductTO toProduct(ProductEntity productEntity);
    ProductEntity toProductEntity(ProductTO product);

    WishlistTO toWishlist(WishlistEntity wishlistEntity);

    WishlistEntity toWishlistEntity(WishlistTO wishlist);
}