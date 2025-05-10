package com.lzb.wishlist.adapter.out.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "wishlists")
public class WishlistEntity {
    @Id
    private String id;

    @Indexed(unique = true)
    private String customerId;

    @Builder.Default
    private Set<ProductEntity> products = new HashSet<>();
}
