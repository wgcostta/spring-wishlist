package com.lzb.wishlist.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

public record ProductRequest(
        @NotBlank(message = "Product ID is mandatory")
        String id,
        String name
) {
}
