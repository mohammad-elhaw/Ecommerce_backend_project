package com.backend.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "short_description", nullable = false)
    private String shortDescription;

    @Column(name = "long_description")
    private String longDescription;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "discount_price")
    private Double discountPrice;

    @Column(name = "discount_percent")
    private Double discountPercent;


    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToOne(mappedBy = "product", cascade = CascadeType.REMOVE, optional = false, orphanRemoval = true)
    private Inventory inventory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Column(name = "num_rating")
    private int numRating;

    private LocalDateTime createdAt;

}
