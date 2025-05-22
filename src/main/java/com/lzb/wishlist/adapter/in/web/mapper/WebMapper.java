package com.lzb.wishlist.adapter.in.web.mapper;

import com.lzb.wishlist.adapter.in.web.dto.ProductRequest;
import com.lzb.wishlist.adapter.in.web.dto.ProductResponse;
import com.lzb.wishlist.adapter.in.web.dto.WishlistResponse;

import com.lzb.wishlist.domain.model.ProductTO;
import com.lzb.wishlist.domain.model.WishlistTO;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface WebMapper {

    ProductTO toProduct(ProductRequest productRequest);

    ProductResponse toProductResponse(ProductTO product);

    WishlistResponse toWishlistResponse(WishlistTO wishlist);

    Set<ProductResponse> toProductResponseSet(Set<ProductTO> products);
}
