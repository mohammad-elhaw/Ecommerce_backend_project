package com.backend.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products", indexes = @Index(name = "idx_discount_end_date", columnList = "discountEndDate"))
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long productId;
    private String productName;
    private String shortDescription;
    private String longDescription;
    private Double price;
    private double discountPercent;
    private LocalDateTime discountStartDate;
    private LocalDateTime discountEndDate;
    private double discountPrice;
    private String imageUrl;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @JsonManagedReference
    @OneToOne(mappedBy = "product", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false)
    private Inventory inventory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Column(name = "num_rating")
    private int numRating;

    private LocalDateTime createdAt;

}
