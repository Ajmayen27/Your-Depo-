package com.ajmayen.smartdepo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chalan_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChalanItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chalan_id")
    private Chalan chalan;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer cartonQty;

    private Double pricePerCarton;

    private Double totalPrice;

}
