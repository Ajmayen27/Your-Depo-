package com.ajmayen.smartdepo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id",unique = true,nullable = false)
    private Product product;

    private Integer currentCartonQty = 0;

    private LocalDateTime updatedAt = LocalDateTime.now();

}
