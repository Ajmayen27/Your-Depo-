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
    private Chalan chalan;


    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private Integer cartonQty;
    private Integer freeCartonQty;

    private Double pricePerCarton;

    private Double totalPrice;

}
