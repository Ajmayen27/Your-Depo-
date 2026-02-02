package com.ajmayen.smartdepo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String skuCode;

    @Column(nullable = false)
    private Integer perCartonPieces;

    @Column(nullable = false)
    private Double pricePerCarton;

    private LocalDateTime createdAt = LocalDateTime.now();


}
